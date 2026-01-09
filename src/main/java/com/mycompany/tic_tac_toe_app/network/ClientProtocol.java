package com.mycompany.tic_tac_toe_app.network;

import com.mycompany.tic_tac_toe_app.controllers.GameController;
import com.mycompany.tic_tac_toe_app.game.online_mode.OnlineGame;
import com.mycompany.tic_tac_toe_app.model.PlayerDTO;
import com.mycompany.tic_tac_toe_app.model.PlayerDTO.PlayerStatus;
import com.mycompany.tic_tac_toe_app.util.Functions;
import com.mycompany.tic_tac_toe_app.game.util.GameListener;
import com.mycompany.tic_tac_toe_app.game.util.GameMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.application.Platform;

public class ClientProtocol {

    private final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    private final String REGISTER_SUCCESS = "REGISTER_SUCCESS";
    private final String ERROR = "ERROR";
    private final String HISTORY_RESPONSE = "HISTORY_RESPONSE";
    private final String RECEIVE_INVITE = "RECEIVE_INVITE";
    private final String INVITE_FAIL = "INVITE_FAIL";
    private final String INVITE_ACCEPTED = "INVITE_ACCEPTED";
    private final String INVITE_REJECTED = "INVITE_REJECTED";
    private final String PLAYER_LIST = "PLAYER_LIST";
    private final String GAME_START = "GAME_START";
    private final String MOVE_VALID = "MOVE_VALID";
    private final String GAME_OVER = "GAME_OVER";

    private final List<String> savedGamesList = new ArrayList<>();
    private final Set<PlayerDTO> players = new HashSet<>();
    private static OnlineGame onlineGame;

    private static ClientProtocol INSTANCE;

    public void setOnlineGame(OnlineGame onlineGame) {
        this.onlineGame = onlineGame;
    }

    private ClientProtocol() {
    }

    public synchronized static ClientProtocol getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClientProtocol();
        }

        return INSTANCE;
    }

    public void processMessage(String msg, Client client) {
        if (msg == null || msg.trim().isEmpty()) {
            return;
        }

        final String[] parts = msg.split(":");
        final String messageType = parts[0];

        switch (messageType) {
            case LOGIN_SUCCESS:
                onLoginSuccess(parts, client);
                break;

            case GAME_START:
                GameController.setGameMode(GameMode.ONLINE_MULTIPLAYER);
                Platform.runLater(() -> Functions.naviagteTo("fxml/game"));
                break;

            case MOVE_VALID:
                if (onlineGame.getGameListener() != null && parts.length >= 4) {
                    int r = Integer.parseInt(parts[1]);
                    int c = Integer.parseInt(parts[2]);
                    String sym = parts[3];
                    Platform.runLater(() -> onlineGame.getGameListener().onPlayerMove(r, c, sym,onlineGame.getOnMoveListener()));
                }
                break;

            case GAME_OVER:
                if (onlineGame.getGameListener() != null && parts.length >= 2) {
                    Platform.runLater(() -> onlineGame.getGameListener().onGameOver(parts[1], onlineGame.getOnResultListener()));
                }
                break;

            case REGISTER_SUCCESS:
                onRegisterSuccess();
                break;

            case HISTORY_RESPONSE:
                onSavedGamesReceived(msg, client);
                break;

            case RECEIVE_INVITE:
                String inviteSenderUsername = parts[1];
                onReceiveInvite(inviteSenderUsername, client);
                break;

            case ERROR:
                onError(parts[1]);
                break;

            case INVITE_FAIL:
                onError(parts[1]);
                break;

            case INVITE_ACCEPTED:
                onInviteAccepted();
                break;

            case INVITE_REJECTED:
                onInviteRejected(parts[1]);
                break;

            case PLAYER_LIST:
                onPlayerList(parts);
                break;

            default:
                break;
        }
    }

    private void onError(String message) {
        Functions.showErrorAlert(new Exception(message));
    }

    private void onLoginSuccess(String[] parts, Client client) {
        String username = parts[1];
        int score = Integer.parseInt(parts[2]);
        PlayerStatus status = PlayerStatus.IDLE;

        client.setPlayer(new PlayerDTO(username, score, status));
        Functions.naviagteTo("fxml/menu");
    }

    private void onRegisterSuccess() {
        Functions.naviagteTo("fxml/login");
    }

    private void onSavedGamesReceived(String msg, Client client) {
        savedGamesList.clear();

        final String data = msg.substring(HISTORY_RESPONSE.length() + 1);

        if (data.trim().isEmpty()) {
            return;
        }

        final String[] games = data.split(";");

        for (String gameStr : games) {
            final String[] details = gameStr.split(",");

            if (details.length >= 4) {
                final String id = details[0];
                final String opponent = details[1];
                final String result = details[2];
                final String date = details[3];

                final String[] dates = date.split(" ");

                final String displayText = String.format("Match #%s: %s Vs %s (%s) %s", id, client.getName(), opponent, result, dates[0]);

                savedGamesList.add(displayText);
            }
        }
    }

    private void onReceiveInvite(final String senderUserName, final Client client) {
        Functions.showConfirmAlert(
                "Match Request",
                null,
                senderUserName + " sent you an invitation to play a game",
                "Accept",
                "Decline",
                () -> {
                    String message = new StringBuilder("INVITE_RESPONSE:").append(senderUserName).append(":").append("ACCEPTED").toString();
                    client.sendMessage(message);
                    return true;
                },
                () -> {
                    String message = new StringBuilder("INVITE_RESPONSE:").append(senderUserName).append(":").append("REJECTED").toString();
                    client.sendMessage(message);
                    return true;
                });
    }

    private void onInviteAccepted() {
        Functions.naviagteTo("fxml/game");
    }

    private void onInviteRejected(final String username) {
        Functions.showInformationAlert("Invitation Rejected", username + " Rejected your invitation");
    }

    private void onPlayerList(String[] parts) {
        final String playersDataWithSemiColon = parts[1];

        final String[] playersCsv = playersDataWithSemiColon.split(";");

        for (final String csvValue : playersCsv) {
            final String[] player = csvValue.split(",");

            final String username = player[0];
            final int score = Integer.parseInt(player[1]);

            PlayerDTO playerDTO = new PlayerDTO(username, score, PlayerStatus.IDLE);

            players.add(playerDTO);
        }
    }

    public List<String> getSavedGames() {
        return savedGamesList;
    }

    public Set<PlayerDTO> getPlayers() {
        return players;
    }
}

package com.mycompany.tic_tac_toe_app.network;

import com.mycompany.tic_tac_toe_app.controllers.GameController;
import com.mycompany.tic_tac_toe_app.game.online_mode.OnlineGame;
import com.mycompany.tic_tac_toe_app.model.PlayerDTO;
import com.mycompany.tic_tac_toe_app.model.PlayerDTO.PlayerStatus;
import com.mycompany.tic_tac_toe_app.util.Functions;
import com.mycompany.tic_tac_toe_app.game.util.GameMode;
import com.mycompany.tic_tac_toe_app.util.Router;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.util.Pair;

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
    private final String REPLAY_REQUESTED_BY = "REPLAY_REQUESTED_BY";
    private final String SAVE_REPLAY_DATA = "SAVE_REPLAY_DATA";

    private final List<String> savedGamesList = new ArrayList<>();
    private final Set<PlayerDTO> players = new HashSet<>();
    private static OnlineGame onlineGame;

    private Consumer<List<PlayerDTO>> updatePlayerList;

    public void setOnNewPlayerListener(Consumer<List<PlayerDTO>> updatePlayerList) {
        this.updatePlayerList = updatePlayerList;
    }
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
                onGameStart(parts);
                break;

            case MOVE_VALID:
                onMoveValid(parts);
                break;

            case GAME_OVER:
                onGameOver(parts, client);
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

            case REPLAY_REQUESTED_BY:
                onReceiveReplayRequest(parts[1], client);
                break;
                
            case SAVE_REPLAY_DATA:
                if (parts.length > 1) {
                    String steps = parts[1];
                    // Store the steps in OnlineGame so it's ready when the game ends
                    if (onlineGame != null && onlineGame.isSaveable) {
                        onlineGame.saveReplayToFile(steps);
                    }

                }
                break;

            default:
                break;
        }
    }

    private void onError(String message) {
        Functions.showErrorAlert(new Exception(message));
    }

    private void onMoveValid(String[] parts) {
        if (onlineGame.getGameListener() != null && parts.length >= 4) {
            int r = Integer.parseInt(parts[1]);
            int c = Integer.parseInt(parts[2]);
            String sym = parts[3];
            onlineGame.getOnMoveListener().onMove(r, c, sym);
        }
    }

    private void onGameStart(String[] parts) {
        String sym = parts[1];
        String opponentName = parts[2];
        GameController.setGameMode(GameMode.ONLINE_MULTIPLAYER);

        GameController.setPlayerX((sym.equals("X")) ? Client.getInstance().getPlayer().getUserName() : opponentName);
        GameController.setPlayerO((sym.equals("O")) ? Client.getInstance().getPlayer().getUserName() : opponentName);
        askToSaveGame();
        Router.getInstance().navigateTo("game");
    }

    private void onGameOver(String[] parts, Client client) {
        // parts structure:
        // WIN -> [GAME_OVER, WIN, Score, CoordsString]
        // LOSE -> [GAME_OVER, LOSE, CoordsString]
        // DRAW -> [GAME_OVER, DRAW, Score]

        String result = parts[1];
        String coordsString = "";

        if (onlineGame.getGameListener() != null && parts.length >= 2) {

            // Handle Score update for Winner/Draw
            if (result.equals("WIN") && parts.length > 2) {
                client.getPlayer().setScore(Integer.parseInt(parts[2]));
            } else if (result.equals("DRAW") && parts.length > 2) {
                client.getPlayer().setScore(Integer.parseInt(parts[2]));
            }

            // Extract Coordinates Logic
            if (result.equals("WIN") && parts.length > 3) {
                coordsString = parts[3];
            } else if (result.equals("LOSE") && parts.length > 2) {
                coordsString = parts[2];
            }

            // Parse coordinates back to a usable list
            List<int[]> winningLine = parseWinningCoords(coordsString);

            // Notify UI
            final String finalResult = result; // for lambda
            final List<int[]> finalWinningLine = winningLine; // for lambda

            Platform.runLater(() -> {
                onlineGame.getOnResultListener().showResult(finalResult, finalWinningLine);
            });

        }

    }

    // Add this helper method to ClientProtocol
    private List<int[]> parseWinningCoords(String coordsData) {
        List<int[]> list = new ArrayList<>();
        if (coordsData == null || coordsData.isEmpty()) {
            return list;
        }

        String[] pairs = coordsData.split(";"); // Split into ["0,0", "0,1", "0,2"]
        for (String pair : pairs) {
            String[] xy = pair.split(","); // Split into ["0", "0"]
            if (xy.length == 2) {
                try {
                    int r = Integer.parseInt(xy[0]);
                    int c = Integer.parseInt(xy[1]);
                    list.add(new int[]{r, c});
                } catch (NumberFormatException e) {
                    // Ignore malformed data
                }
            }
        }
        return list;
    }

    private void onLoginSuccess(String[] parts, Client client) {
        String username = parts[1];
        int score = Integer.parseInt(parts[2]);
        PlayerStatus status = PlayerStatus.IDLE;

        client.setPlayer(new PlayerDTO(username, score, status));
        Router.getInstance().navigateTo("onlineMenu");
    }

    private void onRegisterSuccess() {
        Router.getInstance().navigateTo("login");
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

    private void onReceiveInvite(final String senderUserName, Client client) {
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

        Router.getInstance().navigateTo("game");
    }

    private void onInviteRejected(final String username) {
        Functions.showInformationAlert("Invitation Rejected", username + " Rejected your invitation");
        if (updatePlayerList != null) {
            Platform.runLater(() -> {
                updatePlayerList.accept(new ArrayList<>(players));
            });
        }
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

        if (updatePlayerList != null) {
            Platform.runLater(() -> {
                updatePlayerList.accept(new ArrayList<>(players));
            });
        }

    }

    private void askToSaveGame() {
        Functions.showConfirmAlert(
                "Save Match", null,
                "Would you like to save this match replay?", "Yes", "No",
                () -> {
                    onlineGame.isSaveable = true;
                    return null;
                },
                () -> {
                    onlineGame.isSaveable = false;
                    return null;
                }
        );
    }

    private void onReceiveReplayRequest(String opponentName, Client client) {
        Platform.runLater(() -> {
            Functions.showConfirmAlert(
                    "Replay Request",
                    null,
                    opponentName + " wants to play with you again. Do you accept?",
                    "Yes, Let's go!",
                    "No, Exit",
                    () -> {
                        String message = "SEND_INVITE:" + opponentName;
                        client.sendMessage(message);
                        return true;
                    },
                    () -> {
                        client.sendMessage("MOVE:QUIT_MATCH");
                        Router.getInstance().navigateTo("onlineMenu");
                        return true;
                    }
            );
        });
    }

    public List<String> getSavedGames() {
        return savedGamesList;
    }

    public Set<PlayerDTO> getPlayers() {
        return players;
    }
}

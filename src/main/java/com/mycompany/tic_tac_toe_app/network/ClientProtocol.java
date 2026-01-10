package com.mycompany.tic_tac_toe_app.network;

import com.mycompany.tic_tac_toe_app.controllers.GameController;
import com.mycompany.tic_tac_toe_app.model.PlayerDTO;
import com.mycompany.tic_tac_toe_app.model.PlayerStatus;
import com.mycompany.tic_tac_toe_app.model.service.GameMode;
import com.mycompany.tic_tac_toe_app.model.service.GameRecorder;
import com.mycompany.tic_tac_toe_app.util.Functions;
import com.mycompany.tic_tac_toe_app.model.service.online_mode.GameListener;
import com.mycompany.tic_tac_toe_app.util.PopUp;
import com.mycompany.tic_tac_toe_app.util.Router;
import java.util.ArrayList;
import java.util.Collections;
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
    private final static String GAME_START = "GAME_START";
    private final static String MOVE_VALID = "MOVE_VALID";
    private final static String GAME_OVER = "GAME_OVER";

    // Use thread-safe collections or synchronize access
    private final List<String> savedGamesList = Collections.synchronizedList(new ArrayList<>());
    private final Set<PlayerDTO> players = Collections.synchronizedSet(new HashSet<>());

    private static GameListener gameListener;
    private static ClientProtocol INSTANCE;

    public static void setGameListener(GameListener listener) {
        gameListener = listener;
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
                // Ensure symbol is set before navigation
                Client.assignedSymbol = (parts.length > 1) ? parts[1] : "X";
                Platform.runLater(() -> {
                    GameController.setGameMode(GameMode.ONLINE_MULTIPLAYER);
                    Router.getInstance().navigateTo("game");
                });
                break;

            case MOVE_VALID:
                if (gameListener != null && parts.length >= 4) {
                    int r = Integer.parseInt(parts[1]);
                    int c = Integer.parseInt(parts[2]);
                    String sym = parts[3];
                    // Delegate to the listener (OnlineGame)
                    gameListener.onOpponentMove(r, c, sym);
                }
                break;

            case GAME_OVER:
                if (gameListener != null && parts.length >= 2) {
                    String result = parts[1];
                    String steps = gameListener.getGameSteps();
                    String fileName = "online_" + System.currentTimeMillis();
                    GameRecorder.saveGame(fileName, steps);
                    gameListener.onGameResult(result);
                }
                break;

            case REGISTER_SUCCESS:
                Platform.runLater(() -> Router.getInstance().navigateTo("login"));
                break;

            case HISTORY_RESPONSE:
                onSavedGamesReceived(msg, client);
                break;

            case RECEIVE_INVITE:
                String sender = parts[1];
                Platform.runLater(() -> {
                    PopUp.showInvitation(sender, (accepted) -> {
                        if (accepted) {
                            client.sendMessage("INVITE_RESPONSE:" + sender + ":ACCEPTED");
                        } else {
                            client.sendMessage("INVITE_RESPONSE:" + sender + ":REJECTED");
                        }
                    });
                });
                break;

            case INVITE_ACCEPTED:
                // If I am the inviter, I get this message. 
                // Wait for GAME_START or navigate here if logic requires.
                // Usually GAME_START follows immediately, but safe to set mode.
                Client.assignedSymbol = (parts.length > 1) ? parts[1] : "X";
                Platform.runLater(() -> {
                    GameController.setGameMode(GameMode.ONLINE_MULTIPLAYER);
                    Router.getInstance().navigateTo("game");
                });
                break;

            case INVITE_REJECTED:
                Platform.runLater(() -> Functions.showInformationAlert("Invitation Rejected", parts[1] + " rejected your invitation."));
                break;

            case INVITE_FAIL:
                Platform.runLater(() -> Functions.showErrorAlert(new Exception(parts[1])));
                break;

            case PLAYER_LIST:
                onPlayerList(parts);
                break;

            case ERROR:
                Platform.runLater(() -> Functions.showErrorAlert(new Exception(parts[1])));
                break;
        }
    }

    private void onLoginSuccess(String[] parts, Client client) {
        String username = parts[1];
        int score = Integer.parseInt(parts[2]);
        PlayerDTO player = new PlayerDTO(username, score, PlayerStatus.IDLE);
        client.setPlayer(player);
        Platform.runLater(() -> Router.getInstance().navigateTo("onlineMenu"));
    }

    private void onSavedGamesReceived(String msg, Client client) {
        savedGamesList.clear();
        String data = msg.substring(HISTORY_RESPONSE.length());
        if (data.startsWith(":")) {
            data = data.substring(1); // Handle delimiter
        }
        if (data.trim().isEmpty()) {
            return;
        }

        String[] games = data.split(";");
        for (String gameStr : games) {
            String[] details = gameStr.split(",");
            if (details.length >= 4) {
                // Formatting safety
                String display = String.format("Match #%s: %s Vs %s (%s) %s",
                        details[0], client.getName(), details[1], details[2], details[3]);
                savedGamesList.add(display);
            }
        }
    }

    private void onPlayerList(String[] parts) {
        players.clear(); // Clear old list
        if (parts.length < 2) {
            return;
        }

        String playersData = parts[1];
        if (playersData.isEmpty()) {
            return;
        }

        String[] playersCsv = playersData.split(";");
        for (String csvValue : playersCsv) {
            String[] player = csvValue.split(",");
            if (player.length >= 2) {
                String username = player[0];
                int score = Integer.parseInt(player[1]);
                // Assuming status is available or default to IDLE
                players.add(new PlayerDTO(username, score, PlayerStatus.IDLE));
            }
        }
    }

    // Return copies or synchronized views to avoid ConcurrentModificationException in UI
    public List<String> getSavedGames() {
        return new ArrayList<>(savedGamesList);
    }

    public Set<PlayerDTO> getPlayers() {
        synchronized (players) {
            return new HashSet<>(players);
        }
    }
}

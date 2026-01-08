package com.mycompany.tic_tac_toe_app.network;

import com.mycompany.tic_tac_toe_app.util.Functions;
import com.mycompany.tic_tac_toe_app.App;
import com.mycompany.tic_tac_toe_app.model.service.online_mode.GameListener;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;

public class ClientProtocol {

    private final static String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    private final static String LOGIN_FAILED = "LOGIN_FAILED";
    private final static String HISTORY_RESPONSE = "HISTORY_RESPONSE";
    private final static String GAME_START = "GAME_START";
    private final static String MOVE_VALID = "MOVE_VALID";
    private final static String GAME_OVER = "GAME_OVER";

    public static List<String> savedGamesList = new ArrayList<>();
    private static GameListener gameListener;
    
    public static void setGameListener(GameListener listener) {
        gameListener = listener;
    }
    
    public static void reset() {
        savedGamesList.clear();
    }

    public static void processMessage(String msg, Client client) {
        if (msg == null || msg.trim().isEmpty()) {
            return;
        }

        if (msg.startsWith(HISTORY_RESPONSE)) {
            onSavedGamesReceived(msg);
            return;
        }

        String[] parts = msg.split(":");
        String messageType = parts[0];

        switch (messageType) {
            case LOGIN_SUCCESS:
                onLoginSuccess();
                break;
                
            case LOGIN_FAILED:
                onLoginFailed();
                break;
                
            case GAME_START:
                Platform.runLater(() -> Functions.naviagteTo("fxml/game"));
                break;

            case MOVE_VALID:
                if (gameListener != null && parts.length >= 4) {
                    int r = Integer.parseInt(parts[1]);
                    int c = Integer.parseInt(parts[2]);
                    String sym = parts[3];
                    Platform.runLater(() -> gameListener.onOpponentMove(r, c, sym));
                }
                break;

            case GAME_OVER:
                if (gameListener != null && parts.length >= 2) {
                    Platform.runLater(() -> gameListener.onGameResult(parts[1]));
                }
                break;
                
            case "ERROR":
                System.out.println("SERVER ERROR: " + parts[1]);
                Functions.showErrorAlert(new Exception(parts[1]));
                break;
                
            default:
                break;
        }
    }

    private static void onLoginSuccess() {

        Functions.naviagteTo("fxml/menu");
    }

    private static void onLoginFailed() {
        Functions.showErrorAlert(new Exception("Username Or Password is incorrect!"));
    }

    private static void onSavedGamesReceived(String msg) {
               
        
        savedGamesList.clear();

        String data = msg.substring(HISTORY_RESPONSE.length() + 1);

        if (data.trim().isEmpty()) {
            return;
        }

        String[] games = data.split(";");

        for (String gameStr : games) {
            String[] details = gameStr.split(",");

            if (details.length >= 4) {
                String id = details[0];
                String opponent = details[1];
                String result = details[2];
                String date = details[3];
                
                String[] dates = date.split(" ");

                String displayText = String.format("Match #%s: You Vs %s (%s) %s", id, opponent, result, dates[0]);

                savedGamesList.add(displayText);
            }
        }
    }
}

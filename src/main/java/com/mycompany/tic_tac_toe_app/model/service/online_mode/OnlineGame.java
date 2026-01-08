/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tic_tac_toe_app.model.service.online_mode;

import com.mycompany.tic_tac_toe_app.model.service.GameStrategy;
import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.network.ClientProtocol;
import com.mycompany.tic_tac_toe_app.util.Functions;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.util.Pair;

/**
 *
 * @author thaowpstasaiid
 */
public class OnlineGame implements GameStrategy, GameListener {

    private final Consumer<Pair<Integer, Integer>> updateGuiCallback;
    private final Consumer<String> showResultCallback;
    private final Button[][] boardButtons;
    private Client client;

    public OnlineGame(Consumer<Pair<Integer, Integer>> updateGuiCallback,
            Consumer<String> showResultCallback,
            Button[][] boardButtons) {
        this.updateGuiCallback = updateGuiCallback;
        this.showResultCallback = showResultCallback;
        this.boardButtons = boardButtons;

        ClientProtocol.setGameListener(this);
    }

    @Override
    public void createMove(Button btn, String id) {
        int r = id.charAt(1) - '0';
        int c = id.charAt(2) - '0';

        client.sendMessage("MOVE:" + r + ":" + c);
    }

    @Override
    public boolean checkGameStatus(int player) {
        return false;
    }

    @Override
    public void onOpponentMove(int row, int col, String symbol) {
        Button btn = boardButtons[row][col];

        if (btn != null) {
            btn.setText(symbol);
            btn.setDisable(true);
        }
    }

    @Override
    public void onGameResult(String result) {
        String videoFile = "";

        if ("OPPONENT_LEFT".equals(result)) {
            Platform.runLater(() -> {
                Functions.showErrorAlert(new Exception("Your opponent has disconnected!, The game Ended"));
                Functions.naviagteTo("fxml/menu");
            });

            ClientProtocol.setGameListener(null);
            return;
        }

        switch (result) {
            case "WIN":
                videoFile = "win.mp4";
                break;
            case "LOSE":
                videoFile = "lose.mp4";
                break;
            case "DRAW":
                videoFile = "draw.mp4";
                break;
        }

        if (!videoFile.isEmpty()) {
            showResultCallback.accept(videoFile);
        }

        ClientProtocol.setGameListener(null);
    }
}

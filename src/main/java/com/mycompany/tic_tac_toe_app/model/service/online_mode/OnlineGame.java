package com.mycompany.tic_tac_toe_app.model.service.online_mode;

import com.mycompany.tic_tac_toe_app.model.service.GameStrategy;
import com.mycompany.tic_tac_toe_app.model.service.XOGameLogic;
import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.network.ClientProtocol;
import com.mycompany.tic_tac_toe_app.util.Functions;
import com.mycompany.tic_tac_toe_app.util.Router;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.util.Pair;

public class OnlineGame implements GameStrategy, GameListener {

    private final Consumer<Pair<Integer, Integer>> updateGuiCallback;
    private final Consumer<String> showResultCallback;
    private final Button[][] boardButtons;
    private final Client client = Client.getInstance();
    private final XOGameLogic logic = new XOGameLogic();
    private final String mySymbol;
    private volatile boolean isMyTurn; // Volatile for thread visibility

    public OnlineGame(Consumer<Pair<Integer, Integer>> updateGuiCallback,
            Consumer<String> showResultCallback,
            Button[][] boardButtons) {
        this.updateGuiCallback = updateGuiCallback;
        this.showResultCallback = showResultCallback;
        this.boardButtons = boardButtons;

        this.mySymbol = Client.assignedSymbol;
        System.out.println("Game Started as: " + mySymbol);

        // X always starts
        this.isMyTurn = mySymbol.equals("X");

        ClientProtocol.setGameListener(this);
    }

    @Override
    public void createMove(Button btn, String id) {
        // Prevent moving if it's not my turn or button is taken
        if (!isMyTurn || !btn.getText().isEmpty()) {
            return;
        }

        int r = id.charAt(1) - '0';
        int c = id.charAt(2) - '0';

        // 1. Update Internal Logic
        logic.makeMove(r, c, mySymbol.equals("X") ? XOGameLogic.X : XOGameLogic.O);

        // 2. Update UI Immediately (Optimistic update)
        btn.setText(mySymbol);
        btn.setDisable(true);

        // 3. Lock turn immediately
        isMyTurn = false;

        // 4. Send to Server
        client.sendMessage("MOVE:" + r + ":" + c + ":" + mySymbol);
    }

    @Override
    public void onOpponentMove(int row, int col, String symbol) {
        Platform.runLater(() -> {
            // Check if the move received is NOT mine (it's the opponent's)
            if (!symbol.equals(mySymbol)) {

                // Update logic for opponent
                logic.makeMove(row, col, symbol.equals("X") ? XOGameLogic.X : XOGameLogic.O);

                // Update UI for opponent move
                Button btn = boardButtons[row][col];
                if (btn != null) {
                    btn.setText(symbol);
                    btn.setDisable(true);
                }

                // Since opponent played, IT IS NOW MY TURN
                isMyTurn = true;

                // Trigger any controller callbacks if needed (e.g., sound)
                updateGuiCallback.accept(new Pair<>(row, col));
            }
            // If symbol.equals(mySymbol), it's just the server confirming my move.
            // Do NOT set isMyTurn = true here.
        });
    }

    @Override
    public void onGameResult(String result) {
        Platform.runLater(() -> {
            isMyTurn = false;
            ClientProtocol.setGameListener(null); // Cleanup listener

            if ("OPPONENT_LEFT".equals(result)) {
                Functions.showErrorAlert(new Exception("Your opponent has disconnected!"));
                Router.getInstance().navigateTo("onlineMenu");
                return;
            }

            // Determine video file based on who won relative to me
            String videoFile;
            if ("draw".equalsIgnoreCase(result)) {
                videoFile = "draw.mp4";
            } else if (result.equalsIgnoreCase(mySymbol)) {
                videoFile = "win.mp4";
            } else {
                videoFile = "lose.mp4";
            }

            showResultCallback.accept(videoFile);
        });
    }

    @Override
    public boolean checkGameStatus(int player) {
        // Online game status is handled by Server messages (GAME_OVER)
        return false;
    }

    @Override
    public String getGameSteps() {
        return logic.getGameSteps();
    }
}

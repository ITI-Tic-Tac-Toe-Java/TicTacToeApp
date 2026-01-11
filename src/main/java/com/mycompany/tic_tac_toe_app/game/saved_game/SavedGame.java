package com.mycompany.tic_tac_toe_app.game.saved_game;

import com.mycompany.tic_tac_toe_app.game.util.GameListener;
import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl.OnMoveListener;
import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl.OnResultListener;
import com.mycompany.tic_tac_toe_app.game.util.OfflineGameStrategy;
import java.io.BufferedReader;
import java.io.FileReader;
import javafx.application.Platform;

public class SavedGame extends OfflineGameStrategy {

    private String replayFilePath;

    public SavedGame(GameListener gameListener, OnMoveListener onMoveListener, OnResultListener onResultListener) {
        super(gameListener, onMoveListener, onResultListener);
    }

    @Override
    public void createMove(int row, int col) {}

    public void setReplayPath(String path) {
        replayFilePath = path;
    }

    public void startReplayLogic() {
        try (BufferedReader reader = new BufferedReader(new FileReader(replayFilePath))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                String[] moves = line.split(";");

                for (String move : moves) {
                    // 1. Handle the trailing "X" or "O" (the winner flag)
                    // If the segment doesn't contain a comma, it's not a coordinate.
                    if (!move.contains(",")) {
                        break;
                    }

                    // 2. Parse "row,col,symbol" directly
                    String[] coords = move.split(",");

                    if (coords.length >= 3) {
                        try {
                            int row = Integer.parseInt(coords[0]);
                            int col = Integer.parseInt(coords[1]);
                            String symbol = coords[2];

                            Platform.runLater(() -> onMoveListener.onMove(row, col, symbol));
                            Thread.sleep(1000);
                        } catch (NumberFormatException e) {
                            System.out.println("Skipping invalid move data: " + move);
                        }
                    }
                }

                Platform.runLater(() -> {
                   onResultListener.showResult(null, null);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.mycompany.tic_tac_toe_app.game.online_mode;

import com.mycompany.tic_tac_toe_app.game.util.GameListener;
import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl;
import com.mycompany.tic_tac_toe_app.game.util.GameStrategy;
import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.util.Functions;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javafx.application.Platform;

public class OnlineGame extends GameStrategy {    

    public OnlineGame(GameListener gameListener, GameListenerImpl.OnMoveListener onMoveListener,
            GameListenerImpl.OnResultListener onResultListener) {
        super(gameListener, onMoveListener, onResultListener);
    }

    @Override
    public void createMove(int row, int col) {
        Client.getInstance().sendMessage("MOVE:" + row + ":" + col);
    }

    public GameListener getGameListener() {
        return gameListener;
    }

    public GameListenerImpl.OnMoveListener getOnMoveListener() {
        return onMoveListener;
    }

    public GameListenerImpl.OnResultListener getOnResultListener() {
        return onResultListener;
    }

    public void saveReplayToFile(String data) {
        String fileName = "replay_" + System.currentTimeMillis() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(data);
            Platform.runLater(() -> Functions.showInformationAlert("Saved", "Replay saved: " + fileName));
        } catch (java.io.IOException e) {
            Platform.runLater(() -> Functions.showErrorAlert(e));
        }
    }
}

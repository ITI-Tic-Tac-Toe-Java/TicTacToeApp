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

    public boolean isSaveable;

    // 1. Add this variable to store the steps string
    private String recordedSteps = "";

    public OnlineGame(GameListener gameListener, GameListenerImpl.OnMoveListener onMoveListener,
            GameListenerImpl.OnResultListener onResultListener) {
        super(gameListener, onMoveListener, onResultListener);
    }

    @Override
    public void createMove(int row, int col) {
        Client.getInstance().sendMessage("MOVE:" + row + ":" + col);
    }

    // 2. Add a Setter for the steps
    public void setRecordedSteps(String recordedSteps) {
        this.recordedSteps = recordedSteps;
    }

    // 3. Add a Getter (Optional, but good practice)
    public String getRecordedSteps() {
        return recordedSteps;
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

    // 4. Update this method to use the stored variable if needed
    public void saveReplayToFile(String data) {
        String fileName = "replay_" + System.currentTimeMillis() + ".txt";
        
        System.out.println("data to be saved in online game : " + data);
        // If data passed is null or empty, try using the internal variable
        String contentToWrite = (data != null && !data.isEmpty()) ? data : this.recordedSteps;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(contentToWrite);
            Platform.runLater(() -> Functions.showInformationAlert("Saved", "Replay saved: " + fileName));
        } catch (java.io.IOException e) {
            Platform.runLater(() -> Functions.showErrorAlert(e));
        }
    }
}

package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.model.service.ComputerGame;
import com.mycompany.tic_tac_toe_app.model.service.GameMode;
import com.mycompany.tic_tac_toe_app.model.service.GameStrategy;
import com.mycompany.tic_tac_toe_app.model.service.LocalGame;
import com.mycompany.tic_tac_toe_app.model.service.online_mode.OnlineGame;
import com.mycompany.tic_tac_toe_app.util.Router;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.util.Pair;

public class GameController implements Initializable {

    @FXML
    private Button _00, _01, _02, _10, _11, _12, _20, _21, _22;
    @FXML
    private VBox resultPane;
    @FXML
    private MediaView stateVideo;

    private Button[][] boardButtons;
    private static GameMode currentMode;
    private GameStrategy gameStrategy;
    private MediaPlayer mediaPlayer;

    // Static setter to pass data between controllers
    public static void setGameMode(GameMode mode) {
        currentMode = mode;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boardButtons = new Button[][]{
            {_00, _01, _02},
            {_10, _11, _12},
            {_20, _21, _22}
        };

        if (currentMode == null) {
            currentMode = GameMode.LOCAL_MULTIPLAYER;
        }

        switch (currentMode) {
            case SINGLE_PLAYER:
                gameStrategy = new ComputerGame(this::updateGuiFromComputerMove);
                break;
            case LOCAL_MULTIPLAYER:
                gameStrategy = new LocalGame(); // Ensure LocalGame accepts callbacks if needed
                break;
            case ONLINE_MULTIPLAYER:
                // Pass callbacks for UI updates and Results
                gameStrategy = new OnlineGame(
                        move -> {
                        }, // Opponent move is handled inside OnlineGame mostly, but this can play sound
                        this::showResult,
                        boardButtons
                );
                break;
        }
    }

    @FXML
    private void handleMove(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        if (gameStrategy != null) {
            gameStrategy.createMove(clickedButton, clickedButton.getId());
        }
    }

    // Callback for Single Player
    private void updateGuiFromComputerMove(Pair<Integer, Integer> move) {
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> {
            if (move != null) {
                Button btn = boardButtons[move.getKey()][move.getValue()];
                if (btn != null) {
                    btn.setText("O");
                    btn.setDisable(true);
                }
            }
        });
        delay.play();
    }

    public void showResult(String videoFile) {
        try {
            // Stop previous media if exists
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            URL mediaUrl = getClass().getResource("/com/mycompany/tic_tac_toe_app/media/" + videoFile);
            if (mediaUrl != null) {
                Media media = new Media(mediaUrl.toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                stateVideo.setMediaPlayer(mediaPlayer);
                mediaPlayer.play();
                stateVideo.setVisible(true);
                resultPane.setVisible(true);
            } else {
                System.err.println("Media file not found: " + videoFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleQuitGame(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        Router.getInstance().goBack();
    }

    public void startReplay(String steps) {
        // ... (Replay logic remains the same) ...
        // Ensure to clear board first
        for (Button[] row : boardButtons) {
            for (Button b : row) {
                b.setText("");
                b.setDisable(true);
            }
        }

        String[] moves = steps.split(";");
        Timeline timeline = new Timeline();
        int delay = 0;
        for (String m : moves) {
            if (m.trim().isEmpty()) {
                continue;
            }
            try {
                String[] parts = m.split(":");
                if (parts.length < 2) {
                    continue;
                }
                String[] data = parts[1].split(",");
                int r = Integer.parseInt(data[0]);
                int c = Integer.parseInt(data[1]);
                String sym = data[2].equals("1") ? "X" : "O";

                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(delay++), e -> {
                    boardButtons[r][c].setText(sym);
                }));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        timeline.play();
    }
}

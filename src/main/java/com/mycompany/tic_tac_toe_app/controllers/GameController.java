package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.App;
import com.mycompany.tic_tac_toe_app.model.service.GameMode;
import com.mycompany.tic_tac_toe_app.model.service.GameStrategy;
import com.mycompany.tic_tac_toe_app.model.service.computer.ComputerGame;
import com.mycompany.tic_tac_toe_app.model.service.local_multiplay.LocalGame;
import com.mycompany.tic_tac_toe_app.model.service.online_mode.OnlineGame;
import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javafx.util.Pair;

public class GameController implements Initializable {

    @FXML
    private Button _00;
    @FXML
    private Button _01;
    @FXML
    private Button _02;
    @FXML
    private Button _10;
    @FXML
    private Button _11;
    @FXML
    private Button _12;
    @FXML
    private Button _20;
    @FXML
    private Button _21;
    @FXML
    private Button _22;
    @FXML
    private VBox resultPane;
    @FXML
    private MediaView stateVideo;

    
    private static GameMode currentMode;

    private GameStrategy gameStrategy;
    
    private Button[][] boardButtons;
    
    private MediaPlayer mediaPlayer;

    public static void setGameMode(GameMode mode) {
        currentMode = mode;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boardButtons = new Button[][]{{_00, _01, _02}, {_10, _11, _12}, {_20, _21, _22}};

        if (currentMode == null) {
            currentMode = GameMode.LOCAL_MULTIPLAYER;
        }

        switch (currentMode) {
            case SINGLE_PLAYER:
                gameStrategy = new ComputerGame(this::updateGuiFromComputerMove, this::showResult);
                break;
            case LOCAL_MULTIPLAYER:

                LocalGame localGame = new LocalGame();
                localGame.showResultCallback = this::showResult; 
                gameStrategy = localGame;
                break;
                
            case ONLINE_MULTIPLAYER:
                gameStrategy = new OnlineGame(
                    this::updateGuiFromComputerMove,
                    this::showResult,
                    boardButtons
                );
                break;
                
            default:
                break;
        }
    }

    @FXML
    private void handleMove(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String id = clickedButton.getId();

        gameStrategy.createMove(clickedButton, id);

    }

    private void updateGuiFromComputerMove(Pair<Integer, Integer> move) {
        int r = move.getKey();
        int c = move.getValue();
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> {
            Button btn = boardButtons[r][c];
            if (btn != null) {
                btn.setText("O");
                btn.setDisable(true);
            }
        });
        delay.play();
    }

    @FXML
    private void goBackOnClick(ActionEvent event) throws IOException {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        App.setRoot("fxml/menu");
    }

    public void showResult(String videoFile) {

        try {

            Media media = new Media(getClass().getResource("/com/mycompany/tic_tac_toe_app/videos/" + videoFile).toExternalForm());
            
            if (mediaPlayer != null) {
                mediaPlayer.stop(); 
            }
            
            mediaPlayer = new MediaPlayer(media);

            stateVideo.setMediaPlayer(mediaPlayer);
            
            mediaPlayer.play();

            stateVideo.setVisible(true);

        } catch (Exception e) {
            System.out.println("Error loading video: " + e.getMessage());
            e.printStackTrace();
        }

        resultPane.setVisible(true);
    }
}
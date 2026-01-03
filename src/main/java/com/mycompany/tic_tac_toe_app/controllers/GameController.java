package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.App;
import com.mycompany.tic_tac_toe_app.model.service.GameMode;
import com.mycompany.tic_tac_toe_app.model.service.GameStrategy;
import com.mycompany.tic_tac_toe_app.model.service.computer.ComputerGame;
import com.mycompany.tic_tac_toe_app.model.service.local_multiplay.LocalGame;
import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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
    private ImageView resultGif;
    @FXML
    private Label resultLabel;

    private static GameMode currentMode;

    private GameStrategy gameStrategy;
    private Button[][] boardButtons;

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
                gameStrategy = new ComputerGame(this::updateGuiFromComputerMove);
                break;
            case LOCAL_MULTIPLAYER:
                gameStrategy = new LocalGame();
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

        Button btn = boardButtons[r][c];
        if (btn != null) {
            btn.setText("O");
            btn.setDisable(true);
        }
    }

    @FXML
    private void goBackOnClick(ActionEvent event) throws IOException{
        App.setRoot("fxml/menu");
    }
}

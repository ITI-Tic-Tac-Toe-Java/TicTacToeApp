package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.model.service.GameStrategy;
import com.mycompany.tic_tac_toe_app.model.service.computer.ComputerGame;
import com.mycompany.tic_tac_toe_app.model.service.local_multiplay.LocalGame;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

    private GameStrategy game;

    private GameType currentMode = GameType.COMPUTER;

    public enum GameType {
        COMPUTER, LOCAL, SERVER
    }

    public Button getButton(int r, int c) {
        if (r == 0 && c == 0) {
            return _00;
        }
        if (r == 0 && c == 1) {
            return _01;
        }
        if (r == 0 && c == 2) {
            return _02;
        }
        if (r == 1 && c == 0) {
            return _10;
        }
        if (r == 1 && c == 1) {
            return _11;
        }
        if (r == 1 && c == 2) {
            return _12;
        }
        if (r == 2 && c == 0) {
            return _20;
        }
        if (r == 2 && c == 1) {
            return _21;
        }
        if (r == 2 && c == 2) {
            return _22;
        }
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (currentMode == GameType.LOCAL) {
            game = new LocalGame();
        } else {
            game = new ComputerGame(this::handleButton);
        }

    }

    @FXML
    private void handleMove(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        game.createMove(btn, id);
//        if (currentMode == GameType.LOCAL) {
//            localGame.createMove(btn, id);
//        } else if (currentMode == GameType.COMPUTER) {
//            computerGame.createMove(btn, id);
//        }
    }

    public void handleButton(Pair<Integer, Integer> index) {
        Button btn = getButton(index.getKey(), index.getValue());

        if (btn != null) {
            btn.setText("O");
            btn.setDisable(true);
        }
    }
}

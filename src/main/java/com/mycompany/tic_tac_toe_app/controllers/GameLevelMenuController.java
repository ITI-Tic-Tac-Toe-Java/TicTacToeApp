package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.util.Router;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class GameLevelMenuController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleEasyGame(ActionEvent event) {
        GameController.setAiDepth(1);
        Router.getInstance().navigateTo("game");
    }

    @FXML
    private void handleMediumGame(ActionEvent event) {
        GameController.setAiDepth(3);
        Router.getInstance().navigateTo("game");
    }

    @FXML
    private void handleHardGame(ActionEvent event) {
        GameController.setAiDepth(9);
        Router.getInstance().navigateTo("game");
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        Router.getInstance().goBack();
    }

}

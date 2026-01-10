package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.util.Router;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.TextField;

public class SetPlayersNameController implements Initializable {

    @FXML
    private TextField playerXName;
    @FXML
    private TextField playerOName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void handleStartGame(ActionEvent event) {
        Router.getInstance().navigateTo("game");
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        Router.getInstance().goBack();
    }

}

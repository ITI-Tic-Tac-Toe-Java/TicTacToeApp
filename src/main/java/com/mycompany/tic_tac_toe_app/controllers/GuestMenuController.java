package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.model.service.GameMode;
import com.mycompany.tic_tac_toe_app.util.Router;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class GuestMenuController {

    @FXML
    private void handleComputerGame(ActionEvent event) {
        GameController.setGameMode(GameMode.SINGLE_PLAYER);
        Router.getInstance().navigateTo("game");
    }

    @FXML
    private void handleMultiplayerGame(ActionEvent event) {
        GameController.setGameMode(GameMode.LOCAL_MULTIPLAYER);
        Router.getInstance().navigateTo("game");
    }

    @FXML
    private void handleOnlineGame(ActionEvent event) {
        Router.getInstance().navigateTo("login");
    }

}

package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.game.util.GameMode;
import com.mycompany.tic_tac_toe_app.util.Router;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GuestMenuController {

    @FXML
    private void handleComputerGame(ActionEvent event) {
        GameController.setGameMode(GameMode.SINGLE_PLAYER);
        GameController.setPlayerX("YOU : X");
        GameController.setPlayerO("Computer : O");

        Router.getInstance().navigateTo("gameLevelMenu");
    }

    @FXML
    private void handleMultiplayerGame(ActionEvent event) {
        GameController.setGameMode(GameMode.LOCAL_MULTIPLAYER);
        Router.getInstance().navigateTo("setPlayersName");
    }

    @FXML
    private void handleOnlineGame(ActionEvent event) {
        Router.getInstance().navigateTo("login");
    }

}

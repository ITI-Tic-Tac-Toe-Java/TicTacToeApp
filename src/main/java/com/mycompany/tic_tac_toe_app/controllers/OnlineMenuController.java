package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.model.PlayerDTO;
import com.mycompany.tic_tac_toe_app.game.util.GameMode;
import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.util.Router;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


public class OnlineMenuController implements Initializable {

    @FXML
    private Label userNameText;
    @FXML
    private Label scoreText;
    @FXML
    private Label rankText;
    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
        PlayerDTO player = client.getPlayer();

        userNameText.setText(player.getUserName());
        scoreText.setText(String.valueOf(player.getScore()));
        rankText.setText("Bronze");
    }

    @FXML
    private void handleComputerGame(ActionEvent event) {
        GameController.setGameMode(GameMode.SINGLE_PLAYER);
        Router.getInstance().navigateTo("computerMenu");
    }

    @FXML
    private void handleMultiplayerGame(ActionEvent event) {
        GameController.setGameMode(GameMode.LOCAL_MULTIPLAYER);
        Router.getInstance().navigateTo("game");
    }

    @FXML
    private void handleViewOnlinePlayers(ActionEvent event) {
        client.sendMessage("GET_ONLINE_PLAYERS");
        Router.getInstance().navigateTo("onlinePlayers");
    }

    @FXML
    private void handleSavedGames(ActionEvent event) {
        client.sendMessage("GET_GAME_HISTORY");
        Router.getInstance().navigateTo("savedGames");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Router.getInstance().clearHistory();
        Router.getInstance().navigateTo("login");
    }

}

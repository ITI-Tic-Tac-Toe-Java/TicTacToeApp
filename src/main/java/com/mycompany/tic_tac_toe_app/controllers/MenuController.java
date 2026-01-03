package com.mycompany.tic_tac_toe_app.controllers;


import com.mycompany.tic_tac_toe_app.App;
import com.mycompany.tic_tac_toe_app.model.service.GameMode;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class MenuController implements Initializable {

    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button localMultiplayerButton;
    @FXML
    private Button onlineMultiplayerButton;
    @FXML
    private Button replayGame;
    @FXML
    private Button logoutButton;
    @FXML
    private Label UserName;
    @FXML
    private Label pointsNumber;
    @FXML
    private Label rankNumber;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void singlePlayerAction(ActionEvent event) throws IOException {
        GameController.setGameMode(GameMode.SINGLE_PLAYER);
        App.setRoot("fxml/game");
    }

    @FXML
    private void localMultiplayerAction(ActionEvent event) throws IOException {
        GameController.setGameMode(GameMode.LOCAL_MULTIPLAYER);
        App.setRoot("fxml/game");
    }

    @FXML
    private void replayGameAction(ActionEvent event) throws IOException {
        App.setRoot("fxml/savedGames");
    }

    @FXML
    private void logoutAction(ActionEvent event) throws IOException {
        App.setRoot("fxml/login");
    }

    @FXML
    private void onlineMultiplayerAction(ActionEvent event) throws IOException {
        App.setRoot("fxml/onlinePlayers");
    }
}

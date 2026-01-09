package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.App;
import com.mycompany.tic_tac_toe_app.model.PlayerDTO;
import com.mycompany.tic_tac_toe_app.game.util.GameMode;
import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.util.Functions;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

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
    private Label userName;
    @FXML
    private Label pointsNumber;
    @FXML
    private Label rankNumber;

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
        PlayerDTO player = client.getPlayer();

        userName.setText(player.getUserName());
        pointsNumber.setText(String.valueOf(player.getScore()));
        
        updateRankDisplay(player.getScore());
    }
    
    private void updateRankDisplay(int score) {        
        if (score >= 100) {
            rankNumber.setText("Gold");
            rankNumber.setTextFill(Color.web("#FFD700"));
        } else if (score >= 50) {
            rankNumber.setText("Silver");
            rankNumber.setTextFill(Color.web("#C0C0C0"));
        } else {
            rankNumber.setText("Bronze");
            rankNumber.setTextFill(Color.web("#CD7F32")); 
        }
    }

    @FXML
    private void singlePlayerAction(ActionEvent event) {
        GameController.setGameMode(GameMode.SINGLE_PLAYER);
        Functions.naviagteTo("fxml/game");
    }

    @FXML
    private void localMultiplayerAction(ActionEvent event) {
        GameController.setGameMode(GameMode.LOCAL_MULTIPLAYER);
        Functions.naviagteTo("fxml/game");
    }

    @FXML
    private void replayGameAction(ActionEvent event) {
        client.sendMessage("GET_GAME_HISTORY");
        Functions.naviagteTo("fxml/savedGames");
    }

    @FXML
    private void logoutAction(ActionEvent event) {
        Functions.naviagteTo("fxml/login");
    }

    @FXML
    private void onlineMultiplayerAction(ActionEvent event) {
        client.sendMessage("GET_ONLINE_PLAYERS");
        Functions.naviagteTo("fxml/onlinePlayers");
    }
}

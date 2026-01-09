/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class SinglePlayerLevelController implements Initializable {

    @FXML
    private AnchorPane singlePlayerLevels;
    @FXML
    private Button easyLevel;
    @FXML
    private Button mediumLevel;
    @FXML
    private Button hardLevel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onEasyClick(ActionEvent event) throws IOException{
         GameController.setGameMode(GameMode.SINGLE_PLAYER);
         GameController.setAiDepth(1); // Easy
         App.setRoot("fxml/game");
    }

    @FXML
    private void onMediumClick(ActionEvent event) throws IOException{
         GameController.setGameMode(GameMode.SINGLE_PLAYER);
         GameController.setAiDepth(3); // Medium
         App.setRoot("fxml/game");
    }

    @FXML
    private void onHardClick(ActionEvent event) throws IOException{
        GameController.setGameMode(GameMode.SINGLE_PLAYER);
        GameController.setAiDepth(9); // Hard
        App.setRoot("fxml/game");
    }
    
}

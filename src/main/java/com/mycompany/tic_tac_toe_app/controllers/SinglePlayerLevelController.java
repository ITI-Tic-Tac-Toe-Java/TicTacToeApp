package com.mycompany.tic_tac_toe_app.controllers;


import com.mycompany.tic_tac_toe_app.util.Functions;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class SinglePlayerLevelController implements Initializable {

    @FXML
    private AnchorPane singlePlayerLevels;
    @FXML
    private Button easyLevel;
    @FXML
    private Button mediumLevel;
    @FXML
    private Button hardLevel;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void onEasyClick(ActionEvent event) {
        GameController.setAiDepth(1);
        Functions.naviagteTo("fxml/game");
    }

    @FXML
    private void onMediumClick(ActionEvent event) {
        GameController.setAiDepth(3);
        Functions.naviagteTo("fxml/game");
    }

    @FXML
    private void onHardClick(ActionEvent event) {
        
        GameController.setAiDepth(9);
        Functions.naviagteTo("fxml/game");
    }

}

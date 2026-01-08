
package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;

public class InvetationController implements Initializable {


    @FXML
    private Label userName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    @FXML
    private void acceptOnAction(ActionEvent event) throws IOException {
        App.setRoot("fxml/game");
    }

    @FXML
    private void rejectOnAction(ActionEvent event) throws IOException {
        App.setRoot("fxml/onlinePlayers");
    }

}

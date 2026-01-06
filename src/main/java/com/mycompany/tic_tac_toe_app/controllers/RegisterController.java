/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.App;
import com.mycompany.tic_tac_toe_app.network.Client;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author thaowpstasaiid
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassowrd;
    @FXML
    private Button signUpBtn;
    @FXML
    private Text signIn;
    private Client client;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = new Client();
        client.start();
    }

    @FXML
    private void signUp(ActionEvent event) throws IOException{
       String userNameText = username.getText();
       String uerPasswordText = password.getText();
       String confirmPasswordText = confirmPassowrd.getText();

       if (!uerPasswordText.equals(confirmPasswordText)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Password Mismatch");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match!");
            alert.showAndWait();
            return;
             }

            StringBuilder sb = new StringBuilder();
            sb.append("REGISTER:").append(userNameText).append(":").append(uerPasswordText);
            client.sendMessage(sb.toString());

    }

    @FXML
    private void NavigateToSignIn(MouseEvent event) {
        try {
            App.setRoot("fxml/login");
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error in Loading Screen");
            alert.setHeaderText(null);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }
}

package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.util.Router;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class RegisterController implements Initializable {


    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    
    private Client client;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
    }

    @FXML
    private void signUp(ActionEvent event) throws IOException {
        String userNameText = usernameField.getText();
        String uerPasswordText = passwordField.getText();
        String confirmPasswordText = confirmPasswordField.getText();

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
    private void navigateToSignIn(MouseEvent event) {
        Router.getInstance().navigateTo("login");
    }
    
    
    @FXML
    private void handleRegister(ActionEvent event) {
        Router.getInstance().navigateTo("login");
    }
}

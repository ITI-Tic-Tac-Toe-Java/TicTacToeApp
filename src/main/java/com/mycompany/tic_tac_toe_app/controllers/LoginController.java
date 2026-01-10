package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.util.Functions;
import com.mycompany.tic_tac_toe_app.util.Router;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String userName = usernameField.getText();
        String password = passwordField.getText();

        if (userName.trim().isEmpty()) {
            Functions.showErrorAlert(new Exception("Username Field is Requried"));
            return;
        }

        if (password.trim().isEmpty()) {
            Functions.showErrorAlert(new Exception("Password Field is Requried"));
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("LOGIN:").append(userName).append(":").append(password);

        String loginMessage = sb.toString();

        client.sendMessage(loginMessage);
    }

    @FXML
    private void handleGuist(ActionEvent event) {
        Router.getInstance().navigateTo("guestMenu");
    }

    @FXML
    private void handleRegister(MouseEvent event) {
        Router.getInstance().navigateTo("register");
    }
}

/*
private Client client;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
    }

    @FXML
    private void signIn(ActionEvent event) throws IOException {
        String userName = userNameTxtField.getText();
        String password = passwordTxtField.getText();
        
        if (userName.trim().isEmpty()) {
            Functions.showErrorAlert(new Exception("Username Field is Requried"));
            return;
        }
        
        if (password.trim().isEmpty()) {
            Functions.showErrorAlert(new Exception("Password Field is Requried"));
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("LOGIN:").append(userName).append(":").append(password);

        String loginMessage = sb.toString();

        client.sendMessage(loginMessage);
    }

    @FXML
    private void navigateToRegister(MouseEvent event) throws IOException {
        Functions.naviagteTo("fxml/register");
    }
 */

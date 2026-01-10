package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.util.Router;
import com.mycompany.tic_tac_toe_app.util.Functions; // تأكد من استيراد الكلاس
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private void handleRegister(ActionEvent event) {
        String userNameText = usernameField.getText();
        String uerPasswordText = passwordField.getText();
        String confirmPasswordText = confirmPasswordField.getText();

        if (userNameText.isEmpty() || uerPasswordText.isEmpty()) {
            Functions.showInformationAlert("Missing Info", "Please fill all fields!");
            return;
        }

        if (!uerPasswordText.equals(confirmPasswordText)) {
            Functions.showInformationAlert("Password Mismatch", "Passwords do not match!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("REGISTER:").append(userNameText).append(":").append(uerPasswordText);
        client.sendMessage(sb.toString());
        Router.getInstance().navigateTo("login");
    }

    @FXML
    private void navigateToSignIn(MouseEvent event) {
        Router.getInstance().navigateTo("login");
    }

}

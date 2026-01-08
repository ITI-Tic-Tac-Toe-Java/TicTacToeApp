package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.util.Functions;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
    }

    @FXML
    private void signUp(ActionEvent event) throws IOException {
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
    private void navigateToSignIn(MouseEvent event) {
        Functions.naviagteTo("fxml/login");
    }
}

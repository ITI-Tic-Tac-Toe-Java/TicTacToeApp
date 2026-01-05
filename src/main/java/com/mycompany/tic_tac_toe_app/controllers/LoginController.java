package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.App;
import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.util.Functions;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class LoginController implements Initializable {

    @FXML
    private TextField userNameTxtField;
    @FXML
    private TextField passwordTxtField;
    @FXML
    private Button signInBtn;
    @FXML
    private Text registerLink;

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client =  Client.getInstance();
    }

    @FXML
    private void signIn(ActionEvent event) throws IOException {
        String userName = userNameTxtField.getText();
        String password = passwordTxtField.getText();

        StringBuilder sb = new StringBuilder();
        sb.append("LOGIN:").append(userName).append(":").append(password);

        String loginMessage = sb.toString();

        client.sendMessage(loginMessage);
    }

    @FXML
    private void navigateToRegister(MouseEvent event) throws IOException {
       Functions.naviagteTo("fxml/register");
    }
}

package com.mycompany.tic_tac_toe_app.controllers;

import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InvitationController {

    @FXML
    private Label UserName;
    @FXML
    private Label textLabel;

    private boolean accepted = false;
    private Consumer<Boolean> onResponse;

    public void setData(String playerName, Consumer<Boolean> onResponse) {
        this.UserName.setText(playerName);
        this.onResponse = onResponse;
    }

    public void setData(String playerName, String textLabel, Consumer<Boolean> onResponse) {
        this.UserName.setText(playerName);
        this.textLabel.setText(textLabel);
        this.onResponse = onResponse;
    }

    @FXML
    private void handleAccept() {
        if (onResponse != null) {
            onResponse.accept(true);
        }
    }

    @FXML
    private void handleCancel() {
        if (onResponse != null) {
            onResponse.accept(false);
        }
    }
}

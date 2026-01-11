package com.mycompany.tic_tac_toe_app.controllers;

import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class InvitationController {

    @FXML
    private Label UserName;
    @FXML
    private Label textLabel;
    @FXML
    private Button acceptButton;
    @FXML
    private Button cancelButton;

    private Consumer<Boolean> onResponse;

    public void setData(String title, String message, String acceptText, String cancelText, boolean showCancel, Consumer<Boolean> onResponse) {
        this.UserName.setText(title);
        this.textLabel.setText(message);
        this.acceptButton.setText(acceptText != null ? acceptText : "OK");
        this.onResponse = onResponse;

        if (showCancel) {
            this.cancelButton.setVisible(true);
            this.cancelButton.setManaged(true);
            this.cancelButton.setText(cancelText != null ? cancelText : "CANCEL");
        } else {
            this.cancelButton.setVisible(false);
            this.cancelButton.setManaged(false);
        }
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

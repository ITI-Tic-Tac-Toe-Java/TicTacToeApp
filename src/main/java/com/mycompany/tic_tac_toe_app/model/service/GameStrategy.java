package com.mycompany.tic_tac_toe_app.model.service;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public interface GameStrategy {
    void createMove(Button btn, String id);
    boolean checkGameStatus(int player);
    
    default void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

package com.mycompany.tic_tac_toe_app.util;

import com.mycompany.tic_tac_toe_app.App;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 *
 * @author DELL
 */
public class Functions {

    public static void showErrorAlert(Exception ex) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Someting Went Wrong");
            alert.setHeaderText(null);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        });
    }

    public static void naviagteTo(String fxml){
        Platform.runLater(() -> {
            try {
                App.setRoot(fxml);
            } catch (IOException ex) {
                showErrorAlert(ex);
            }
        });
        
    }
}

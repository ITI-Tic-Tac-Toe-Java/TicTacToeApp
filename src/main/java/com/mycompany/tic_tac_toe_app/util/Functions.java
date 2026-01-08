package com.mycompany.tic_tac_toe_app.util;

import com.mycompany.tic_tac_toe_app.App;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Functions {

    public static void showErrorAlert(final Exception ex) {
        Platform.runLater(() -> {
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Someting Went Wrong");
            alert.setHeaderText(null);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        });
    }

    public static void showConfirmAlert(
            final String title,
            final String header,
            final String content,
            final String ok,
            final String cancel,
            final Supplier onOk,
            final Supplier onCancel
    ) {
        Platform.runLater(() -> {
            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            final ButtonType OK = new ButtonType(ok);
            final ButtonType CANCEL = new ButtonType(cancel);
            alert.getButtonTypes().setAll(OK, CANCEL);
            
            final Optional<ButtonType> op = alert.showAndWait();
            if (op.isPresent()) {
                if (op.get() == OK) {
                    onOk.get();
                } else {
                    onCancel.get();
                }
            }
        });
    }

    public static void showInformationAlert(
            final String title,
            final String content
    ) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void naviagteTo(final String fxml) {
        Platform.runLater(() -> {
            try {
                App.setRoot(fxml);
            } catch (IOException ex) {
                showErrorAlert(ex);
            }
        });
    }
}

package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.model.service.GameRecorder;
import com.mycompany.tic_tac_toe_app.network.ClientProtocol;
import com.mycompany.tic_tac_toe_app.util.Router;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;

public class SavedGamesController implements Initializable {

    @FXML
    private ListView<String> listView;
    private List<String> savedGames;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ClientProtocol cp = ClientProtocol.getInstance();
        savedGames = GameRecorder.getSavedFiles();

        listView.getItems().clear();

        if (savedGames == null || savedGames.isEmpty()) {
            listView.getItems().add("No Saved Games");
            listView.setDisable(true);
        } else {
            listView.setDisable(false);
            listView.getItems().addAll(savedGames);
        }

        listView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    getStyleClass().add("list-cell");
                }
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals("No Saved Games")) {
                String steps = GameRecorder.loadGameSteps(newVal);

                Router.getInstance().navigateTo("game");

                Platform.runLater(() -> {
                    GameController controller = (GameController) Router.getInstance().getCurrentController();
                    controller.startReplay(steps);
                });
            }
        });

    }

    @FXML
    private void handleBackToMenu() {
        Router.getInstance().goBack();
    }
}

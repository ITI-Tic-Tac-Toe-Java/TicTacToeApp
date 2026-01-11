package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.game.util.GameMode;
import com.mycompany.tic_tac_toe_app.util.Router;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class SavedGamesController implements Initializable {

    @FXML
    private ListView<String> listView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadSavedFiles();

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals("No Saved Games To Show")) {
                GameController.setReplayPath(newVal);
                GameController.setGameMode(GameMode.SAVED_GAME);
                Router.getInstance().navigateTo("game");
            }
        });
    }

    private void loadSavedFiles() {
        listView.getItems().clear();
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.startsWith("replay_") && name.endsWith(".txt"));

        if (listOfFiles == null || listOfFiles.length == 0) {
            listView.getItems().add("No Saved Games To Show");
            listView.setDisable(true);
        } else {
            listView.setDisable(false);
            for (File file : listOfFiles) {
                listView.getItems().add(file.getName());
            }
        }
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        Router.getInstance().navigateTo("onlineMenu");
    }
}

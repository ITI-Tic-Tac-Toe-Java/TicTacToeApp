/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.App;
import com.mycompany.tic_tac_toe_app.network.ClientProtocol;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author thaowpstasaiid
 */
public class SavedGamesController implements Initializable {

    @FXML
    private ListView<String> listView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        listView.getItems().clear();
        
        if (ClientProtocol.savedGamesList.isEmpty()) {
            listView.getItems().add("No Saved Games To Show");
            listView.setDisable(true);
        } else {
            listView.setDisable(false);
            listView.getItems().addAll(ClientProtocol.savedGamesList);
        }

        listView.prefHeightProperty().bind(Bindings.size(listView.getItems()).multiply(listView.getFixedCellSize()).add(2));

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    App.setRoot("fxml/game");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void GoToSavedMatch(MouseEvent event) {

    }

    @FXML
    private void backToMenu(ActionEvent event) {
        try {
            App.setRoot("fxml/menu");
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error in Loading Screen");
            alert.setHeaderText(null);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }
}

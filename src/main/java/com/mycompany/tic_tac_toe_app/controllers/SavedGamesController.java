/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tic_tac_toe_app.controllers;


import com.mycompany.tic_tac_toe_app.network.ClientProtocol;
import com.mycompany.tic_tac_toe_app.util.Functions;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author thaowpstasaiid
 */
public class SavedGamesController implements Initializable {

    @FXML
    private ListView<String> listView;
    private List<String> savedGames;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ClientProtocol cp = ClientProtocol.getInstance();
        
        savedGames = cp.getSavedGames();

        listView.getItems().clear();

        if (savedGames.isEmpty()) {
            listView.getItems().add("No Saved Games To Show");
            listView.setDisable(true);
        } else {
            listView.getItems().addAll(savedGames);
        }

        listView.prefHeightProperty().bind(Bindings.size(listView.getItems()).multiply(listView.getFixedCellSize()).add(2));

        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            Functions.naviagteTo("fxml/game");
        });
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tic_tac_toe_app.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.beans.binding.Bindings;

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
        // Your existing code to add items
        listView.getItems().addAll(
                "Game 1",
                "Game 2",
                "Game 3",
                "Game 4",
                "Row 5"
        );

        listView.setFixedCellSize(40);

        listView.prefHeightProperty().bind(
                Bindings.size(listView.getItems())
                        .multiply(listView.getFixedCellSize())
                        .add(2)
        );
    }
}
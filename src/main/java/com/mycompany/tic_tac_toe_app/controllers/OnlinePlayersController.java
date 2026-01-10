package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.model.PlayerDTO;
import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.network.ClientProtocol;
import com.mycompany.tic_tac_toe_app.util.Router;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import javafx.scene.shape.Circle;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;

public class OnlinePlayersController implements Initializable {

    @FXML
    private ListView<HBox> playersListView;
    @FXML
    private TextField searchField;

    private Set<PlayerDTO> onlinePlayers;
    private Client client;
    private ClientProtocol cp;
    private Consumer<List<PlayerDTO>> updateList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
        cp = ClientProtocol.getInstance();
        onlinePlayers = cp.getPlayers();

        updateList = (List<PlayerDTO> players) -> {
            Platform.runLater(() -> updateListView(new ArrayList<>(players)));
        };

        cp.setOnNewPlayerListener(updateList);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterList(newValue);
        });

        updateListView(new ArrayList<>(onlinePlayers));
    }

    private void filterList(String searchText) {
        List<PlayerDTO> filteredList = new ArrayList<>();
        for (PlayerDTO p : onlinePlayers) {
            if (p.getUserName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(p);
            }
        }
        updateListView(filteredList);
    }

    private void updateListView(List<PlayerDTO> players) {
        playersListView.getItems().clear();
        for (PlayerDTO p : players) {
            if (!p.getUserName().equals(client.getPlayer().getUserName())) {
                addPlayerToList(p.getUserName(), p.getStatus().name());
            }
        }
    }

    private void addPlayerToList(String name, String status) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Circle statusDot = new Circle(5);
        statusDot.getStyleClass().add("status-circle");

        if (status.equalsIgnoreCase("Playing")) {
            statusDot.getStyleClass().add("status-ingame");
        } else {
            statusDot.getStyleClass().add("status-online"); 
        }

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("medium-text"); 

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button inviteBtn = new Button("INVITE");

        if (status.equalsIgnoreCase("Playing")) {
            inviteBtn.setDisable(true);
            inviteBtn.setOpacity(0.5);
        }

        inviteBtn.setOnAction(e -> {
            inviteBtn.setText("SENT");
            inviteBtn.setDisable(true);
            client.sendMessage("SEND_INVITE:" + name);
        });

        row.getChildren().addAll(statusDot, nameLabel, spacer, inviteBtn);
        playersListView.getItems().add(row);
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        Router.getInstance().navigateTo("onlineMenu");
    }
}
/*
package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.model.PlayerDTO;
import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.network.ClientProtocol;
import com.mycompany.tic_tac_toe_app.util.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.scene.layout.Priority;

public class OnlinePlayersControllerTemp implements Initializable {

    @FXML
    private ListView<HBox> playersListView;
    @FXML
    private Label noPlayersLabel;
    @FXML
    private TextField searchBar;

    private Set<PlayerDTO> onlinePlayers;
    private Client client;
    private ClientProtocol cp;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
        cp = ClientProtocol.getInstance();
        onlinePlayers = cp.getPlayers();
        updateListView(new ArrayList<>(onlinePlayers));
    }

    @FXML
    private void onSearch(ActionEvent event) {
        String searchText = searchBar.getText();
        filterList(searchText);
    }

    private void filterList(String searchText) {
        List<PlayerDTO> filteredList = new ArrayList<>();
        for (PlayerDTO p : onlinePlayers) {
            if (p.getUserName().contains(searchText)) {
                filteredList.add(p);
            }
        }
        updateListView(filteredList);
    }

    private void updateListView(List<PlayerDTO> players) {
        playersListView.getItems().clear();
        if (players.isEmpty()) {
            playersListView.setVisible(false);
            noPlayersLabel.setVisible(true);
        } else {
            playersListView.setVisible(true);
            noPlayersLabel.setVisible(false);
            for (PlayerDTO p : players) {
                addPlayerToList(p.getUserName(), p.getStatus().name());
            }
        }
    }

    private void addPlayerToList(String name, String status) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox details = new VBox(2);
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label statusLabel = new Label(status);
        if (status.equals("Playing")) {
            statusLabel.setStyle("-fx-text-fill: #FF827E; -fx-font-size: 12px;");
        } else if (status.equals("Online")) {
            statusLabel.setStyle("-fx-text-fill: #00C096; -fx-font-size: 12px;");
        }

        details.getChildren().addAll(nameLabel, statusLabel);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        row.getChildren().addAll(details, spacer);

        Button inviteBtn = new Button("Invite");

        if (status.equals("Playing")) {
            inviteBtn.setStyle("-fx-background-color: #ADADAD; -fx-text-fill: white; -fx-background-radius: 15;");
        } else {
            inviteBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 15;");
        }

        inviteBtn.setOnAction(e -> {
            if (inviteBtn.getText().equals("Invite")) {
                inviteBtn.setText("Sent");
                inviteBtn.setDisable(true);
                client.sendMessage("SEND_INVITE:" + name);
            }
        });

        if (!(name.equals(Client.getInstance().getPlayer().getUserName()))) {
            row.getChildren().add(inviteBtn);
        }
        
        playersListView.getItems().add(row);
    }

    @FXML
    private void goBackOnClick(ActionEvent event) {
        Functions.naviagteTo("fxml/menu");
    }
}
*/
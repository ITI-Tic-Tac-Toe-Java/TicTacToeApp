package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.model.Player;
import com.mycompany.tic_tac_toe_app.model.PlayerDTO;
import com.mycompany.tic_tac_toe_app.model.PlayerStatus;
import com.mycompany.tic_tac_toe_app.util.PopUp;
import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.network.ClientProtocol;
import com.mycompany.tic_tac_toe_app.util.Router;
import java.net.URL;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

public class OnlinePlayersController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Player> playersListView;

    private final ObservableList<Player> players = FXCollections.observableArrayList();

    private Client client;
    private ClientProtocol cp;
    private Set<PlayerDTO> onlinePlayers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = Client.getInstance();
        cp = ClientProtocol.getInstance();
        onlinePlayers = cp.getPlayers();

        refreshPlayers();
        setupListViewCell();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterList(newVal));
    }

    private void refreshPlayers() {
        loadPlayers(new ArrayList<>(onlinePlayers));
    }

    private void loadPlayers(List<PlayerDTO> serverPlayers) {
        players.clear();
        String myName = client.getPlayer().getUserName();

        for (PlayerDTO p : serverPlayers) {
            if (!p.getUserName().equals(myName)) {
                players.add(new Player(
                        p.getUserName(),
                        PlayerStatus.valueOf(p.getStatus().name())
                ));
            }
        }
    }

    private void filterList(String text) {
        String query = text.toLowerCase();
        List<PlayerDTO> filtered = new ArrayList<>();

        for (PlayerDTO p : onlinePlayers) {
            if (p.getUserName().toLowerCase().contains(query)) {
                filtered.add(p);
            }
        }

        loadPlayers(filtered);
    }

    private void setupListViewCell() {
        playersListView.setItems(players);

        playersListView.setCellFactory(lv -> new ListCell<>() {

            private final HBox container = new HBox(10);
            private final Circle statusCircle = new Circle(6);
            private final Label nameLabel = new Label();

            {
                container.setAlignment(Pos.CENTER_LEFT);
                statusCircle.getStyleClass().add("status-circle");
                container.getChildren().addAll(statusCircle, nameLabel);
            }

            @Override
            protected void updateItem(Player player, boolean empty) {
                super.updateItem(player, empty);

                if (empty || player == null) {
                    setGraphic(null);
                    return;
                }

                nameLabel.setText(player.getName());

                statusCircle.getStyleClass().removeAll(
                        "status-online",
                        "status-ingame",
                        "status-offline"
                );

                switch (player.getStatus()) {
                    case IDLE:
                        statusCircle.getStyleClass().add("status-online");
                        break;

                    case INGAME:
                        statusCircle.getStyleClass().add("status-ingame");
                        break;

                    case OFFLINE:
                        statusCircle.getStyleClass().add("status-offline");
                        break;
                }

                setGraphic(container);
            }
        });

        playersListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> handleSelection(newVal));
    }

    private void handleSelection(Player player) {
        if (player == null) {
            return;
        }

        if (player.getStatus() == PlayerStatus.IDLE) {
            PopUp.confermInvitation(player.getName(),", are you ready to send the invitation?", isAccepted -> {
                if (isAccepted) {
                    client.sendMessage("SEND_INVITE:" + player.getName());
                }
            });
        } else {
            showBusyAlert();
        }

        playersListView.getSelectionModel().clearSelection();
    }

    private void showBusyAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("This player is currently busy.");
        alert.showAndWait();
    }

    @FXML
    private void handleBackToMenu() {
        Router.getInstance().goBack();
    }
}

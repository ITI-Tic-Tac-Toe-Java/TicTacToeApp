package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.App;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class OnlinePlayersController implements Initializable {

    @FXML
    private Pane onlinePlayersPane;
    @FXML
    private HBox onlineAppBar;
    @FXML
    private TextField searchBar;

    @FXML
    private ListView<HBox> playersListView;

    private List<Player> allPlayers = new ArrayList<>();
    @FXML
    private Label noPlayersLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        allPlayers.add(new Player("Ahmed", "Online", "Invite"));
        allPlayers.add(new Player("Sara", "Playing", "Invite"));
        allPlayers.add(new Player("Mona", "Online", "Accept"));
        allPlayers.add(new Player("Ali", "Online", "Accept"));
        allPlayers.add(new Player("Mariam", "Online", "Decline"));
        allPlayers.add(new Player("Ahmed", "Online", "Invite"));
        allPlayers.add(new Player("Sara", "Playing", "Invite"));
        allPlayers.add(new Player("Mona", "Online", "Invite"));
        allPlayers.add(new Player("Ali", "Online", "Accept"));
        allPlayers.add(new Player("Ahmed", "Online", "Invite"));
        allPlayers.add(new Player("Sara", "Playing", "Invite"));
        allPlayers.add(new Player("Mona", "Online", "Invite"));
        allPlayers.add(new Player("Ali", "Online", "Decline"));
        allPlayers.add(new Player("Ahmed", "Online", "Invite"));
        allPlayers.add(new Player("Sara", "Playing", "Invite"));
        allPlayers.add(new Player("Mona", "Online", "Accept"));
        allPlayers.add(new Player("Ali", "Online", "Accept"));
        allPlayers.add(new Player("Mariam", "Online", "Decline"));
        allPlayers.add(new Player("Ahmed", "Online", "Invite"));
        allPlayers.add(new Player("Sara", "Playing", "Invite"));
        allPlayers.add(new Player("Mona", "Online", "Invite"));
        allPlayers.add(new Player("Ali", "Online", "Accept"));
        allPlayers.add(new Player("Ahmed", "Online", "Invite"));
        allPlayers.add(new Player("Sara", "Playing", "Invite"));
        allPlayers.add(new Player("Mona", "Online", "Invite"));
        allPlayers.add(new Player("Ali", "Online", "Decline"));

        updateListView(allPlayers);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {filterList(newValue);});

        playersListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                return;
            }
            String actionType = (String) newVal.getUserData();
            System.out.println(actionType);
            if ("Invite".equals(actionType)) {
                try {
                    App.setRoot("fxml/invetation");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void filterList(String searchText) {
        List<Player> filteredList = new ArrayList<>();
        for (Player p : allPlayers) {
            if (p.name.contains(searchText)) {
                filteredList.add(p);
            }
        }
        updateListView(filteredList);
    }

    private void updateListView(List<Player> players) {
        playersListView.getItems().clear();
        if (players.isEmpty()) {
            playersListView.setVisible(false);
            noPlayersLabel.setVisible(true);
        } else {
            playersListView.setVisible(true);
            noPlayersLabel.setVisible(false);

            for (Player p : players) {
                addPlayerToList(p.name, p.status, p.actionType);
            }
        }
    }

    private void addPlayerToList(String name, String status, String type) {
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

        if (type.equals("Invite")) {
            Button inviteBtn = new Button("Invite");
            if (status.equals("Playing")) {
                inviteBtn.setStyle("-fx-background-color: #ADADAD; -fx-text-fill: white; -fx-background-radius: 15;");
            } else {
                inviteBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 15;");
            }
            inviteBtn.setOnAction(e -> System.out.println("Inviting " + name));
            row.getChildren().add(inviteBtn);
        } else if (type.equals("Accept")) {
            Button acceptBtn = new Button("Accept");
            acceptBtn.setStyle("-fx-background-color: #00C096; -fx-text-fill: white; -fx-background-radius: 15;");
            row.getChildren().add(acceptBtn);

        } else if (type.equals("Decline")) {
            Button declineBtn = new Button("Decline");
            declineBtn.setStyle("-fx-background-color: #FF827E; -fx-text-fill: white; -fx-background-radius: 15;");
            row.getChildren().add(declineBtn);

        }
         row.setUserData(type);

        playersListView.getItems().add(row);
    }

    @FXML
    private void onSearch(ActionEvent event) {
    }

    @FXML
    private void goBackOnClick(ActionEvent event) throws IOException {
        App.setRoot("fxml/menu");
    }

}

//TODO -> convert it to POJO
class Player {

    String name;
    String status;
    String actionType;

    public Player(String name, String status, String actionType) {
        this.name = name;
        this.status = status;
        this.actionType = actionType;
    }
}

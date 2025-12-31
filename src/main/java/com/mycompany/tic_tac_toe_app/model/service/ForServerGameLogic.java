package com.mycompany.tic_tac_toe_app.model.service;

public class ForServerGameLogic {

}

/*


package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.model.service.XOGameLogic;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class GameController implements Initializable, Runnable {

    @FXML
    private Button _00;
    @FXML
    private Button _01;
    @FXML
    private Button _22;
    @FXML
    private Button _21;
    @FXML
    private Button _12;
    @FXML
    private Button _11;
    @FXML
    private Button _20;
    @FXML
    private Button _10;
    @FXML
    private Button _02;

    Socket socket;
    BufferedReader br;
    PrintStream ps;
    Thread th;

    XOGameLogic game = new XOGameLogic();
    LocalGame localGame = new LocalGame();

    boolean myTurn = false;
    String mySymbol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            socket = new Socket("127.0.0.1", 5008);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ps = new PrintStream(socket.getOutputStream());

            th = new Thread(this);
            th.start();

        } catch (IOException ex) {
            showAlert("Server", "Could't connect to server");
        }
    }

    @Override
    public void run() {
        try {
            mySymbol = br.readLine();
            myTurn = mySymbol.equals("X");

            while (true) {
                String msg = br.readLine();
                if (msg == null) {
                    break;
                }

                String[] data = msg.split(",");
                int r = Integer.parseInt(data[0]);
                int c = Integer.parseInt(data[1]);
                String sym = data[2];

                Platform.runLater(() -> {
                    applyMove(r, c, sym);
                    myTurn = true;
                });
            }
        } catch (IOException e) {
            Platform.runLater(() -> showAlert("Connection", "Server disconnected")
            );
        }
    }

    private Button getButton(int r, int c) {
        if (r == 0 && c == 0) {
            return _00;
        }
        if (r == 0 && c == 1) {
            return _01;
        }
        if (r == 0 && c == 2) {
            return _02;
        }
        if (r == 1 && c == 0) {
            return _10;
        }
        if (r == 1 && c == 1) {
            return _11;
        }
        if (r == 1 && c == 2) {
            return _12;
        }
        if (r == 2 && c == 0) {
            return _20;
        }
        if (r == 2 && c == 1) {
            return _21;
        }
        return _22;
    }

    private void applyMove(int r, int c, String sym) {
        Button btn = getButton(r, c);
        btn.setText(sym);
        btn.setDisable(true);

        int symbol = game.getSymbol(sym);
        game.makeMove(r, c, symbol);

        if (game.checkWin(symbol)) {
            showAlert("Win", sym + " is the winner");
        } else if (game.isDraw()) {
            showAlert("Draw", "Game ended with a draw");
        }
    }

    @FXML
    private void handleMove(ActionEvent event) {
        if (!myTurn) {
            return;
        }

        Button btn = (Button) event.getSource();
        String id = btn.getId();

        int r = id.charAt(1) - '0';
        int c = id.charAt(2) - '0';

        applyMove(r, c, mySymbol);

        ps.println(r + "," + c + "," + mySymbol);

        myTurn = false;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

*/

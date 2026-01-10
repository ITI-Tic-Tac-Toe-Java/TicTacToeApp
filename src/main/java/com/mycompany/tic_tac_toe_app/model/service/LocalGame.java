package com.mycompany.tic_tac_toe_app.model.service;
import javafx.scene.control.Button;

public class LocalGame implements GameStrategy{

    boolean isX = true;
    XOGameLogic game = new XOGameLogic();

    public void createMove(Button btn, String id) {
        int r = id.charAt(1) - '0';
        int c = id.charAt(2) - '0';

        if (isX) {
            game.makeMove(r, c, 1);
            btn.setText("X");
        } else {
            game.makeMove(r, c, 2);
            btn.setText("O");
        }

        btn.setDisable(true);
        checkGameStatus(1);

        isX = !isX;
    }

    public boolean checkGameStatus(int player) {
        if (game.checkWin(player)) {
            showAlert("Win", (player == 1 ? "Player X" : "Player O") + " is the winner");
            return true;
        }
        if (game.isDraw()) {
            showAlert("Draw", "There is no winner");
            return true;
        }
        return false;
    }
}

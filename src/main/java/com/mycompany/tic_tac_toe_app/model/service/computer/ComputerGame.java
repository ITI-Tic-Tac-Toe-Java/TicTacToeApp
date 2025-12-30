package com.mycompany.tic_tac_toe_app.model.service.computer;

import com.mycompany.tic_tac_toe_app.controllers.GameController;
import com.mycompany.tic_tac_toe_app.model.service.GameStrategy;
import com.mycompany.tic_tac_toe_app.model.service.XOGameLogic;
import java.util.function.Consumer;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.util.Pair;

public class ComputerGame implements GameStrategy {

    XOGameLogic game = new XOGameLogic();
//    GameController controller;
    Consumer<Pair<Integer, Integer>> handleButton;

    public ComputerGame(Consumer<Pair<Integer, Integer>> handleButton) {
        this.handleButton = handleButton;
        // this.controller = controller;
    }

    public void createMove(Button btn, String id) {
        int r = id.charAt(1) - '0';
        int c = id.charAt(2) - '0';

        game.makeMove(r, c, 1);
        btn.setText("X");
        btn.setDisable(true);

        if (checkGameStatus(1)) {
            return;
        }
        makeRandomMove();
    }

    void makeRandomMove() {
        String curMove = game.getEmptyPos();
        if (!curMove.equals("")) {
            String[] rc = curMove.split(",");
            int r = Integer.parseInt(rc[0]);
            int c = Integer.parseInt(rc[1]);

            game.makeMove(r, c, 2);

            handleButton.accept(new Pair(r, c));

//            Button btn = controller.getButton(r, c);
//
//            if (btn != null) {
//                btn.setText("O");
//                btn.setDisable(true);
//            }
            checkGameStatus(2);
        }
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

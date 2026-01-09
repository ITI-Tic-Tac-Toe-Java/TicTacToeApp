package com.mycompany.tic_tac_toe_app.model.service.computer;

import com.mycompany.tic_tac_toe_app.controllers.GameController;
import com.mycompany.tic_tac_toe_app.model.service.GameStrategy;
import com.mycompany.tic_tac_toe_app.model.service.XOGameLogic;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;
import javafx.util.Pair;

public class ComputerGame implements GameStrategy {

    XOGameLogic game = new XOGameLogic();
     private int depth;
    //    GameController controller;
    Consumer<String> showResultCallback;
    Consumer<Pair<Integer, Integer>> handleButton;

    public ComputerGame(int depth ,Consumer<Pair<Integer, Integer>> handleButton, Consumer<String> showResultCallback) {
        this.depth = depth;
        this.handleButton = handleButton;
        this.showResultCallback = showResultCallback;
        // this.controller = controller;
    }

    @Override
    public void createMove(Button btn, String id) {
        int r = id.charAt(1) - '0';
        int c = id.charAt(2) - '0';

        game.makeMove(r, c, 1);
        btn.setText("X");
        btn.setDisable(true);

        if (checkGameStatus(1)) {
            return;
        }
         makeComputerMove();
    }

//    void makeRandomMove() {
//
//        String curMove = game.getEmptyPos();
//        if (!curMove.equals("")) {
//            String[] rc = curMove.split(",");
//            int r = Integer.parseInt(rc[0]);
//            int c = Integer.parseInt(rc[1]);
//
//            game.makeMove(r, c, 2);
//            handleButton.accept(new Pair(r, c));
//
////            Button btn = controller.getButton(r, c);
////
////            if (btn != null) {
////                btn.setText("O");
////                btn.setDisable(true);
////            }
//            checkGameStatus(2);
//        }
//
//    }
    
    private void makeComputerMove() {

    Pair<Integer, Integer> bestMove;

    if (depth == 1) {
        bestMove = randomMove();        // Easy
    } else {
        bestMove = minimaxMove();       // Medium / Hard
    }

    if (bestMove != null) {
        int r = bestMove.getKey();
        int c = bestMove.getValue();

        game.makeMove(r, c, 2);
        handleButton.accept(bestMove);
        checkGameStatus(2);
    }
}
    private Pair<Integer, Integer> randomMove() {
    String pos = game.getEmptyPos();
    if (pos.isEmpty()) return null;

    String[] rc = pos.split(",");
    return new Pair<>(
            Integer.parseInt(rc[0]),
            Integer.parseInt(rc[1])
    );
}
private Pair<Integer, Integer> minimaxMove() {
    int bestScore = Integer.MIN_VALUE;
    Pair<Integer, Integer> bestMove = null;

    for (Pair<Integer, Integer> move : game.getAvailableMoves()) {
        game.makeMove(move.getKey(), move.getValue(), 2);
        int score = minimax(depth - 1, false);
        game.undoMove(move.getKey(), move.getValue());

        if (score > bestScore) {
            bestScore = score;
            bestMove = move;
        }
    }
    return bestMove;
}

private int minimax(int depth, boolean isMax) {

    if (game.checkWin(2)) return 10;
    if (game.checkWin(1)) return -10;
    if (game.isDraw() || depth == 0) return 0;

    int best = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    for (Pair<Integer, Integer> move : game.getAvailableMoves()) {
        game.makeMove(move.getKey(), move.getValue(), isMax ? 2 : 1);
        int score = minimax(depth - 1, !isMax);
        game.undoMove(move.getKey(), move.getValue());

        best = isMax ? Math.max(best, score) : Math.min(best, score);
    }
    return best;
}
    @Override
    public boolean checkGameStatus(int player) {
        if (game.checkWin(player)) {
            showResultCallback.accept(
//                    player == 1 ? "You Win 🎉" : "You Lose 😢",
                    player == 1 ? "win.mp4" : "lose.mp4"
            );
            return true;
        }

        if (game.isDraw()) {
            showResultCallback.accept("draw.mp4");
            return true;
        }
        return false;
    }
}
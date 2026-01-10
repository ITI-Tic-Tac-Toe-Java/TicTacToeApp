package com.mycompany.tic_tac_toe_app.game.computer;

import static com.mycompany.tic_tac_toe_app.game.XOGameLogic.Symbol.O;
import static com.mycompany.tic_tac_toe_app.game.XOGameLogic.Symbol.X;
import com.mycompany.tic_tac_toe_app.game.util.GameListener;
import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl;
import com.mycompany.tic_tac_toe_app.game.util.OfflineGameStrategy;
import javafx.util.Pair;

public class ComputerGame extends OfflineGameStrategy {

    private int depth;

    public ComputerGame(
            GameListener gameListener,
            GameListenerImpl.OnMoveListener onMoveListener,
            GameListenerImpl.OnResultListener onResultListener,
            int depth
    ) {
        super(gameListener, onMoveListener, onResultListener);
        this.depth = depth;
    }

    @Override
    public void createMove(int row, int col) {
        game.makeMove(row, col, X);
        gameListener.onPlayerMove(row, col, "X", onMoveListener);
        if (checkGameStatus(X)) {
            return;
        }
        makeComputerMove();
    }

    @Override
    public boolean checkGameStatus(int player) {
        if (game.checkWin(player)) {
            gameListener.onGameOver(player == X ? "WIN" : "LOSE", onResultListener);
            return true;
        }
        if (game.isDraw()) {
            gameListener.onGameOver("DRAW", onResultListener);
            return true;
        }
        return false;
    }

    void makeComputerMove() {
        Pair<Integer, Integer> bestMove;

        if (depth == 1) {
            bestMove = randomMove();        // Easy
        } else {
            bestMove = minimaxMove();       // Medium / Hard
        }

        if (bestMove != null) {
            int r = bestMove.getKey();
            int c = bestMove.getValue();

            game.makeMove(r, c, O);
            onMoveListener.onMove(r, c, "O");
            checkGameStatus(O);
        }
    }

    private Pair<Integer, Integer> randomMove() {
        int[] positions = game.getRandomEmptyPosition();
        if (positions[0] == -1 || positions[1] == -1) {
            return null;
        }

        return new Pair(positions[0], positions[1]);
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

        if (game.checkWin(O)) {
            return 10;
        }
        if (game.checkWin(X)) {
            return -10;
        }
        if (game.isDraw() || depth == 0) {
            return 0;
        }

        int best = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Pair<Integer, Integer> move : game.getAvailableMoves()) {
            game.makeMove(move.getKey(), move.getValue(), isMax ? 2 : 1);
            int score = minimax(depth - 1, !isMax);
            game.undoMove(move.getKey(), move.getValue());

            best = isMax ? Math.max(best, score) : Math.min(best, score);
        }
        return best;
    }

}

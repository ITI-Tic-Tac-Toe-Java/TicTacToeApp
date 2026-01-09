package com.mycompany.tic_tac_toe_app.game.computer;

import static com.mycompany.tic_tac_toe_app.game.XOGameLogic.Symbol.O;
import static com.mycompany.tic_tac_toe_app.game.XOGameLogic.Symbol.X;
import com.mycompany.tic_tac_toe_app.game.util.GameListener;
import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl;
import com.mycompany.tic_tac_toe_app.game.util.OfflineGameStrategy;

public class ComputerGame extends OfflineGameStrategy {

    public ComputerGame(GameListener gameListener, GameListenerImpl.OnMoveListener onMoveListener, GameListenerImpl.OnResultListener onResultListener) {
        super(gameListener, onMoveListener, onResultListener);
    }

    @Override
    public void createMove(int row, int col) {
        game.makeMove(row, col, X);
        gameListener.onPlayerMove(row, col, "X", onMoveListener);
        if (checkGameStatus(X)) {
            return;
        }
        makeRandomMove();
    }

    @Override
    public boolean checkGameStatus(int player) {
        if (game.hasPlayerWon(player)) {
            gameListener.onGameOver(player == X ? "win.mp4" : "draw.mp4", onResultListener);
            return true;
        }

        if (game.isDraw()) {
            gameListener.onGameOver("draw.mp4", onResultListener);
            return true;
        }
        return false;
    }

    void makeRandomMove() {
        int[] emptyPos = game.getEmptyPos();
        int row = emptyPos[0];
        int col = emptyPos[1];

        if (row >= 0 && col >= 0) {
            game.makeMove(row, col, O);
            gameListener.onPlayerMove(row, col, "O", onMoveListener);
            checkGameStatus(O);
        }
    }

}

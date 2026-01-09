package com.mycompany.tic_tac_toe_app.game.local_multiplay;


import static com.mycompany.tic_tac_toe_app.game.XOGameLogic.Symbol.O;
import static com.mycompany.tic_tac_toe_app.game.XOGameLogic.Symbol.X;
import com.mycompany.tic_tac_toe_app.game.util.GameListener;
import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl.OnMoveListener;
import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl.OnResultListener;
import com.mycompany.tic_tac_toe_app.game.util.OfflineGameStrategy;


public class LocalGame extends OfflineGameStrategy{
    boolean isX = true;

    public LocalGame(GameListener gameListener, OnMoveListener onMoveListener, OnResultListener onResultListener) {
        super(gameListener, onMoveListener, onResultListener);
    }
   
    public void createMove(int row, int col) {
        if (isX) {
            game.makeMove(row, col, X);
            gameListener.onPlayerMove(row, col, "X", onMoveListener);
        } else {
            game.makeMove(row, col, O);
            gameListener.onPlayerMove(row, col, "O", onMoveListener);
        }
        isX = !isX;
        checkGameStatus(1);
        checkGameStatus(2);
    }

    public boolean checkGameStatus(int player) {
        boolean played = false;

        if (game.hasPlayerWon(player)) {
            gameListener.onGameOver("win.mp4", onResultListener);
            played = true;
        }

        if (game.isDraw()) {
            gameListener.onGameOver("draw.mp4", onResultListener);
            played = true;
        }

        return played;
    }

  
}

package com.mycompany.tic_tac_toe_app.game.util;

import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl.OnResultListener;
import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl.OnMoveListener;

public interface GameListener {
    void onPlayerMove(int row, int col, String symbol, OnMoveListener onMove);

    void onGameOver(String result, OnResultListener resultListener);
}

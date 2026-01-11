package com.mycompany.tic_tac_toe_app.game.util;

import java.util.List;


public class GameListenerImpl implements GameListener {
    
    public void onPlayerMove(int row, int col, String symbol, OnMoveListener moveListener) {
        moveListener.onMove(row, col, symbol);
    }

    @Override
    public void onGameOver(String result, OnResultListener resultListener) {
        resultListener.showResult(result, null);
    }

    public interface OnMoveListener {
        void onMove(int row, int col, String symbol);
    }

    public interface OnResultListener {
        void showResult(String result, List<int[]> onlineWinningCoords);
    }
}

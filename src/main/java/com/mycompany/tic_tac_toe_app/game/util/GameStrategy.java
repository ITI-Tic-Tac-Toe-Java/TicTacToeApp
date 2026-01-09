package com.mycompany.tic_tac_toe_app.game.util;

import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl.OnMoveListener;
import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl.OnResultListener;

public abstract class GameStrategy {

    protected GameListener gameListener;
    protected OnMoveListener onMoveListener;
    protected GameListenerImpl.OnResultListener onResultListener;

    public GameStrategy(
            GameListener gameListener,
            OnMoveListener onMoveListener,
            OnResultListener onResultListener
    ) {
        this.onMoveListener = onMoveListener;
        this.onResultListener = onResultListener;
        this.gameListener = gameListener;
    }

    public abstract void createMove(int row, int col);

}

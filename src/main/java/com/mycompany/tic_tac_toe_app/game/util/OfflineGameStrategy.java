package com.mycompany.tic_tac_toe_app.game.util;

import com.mycompany.tic_tac_toe_app.game.XOGameLogic;
import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl.OnMoveListener;
import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl.OnResultListener;

public abstract class OfflineGameStrategy extends GameStrategy {

    protected XOGameLogic game;

    public OfflineGameStrategy(
            GameListener gameListener,
            OnMoveListener onMoveListener,
            OnResultListener onResultListener
    ) {
        super(gameListener, onMoveListener, onResultListener);
        game = new XOGameLogic();
    }

    public XOGameLogic getGameLogic() {
        return game;
    }

    public boolean checkGameStatus(int player) {
        return false;
    }

}

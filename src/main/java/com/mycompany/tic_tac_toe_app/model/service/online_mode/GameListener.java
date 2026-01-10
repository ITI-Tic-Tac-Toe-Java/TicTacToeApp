package com.mycompany.tic_tac_toe_app.model.service.online_mode;

public interface GameListener {

    void onOpponentMove(int row, int col, String symbol);

    void onGameResult(String result);

    String getGameSteps();
}

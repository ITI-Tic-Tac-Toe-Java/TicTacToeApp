package com.mycompany.tic_tac_toe_app.game.util;


import com.mycompany.tic_tac_toe_app.network.ClientProtocol;
import com.mycompany.tic_tac_toe_app.util.Functions;

public class GameListenerImpl implements GameListener{
    GameMode currentMode;
    
    public GameListenerImpl(GameMode gameMode){
        currentMode = gameMode;
    }
    
    public void onPlayerMove(int row, int col, String symbol, OnMoveListener moveListener) {
        moveListener.onMove(row, col, symbol);
    }

    @Override
    public void onGameOver(String result, OnResultListener resultListener) {
        if (currentMode == GameMode.ONLINE_MULTIPLAYER) {
            String videoFile = "";

            if ("OPPONENT_LEFT".equals(result)) {
                Functions.showErrorAlert(new Exception("Your opponent has disconnected!, The game Ended"));
                Functions.naviagteTo("fxml/menu");
                ClientProtocol.getInstance().setOnlineGame(null);
                return;
            }

            switch (result) {
                case "WIN":
                    videoFile = "win.mp4";
                    break;
                case "LOSE":
                    videoFile = "lose.mp4";
                    break;
                case "DRAW":
                    videoFile = "draw.mp4";
                    break;
            }

            if (!videoFile.isEmpty()) {
                resultListener.showResult(videoFile);
            }

            ClientProtocol.getInstance().setOnlineGame(null);
        } else {
            resultListener.showResult(result);
        }
    }
    
    public interface OnMoveListener{
        void onMove(int row, int col, String symbol);
    }
    
    public interface OnResultListener{
        void showResult(String result);
    }
}

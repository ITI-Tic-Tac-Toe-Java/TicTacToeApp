package com.mycompany.tic_tac_toe_app.model.service.local_multiplay;

import com.mycompany.tic_tac_toe_app.model.service.GameStrategy;
import com.mycompany.tic_tac_toe_app.model.service.XOGameLogic;
import com.mycompany.tic_tac_toe_app.network.Client;
import java.util.function.Consumer;
import javafx.scene.control.Button;

public class LocalGame implements GameStrategy{

    boolean isX = true;
    XOGameLogic game = new XOGameLogic();
    public Consumer<String> showResultCallback;
    private Client client;
    
    public void createMove(Button btn, String id) {
        int r = id.charAt(1) - '0';
        int c = id.charAt(2) - '0';

        if (isX) {
            game.makeMove(r, c, 1);
            btn.setText("X");
        } else {
            game.makeMove(r, c, 2);
            btn.setText("O");
        }

        btn.setDisable(true);
        checkGameStatus(1);
        checkGameStatus(2);

        isX = !isX;
    }

    public boolean checkGameStatus(int player) {
        if (game.checkWin(player)) {
            showResultCallback.accept("win.mp4");
            return true;
        }
        if (game.isDraw()) {
            showResultCallback.accept("draw.mp4");
            return true;
        }
        return false;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tic_tac_toe_app.model.service.online_mode;

import com.mycompany.tic_tac_toe_app.model.service.GameStrategy;
import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.network.ClientProtocol;
import javafx.scene.control.Button;

/**
 *
 * @author thaowpstasaiid
 */
public class OnlineGame implements GameStrategy{

    private final Client client;

    public OnlineGame(GameListener gameListener) {
        client = Client.getInstance();
        
        ClientProtocol.setGameListener(gameListener);
    }

    @Override
    public void createMove(Button btn, String id) {
        int r = id.charAt(1) - '0';
        int c = id.charAt(2) - '0';

        client.sendMessage("MOVE:" + r + ":" + c);
    }

    @Override
    public boolean checkGameStatus(int player) {
        return false;
    }

}

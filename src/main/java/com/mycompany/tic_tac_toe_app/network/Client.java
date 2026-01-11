package com.mycompany.tic_tac_toe_app.network;

import com.mycompany.tic_tac_toe_app.model.PlayerDTO;
import com.mycompany.tic_tac_toe_app.util.Functions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import javafx.application.Platform;

public class Client extends Thread {

    private PlayerDTO player;
    private final int port = 5008;
    private final String localHost = "10.191.242.48";
    private PrintStream ps;
    private BufferedReader br;
    private Socket socket;
    private ClientProtocol cp;
    private static Client INSTANCE;

    private Client() {
        cp = ClientProtocol.getInstance();
        try {
            socket = new Socket(localHost, port);
            this.ps = new PrintStream(socket.getOutputStream());
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            Functions.showErrorAlert(new IOException("Error In Connecting Client"));
        }
    }

    public synchronized static Client getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Client();
            INSTANCE.start();
        }
        return INSTANCE;
    }

    @Override
    public void run() {
        receiveMessage();
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    } 
    
    public PlayerDTO getPlayer() {
        return player;
    }

    private void receiveMessage() {
        String msg;
        try {
            while ((msg = br.readLine()) != null) {
                cp.processMessage(msg, this);
            }
        } catch (IOException ex) {
            Functions.showErrorAlert(new IOException("Error In Receving Message"));
        }
    }

    public void sendMessage(String msg) {
        if (ps != null) {
            ps.println(msg);
            ps.flush();
        }
    }
}
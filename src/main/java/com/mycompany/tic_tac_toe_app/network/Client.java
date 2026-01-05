package com.mycompany.tic_tac_toe_app.network;

import com.mycompany.tic_tac_toe_app.util.Functions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import javafx.application.Platform;

public class Client extends Thread {

    private final int port = 5008;
    private final String localHost = "localhost";
    private PrintStream ps;
    private BufferedReader br;
    private Socket socket;

    private static Client INSTANCE;

    private Client() {
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

    private void receiveMessage() {
        String msg;
        try {
            while ((msg = br.readLine()) != null) {
                ClientProtocol.processMessage(msg, this);
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

    private void closeResources() {
        try {
            ps.flush();
            ps.close();
            br.close();
            socket.close();
        } catch (IOException ex) {
            Platform.runLater(() -> Functions.showErrorAlert(new IOException("Error in closing resources")));
        }
    }
}

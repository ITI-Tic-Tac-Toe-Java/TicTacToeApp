package com.mycompany.tic_tac_toe_app.network;

import com.mycompany.tic_tac_toe_app.util.Functions;


public class ClientProtocol {
    private final static String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    private final static String LOGIN_FAILED = "LOGIN_FAILED";

    public static void processMessage(String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            return;
        }

        String[] parts = msg.split(":");

        String messageType = parts[0];

        switch (messageType) {
            case LOGIN_SUCCESS:
                onLoginSuccess();
                break;

            case LOGIN_FAILED:
                onLoginFailed();
                break;
            default:
                break;
        }
    }

    private static void onLoginSuccess() {
        Functions.naviagteTo("fxml/menu");
    }

    private static void onLoginFailed() {
        Functions.showErrorAlert(new Exception("Username Or Password is incorrect!"));
    }
}

package com.mycompany.tic_tac_toe_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.text.Font;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Font.loadFont(getClass().getResourceAsStream("/com/mycompany/tic_tac_toe_app/fonts/Minecraft.ttf"), 10);
        Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/tic_tac_toe_app/fxml/mainlayout.fxml"));

        Scene scene = new Scene(root, 1280, 720);

        stage.setTitle("Tic Tac Toe Pixel");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}

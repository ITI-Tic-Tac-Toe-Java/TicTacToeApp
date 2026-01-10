package com.mycompany.tic_tac_toe_app;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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

    public static void main(String[] args) {
        launch();
    }
}

package com.mycompany.tic_tac_toe_app.util;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Stack;
import javafx.application.Platform;

public class Router {

    private Object currentController;

    private static Router instance;
    private StackPane rootPane;
    private StackPane viewContainer;
    private static final String FXML_PATH = "/com/mycompany/tic_tac_toe_app/fxml/";

    private final Stack<String> history = new Stack<>();

    private Router() {
    }

    public static Router getInstance() {
        if(instance == null){
            instance = new Router();
        }
        
        return instance;
    }

    public void init(StackPane viewContainer, StackPane rootPane) {
        this.viewContainer = viewContainer;
        this.rootPane = rootPane;
    }

    public StackPane getRootPane() {
        return rootPane;
    }

    public StackPane getViewContainer() {
        return viewContainer;
    }

    public void navigateTo(String fxmlName) {
        Platform.runLater(() -> {
            if (viewContainer == null) {
                return;
            }
            history.push(fxmlName);
            loadScreen(fxmlName);
        });
    }

    public void goBack() {
        Platform.runLater(() -> {
            if (history.size() > 1) {
                history.pop();
                loadScreen(history.peek());
            }
        });
    }

    public Object getCurrentController() {
        return currentController;
    }

    private void loadScreen(String fxmlName) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH + fxmlName + ".fxml"));
                Parent node = loader.load();

                currentController = loader.getController();

                viewContainer.getChildren().setAll(node);

                node.setOpacity(0);
                FadeTransition ft = new FadeTransition(Duration.seconds(0.5), node);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void clearHistory() {
        history.clear();
    }
}

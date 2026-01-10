package com.mycompany.tic_tac_toe_app.util;

import com.mycompany.tic_tac_toe_app.controllers.InvitationController;
import java.util.function.Consumer;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class PopUp {

    private static void display(String title, String message, String acceptText, String cancelText, boolean showCancel, Consumer<Boolean> onResponse) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(PopUp.class.getResource("/com/mycompany/tic_tac_toe_app/fxml/invitation.fxml"));
                Parent popupRoot = loader.load();
                InvitationController controller = loader.getController();

                StackPane fullRoot = Router.getInstance().getRootPane();
                if (fullRoot == null) {
                    return;
                }

                Pane overlay = new Pane();
                overlay.setStyle("-fx-background-color: rgba(0,0,0,0.7);");

                StackPane popupWrapper = new StackPane(popupRoot);
                popupWrapper.setAlignment(Pos.CENTER);

                controller.setData(title, message, acceptText, cancelText, showCancel, accepted -> {
                    fullRoot.getChildren().removeAll(overlay, popupWrapper);
                    if (onResponse != null) {
                        onResponse.accept(accepted);
                    }
                });

                fullRoot.getChildren().addAll(overlay, popupWrapper);

                FadeTransition ft = new FadeTransition(Duration.millis(200), popupWrapper);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void showCustom(String title, String message, String accept, String cancel, boolean hasCancel, Consumer<Boolean> callback) {
        display(title, message, accept, cancel, hasCancel, callback);
    }
}

//package com.mycompany.tic_tac_toe_app.util;
//
//import com.mycompany.tic_tac_toe_app.controllers.InvitationController;
//import java.util.function.Consumer;
//import javafx.animation.FadeTransition;
//import javafx.application.Platform;
//import javafx.fxml.FXMLLoader;
//import javafx.geometry.Pos;
//import javafx.scene.Parent;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.StackPane;
//import javafx.util.Duration;
//
//public class PopUp {
//
//    private static void display(Parent popupRoot,
//            InvitationController controller,
//            String playerName,
//            String text,
//            Consumer<Boolean> onResponse
//    ) {
//        StackPane fullRoot = Router.getInstance().getRootPane();
//
//        if (fullRoot == null) {
//            System.err.println("RootPane is not initialized in Router!");
//            return;
//        }
//
//        Pane overlay = new Pane();
//        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.7);");
//        overlay.prefWidthProperty().bind(fullRoot.widthProperty());
//        overlay.prefHeightProperty().bind(fullRoot.heightProperty());
//
//        StackPane popupWrapper = new StackPane(popupRoot);
//        popupWrapper.setAlignment(Pos.CENTER);
//        popupWrapper.setPickOnBounds(false);
//
//        FadeTransition ft = new FadeTransition(Duration.millis(200), popupWrapper);
//        ft.setFromValue(0);
//        ft.setToValue(1);
//        ft.play();
//
//        if (text == null) {
//            controller.setData(playerName, accepted -> {
//                fullRoot.getChildren().removeAll(overlay, popupWrapper);
//                if (onResponse != null) {
//                    onResponse.accept(accepted);
//                }
//            });
//        } else {
//            controller.setData(playerName, text, accepted -> {
//                fullRoot.getChildren().removeAll(overlay, popupWrapper);
//                if (onResponse != null) {
//                    onResponse.accept(accepted);
//                }
//            });
//        }
//
//        fullRoot.getChildren().addAll(overlay, popupWrapper);
//    }
//
//    public static void showInvitation(String playerName, Consumer<Boolean> onResponse) {
//        Platform.runLater(() -> {
//            try {
//                FXMLLoader loader = new FXMLLoader(PopUp.class.getResource("/com/mycompany/tic_tac_toe_app/fxml/invitation.fxml"));
//                Parent root = loader.load();
//                display(root, loader.getController(), playerName, null, onResponse);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    public static void confermInvitation(String playerName, String text, Consumer<Boolean> onResponse) {
//        Platform.runLater(() -> {
//            try {
//                FXMLLoader loader = new FXMLLoader(PopUp.class.getResource("/com/mycompany/tic_tac_toe_app/fxml/invitation.fxml"));
//                Parent root = loader.load();
//                display(root, loader.getController(), playerName, text, onResponse);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    public static void showCustomAlert(String title, String message, String btnText, Runnable action) {
//        Platform.runLater(() -> {
//            try {
//                FXMLLoader loader = new FXMLLoader(PopUp.class.getResource("/com/mycompany/tic_tac_toe_app/fxml/invitation.fxml"));
//                Parent root = loader.load();
//                InvitationController controller = loader.getController();
//
//                display(root, controller, title, message, accepted -> {
//                    if (accepted && action != null) {
//                        action.run();
//                    }
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    public static void win() {
//        Platform.runLater(() -> {
//            /* ... */ });
//    }
//
//    public static void lose() {
//        Platform.runLater(() -> {
//            /* ... */ });
//    }
//
//    public static void draw() {
//        Platform.runLater(() -> {
//            /* ... */ });
//    }
//}

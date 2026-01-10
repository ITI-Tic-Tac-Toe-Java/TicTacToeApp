package com.mycompany.tic_tac_toe_app;

import com.mycompany.tic_tac_toe_app.util.Router;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class MainController implements Initializable {

    @FXML
    private MediaView mediaView;

    @FXML
    private StackPane contentPane;

    private StackPane viewContainer;
    @FXML
    private StackPane mainRoot;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupBackgroundVideo();

        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> playSplashAnimation());
        delay.play();
    }

    private void setupBackgroundVideo() {
        try {
            Media media = new Media(getClass().getResource("/com/mycompany/tic_tac_toe_app/videos/background.mp4").toExternalForm());
            MediaPlayer player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.play();
            mediaView.setMediaPlayer(player);

            mediaView.fitWidthProperty().bind(contentPane.widthProperty());
            mediaView.fitHeightProperty().bind(contentPane.heightProperty());

        } catch (Exception e) {
            System.out.println("Error loading video: " + e.getMessage());
        }
    }

    private void playSplashAnimation() {
        ImageView boardImage = new ImageView(new Image(getClass()
                .getResource("/com/mycompany/tic_tac_toe_app/images/board.png")
                .toExternalForm())
        );
        boardImage.setPreserveRatio(true);
        boardImage.setFitWidth(500);

        viewContainer = new StackPane();
        viewContainer.setAlignment(Pos.CENTER);
        viewContainer.setMaxSize(350, 400);

        contentPane.getChildren().addAll(boardImage, viewContainer);

        Router.getInstance().init(viewContainer, contentPane);

        ScaleTransition st = new ScaleTransition(Duration.seconds(1), boardImage);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1);
        st.setToY(1);

        st.setOnFinished(e -> Router.getInstance().navigateTo("login"));

        st.play();
    }

}

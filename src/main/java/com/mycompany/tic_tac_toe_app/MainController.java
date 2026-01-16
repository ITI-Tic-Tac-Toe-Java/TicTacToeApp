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
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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

    private MediaPlayer player;

    private void setupBackgroundVideo() {
        try {
            // 1) Load from resources as a stream
            InputStream is = getClass().getResourceAsStream(
                    "/com/mycompany/tic_tac_toe_app/videos/background.mp4"
            );
            if (is == null) {
                throw new RuntimeException("background.mp4 not found in resources");
            }

            // 2) Copy to a unique temp file for THIS instance
            Path tempVideo = Files.createTempFile("ttt_bg_", ".mp4");
            Files.copy(is, tempVideo, StandardCopyOption.REPLACE_EXISTING);
            tempVideo.toFile().deleteOnExit();

            // 3) Play from temp file (each instance has its own file)
            Media media = new Media(tempVideo.toUri().toString());
            player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.play();

            mediaView.setMediaPlayer(player);
            mediaView.fitWidthProperty().bind(contentPane.widthProperty());
            mediaView.fitHeightProperty().bind(contentPane.heightProperty());

        } catch (Exception e) {
            e.printStackTrace();
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

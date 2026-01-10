package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl;
import com.mycompany.tic_tac_toe_app.game.util.GameMode;
import com.mycompany.tic_tac_toe_app.game.util.GameStrategy;
import com.mycompany.tic_tac_toe_app.game.computer.ComputerGame;
import com.mycompany.tic_tac_toe_app.game.local_multiplay.LocalGame;
import com.mycompany.tic_tac_toe_app.game.util.GameListener;
import com.mycompany.tic_tac_toe_app.game.online_mode.OnlineGame;
import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.network.ClientProtocol;
import com.mycompany.tic_tac_toe_app.util.Functions;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class GameController implements Initializable {

    @FXML
    private Button _00;
    @FXML
    private Button _01;
    @FXML
    private Button _02;
    @FXML
    private Button _10;
    @FXML
    private Button _11;
    @FXML
    private Button _12;
    @FXML
    private Button _20;
    @FXML
    private Button _21;
    @FXML
    private Button _22;
    @FXML
    private VBox resultPane;
    @FXML
    private MediaView stateVideo;

    private static GameMode currentMode;

    private GameStrategy gameStrategy;

    private Button[][] boardButtons;

    private MediaPlayer mediaPlayer;

    private GameListener gameListener;

    private static int aiDepth = 3;
    @FXML
    private Button goBackBtn;
    @FXML
    private StackPane stackPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boardButtons = new Button[][]{{_00, _01, _02}, {_10, _11, _12}, {_20, _21, _22}};

        if (currentMode == null) {
            currentMode = GameMode.LOCAL_MULTIPLAYER;
        }

        gameListener = new GameListenerImpl(currentMode);

        switch (currentMode) {
            case SINGLE_PLAYER:
                gameStrategy = new ComputerGame(
                        gameListener,
                        this::onMove,
                        this::showResult,
                        aiDepth
                );

                break;
            case LOCAL_MULTIPLAYER:
                gameStrategy = new LocalGame(
                        gameListener,
                        this::onMove,
                        this::showResult
                );
                break;

            case ONLINE_MULTIPLAYER:
                gameStrategy = new OnlineGame(
                        gameListener,
                        this::onMove,
                        this::showResult
                );
                ClientProtocol.getInstance().setOnlineGame((OnlineGame) gameStrategy);
                break;

            default:
                break;
        }
    }

    public static void setGameMode(GameMode gameMode) {
        currentMode = gameMode;
    }

    public static void setAiDepth(int depth) {
        aiDepth = depth;
    }

    @FXML
    private void handlePlayerMove(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String id = clickedButton.getId();
        int r = id.charAt(1) - '0';
        int c = id.charAt(2) - '0';
        gameStrategy.createMove(r, c);
    }

    public void onMove(int row, int col, String symbol) {
        Platform.runLater(() -> {
            Button btn = boardButtons[row][col];
            if (btn != null) {
                btn.setText(symbol);
                btn.setDisable(true);
            }
        });
    }

    public void showResult(String videoFile) {
        try {
            URL videoUrl = getClass().getResource("/com/mycompany/tic_tac_toe_app/videos/" + videoFile);

            if (videoUrl == null) {
                System.out.println("Video file not found: " + videoFile);
                return;
            }

            System.out.println("Video URL: " + videoUrl.toExternalForm());

            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                mediaPlayer = null;
            }

            Media media = new Media(videoUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(media);

            Platform.runLater(() -> {
                stateVideo.setMediaPlayer(mediaPlayer);
                for (Button[] row : boardButtons) {
                    for (Button btn : row) {
                        btn.setDisable(true);
                    }
                }
            });

            mediaPlayer.setOnReady(() -> {
                Platform.runLater(() -> {
                    goBackBtn.setVisible(false);
                    stateVideo.setVisible(true);
                    mediaPlayer.play();
                    System.out.println("Playing video: " + videoFile);
                });
            });

            mediaPlayer.setOnError(() -> {
                System.out.println("Error with MediaPlayer: " + mediaPlayer.getError().getMessage());
                mediaPlayer.getError().printStackTrace();  // More detailed error
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                mediaPlayer = null;

                Platform.runLater(() -> {
                    for (Button[] row : boardButtons) {
                        for (Button btn : row) {
                            btn.setDisable(false);
                        }
                    }
                    Functions.naviagteTo("fxml/menu");
                });
            });

            mediaPlayer.getMedia().getSource();

            resultPane.setVisible(true);
        } catch (Exception e) {
            System.out.println("Error loading video: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goBackOnClick(ActionEvent event) throws IOException {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        Client.getInstance().sendMessage("GAME_DISCONNECT");
        Functions.naviagteTo("fxml/menu");
    }
}

package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl;
import com.mycompany.tic_tac_toe_app.game.util.GameMode;
import com.mycompany.tic_tac_toe_app.game.util.GameStrategy;
import com.mycompany.tic_tac_toe_app.game.computer.ComputerGame;
import com.mycompany.tic_tac_toe_app.game.local_multiplay.LocalGame;
import com.mycompany.tic_tac_toe_app.game.util.GameListener;
import com.mycompany.tic_tac_toe_app.game.online_mode.OnlineGame;
import com.mycompany.tic_tac_toe_app.game.saved_game.SavedGame;
import com.mycompany.tic_tac_toe_app.game.util.OfflineGameStrategy;
import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.network.ClientProtocol;
import com.mycompany.tic_tac_toe_app.util.Functions;
import com.mycompany.tic_tac_toe_app.util.Router;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.util.Pair;

public class GameController implements Initializable {

    @FXML
    private Label playerName;
    @FXML
    private Button _00, _01, _02, _10, _11, _12, _20, _21, _22;

    private static String playerXName;
    private static String playerOName;
    private Button[][] boardButtons;
    private GameStrategy gameStrategy;
    private GameListener gameListener;
    private MediaPlayer mediaPlayer;
    private static GameMode currentMode;
    private static int aiDepth = 3;

    private static String replayFilePath = "";

    public static void setReplayPath(String path) {
        replayFilePath = path;
    }

    public static void setPlayerX(String name) {
        playerXName = name;
    }
    
    public static void setPlayerO(String name) {
        playerOName = name;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playerName.setText(playerXName + " : X");
        boardButtons = new Button[][]{{_00, _01, _02}, {_10, _11, _12}, {_20, _21, _22}};
        setupNormalGame();
    }

    private void setupNormalGame() {
        if (currentMode == null) {
            currentMode = GameMode.LOCAL_MULTIPLAYER;
        }
        gameListener = new GameListenerImpl();

        switch (currentMode) {
            case SINGLE_PLAYER:
                gameStrategy = new ComputerGame(gameListener, this::onMove, this::showResult, aiDepth);
                break;
            case LOCAL_MULTIPLAYER:
                gameStrategy = new LocalGame(gameListener, this::onMove, this::showResult);
                break;
            case ONLINE_MULTIPLAYER:
                gameStrategy = new OnlineGame(gameListener, this::onMove, this::showResult);
                ClientProtocol.getInstance().setOnlineGame((OnlineGame) gameStrategy);
                break;

            case SAVED_GAME:
                prepareBoardForReplay();
                gameStrategy = new SavedGame(gameListener, this::updateBoardFromReplay, (res, list) -> {
                    Functions.showInformationAlert("Replay Finished", "The game replay has ended.");
                    Router.getInstance().goBack();
                });

                ((SavedGame) gameStrategy).setReplayPath(replayFilePath);
                new Thread(
                        () -> {
                            ((SavedGame) gameStrategy).startReplayLogic();
                        }
                ).start();
        }
    }

    private void prepareBoardForReplay() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardButtons[i][j].setDisable(true);
                boardButtons[i][j].setText("");
                boardButtons[i][j].setStyle("");
                boardButtons[i][j].setOpacity(1.0);
            }
        }
    }

    @FXML
    private void handleMove(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String id = clickedButton.getId();
        int r = id.charAt(1) - '0';
        int c = id.charAt(2) - '0';
        if (gameStrategy != null) {
            gameStrategy.createMove(r, c);
        }
    }

    public void updateBoardFromReplay(int row, int col, String symbol) {
        Button btn = boardButtons[row][col];
        if (btn != null) {
            btn.setText(symbol);
            btn.setStyle("-fx-text-fill: #fbff00; -fx-font-weight: bold; -fx-opacity: 1.0;");
        }
    }

    public void onMove(int row, int col, String symbol) {
        Platform.runLater(() -> {
            Button btn = boardButtons[row][col];
            if (btn != null) {
                btn.setText(symbol);
                btn.setDisable(true);
                if(symbol.equals("X")) playerName.setText(playerOName + " : O");
                else playerName.setText(playerXName + " : X");
            }
        });
    }

    public void showResult(String result, List<int[]> onlineWinningCoords) {
        Platform.runLater(() -> {
            String videoFile = "";
            String colorStyle = "";
            switch (result) {
                case "WIN":
                    videoFile = "win.mp4";
                    colorStyle = "-fx-background-color: #39e639; -fx-text-fill: white;";
                    break;
                case "LOSE":
                    videoFile = "lose.mp4";
                    colorStyle = "-fx-background-color: #ff3333; -fx-text-fill: white;";
                    break;
                case "DRAW":
                    videoFile = "draw.mp4";
                    break;
                case "OPPONENT_LEFT":
                    Functions.showErrorAlert(new Exception("Your opponent has disconnected!, The game Ended"));
                    Router.getInstance().navigateTo("onlineMenu");
                    ClientProtocol.getInstance().setOnlineGame(null);
                    return;
            }

            final String finalVideo = videoFile;
            PauseTransition pause = new PauseTransition(Duration.millis(600));
            pause.setOnFinished(e -> {
                if (!finalVideo.isEmpty()) {
                    playVideoOverlay(finalVideo);
                }
            });
            pause.play();

            if (currentMode == GameMode.ONLINE_MULTIPLAYER) {
                for (int[] coord : onlineWinningCoords) {
                    boardButtons[coord[0]][coord[1]].setStyle(colorStyle + " -fx-opacity: 0.8;");
                }
            } else {
                var winningCoords = ((OfflineGameStrategy) gameStrategy).getGameLogic().getWinningCoords();
                for (Pair<Integer, Integer> coord : winningCoords) {
                    boardButtons[coord.getKey()][coord.getValue()].setStyle(colorStyle + " -fx-opacity: 0.8;");
                }
            }

        });
    }

    private void playVideoOverlay(String videoFile) {
        try {
            URL videoUrl = getClass().getResource("/com/mycompany/tic_tac_toe_app/videos/" + videoFile);
            if (videoUrl == null) {
                return;
            }

            Media media = new Media(videoUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            MediaView dynamicVideoView = new MediaView(mediaPlayer);
            StackPane videoOverlay = new StackPane(dynamicVideoView);
            videoOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");

            StackPane rootPane = Router.getInstance().getRootPane();
            dynamicVideoView.setFitWidth(rootPane.getWidth());
            dynamicVideoView.setFitHeight(rootPane.getHeight());

            rootPane.getChildren().add(videoOverlay);
            mediaPlayer.play();

            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(e -> {
                mediaPlayer.stop();
                rootPane.getChildren().remove(videoOverlay);
                Router.getInstance().navigateTo("onlineMenu");
            });
            delay.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleQuitGame(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        if (currentMode == GameMode.ONLINE_MULTIPLAYER) {
            Client.getInstance().sendMessage("GAME_DISCONNECT");
        }
        Router.getInstance().goBack();
    }

    public static void setGameMode(GameMode gameMode) {
        currentMode = gameMode;
    }

    public static void setAiDepth(int depth) {
        aiDepth = depth;
    }

}

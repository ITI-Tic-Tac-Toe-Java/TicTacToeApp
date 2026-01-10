package com.mycompany.tic_tac_toe_app.controllers;

import com.mycompany.tic_tac_toe_app.game.util.GameListenerImpl;
import com.mycompany.tic_tac_toe_app.game.util.GameMode;
import com.mycompany.tic_tac_toe_app.game.util.GameStrategy;
import com.mycompany.tic_tac_toe_app.game.computer.ComputerGame;
import com.mycompany.tic_tac_toe_app.game.local_multiplay.LocalGame;
import com.mycompany.tic_tac_toe_app.game.util.GameListener;
import com.mycompany.tic_tac_toe_app.game.online_mode.OnlineGame;
import com.mycompany.tic_tac_toe_app.game.util.OfflineGameStrategy;
import com.mycompany.tic_tac_toe_app.network.Client;
import com.mycompany.tic_tac_toe_app.network.ClientProtocol;
import com.mycompany.tic_tac_toe_app.util.Functions;
import com.mycompany.tic_tac_toe_app.util.Router;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Pair;

public class GameController implements Initializable {

    @FXML
    private Button _00, _01, _02, _10, _11, _12, _20, _21, _22;
    private Button[][] boardButtons;
    private GameStrategy gameStrategy;
    private GameListener gameListener;
    private MediaPlayer mediaPlayer;
    private static GameMode currentMode;
    private static int aiDepth = 3;
    private String lastGameSteps = "";

    // متغيرات التحكم في وضع العرض
    private boolean isReplayMode = false;
    private static String replayFilePath = null;

    public static void setReplayPath(String path) {
        replayFilePath = path;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boardButtons = new Button[][]{{_00, _01, _02}, {_10, _11, _12}, {_20, _21, _22}};

        if (replayFilePath != null) {
            this.isReplayMode = true;
            String fileToRead = replayFilePath;
            replayFilePath = null;

            prepareBoardForReplay();
            new Thread(() -> startReplayLogic(fileToRead)).start();
        } else {
            this.isReplayMode = false;
            setupNormalGame();
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

    private void startReplayLogic(String fileName) {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(fileName))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                String[] moves = line.split(";");
                for (String move : moves) {
                    if (move.trim().isEmpty()) {
                        continue;
                    }

                    String[] parts = move.split(":");
                    String[] coords = parts[1].split(",");
                    int row = Integer.parseInt(coords[0]);
                    int col = Integer.parseInt(coords[1]);
                    String symbol = coords[2];

                    Platform.runLater(() -> updateBoardFromReplay(row, col, symbol));
                    Thread.sleep(1000);
                }
                Platform.runLater(() -> {
                    Functions.showInformationAlert("Replay Finished", "The game replay has ended.");
                    isReplayMode = false;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupNormalGame() {
        if (currentMode == null) {
            currentMode = GameMode.LOCAL_MULTIPLAYER;
        }
        gameListener = new GameListenerImpl(currentMode);

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
        }
    }

    @FXML
    private void handleMove(ActionEvent event) {
        if (isReplayMode) {
            return;
        }
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
            }
        });
    }

    public void showResult(String result) {
        Platform.runLater(() -> {
            String videoFile = "";
            String colorStyle = "";
            String status = result.replace(".mp4", "").toUpperCase();

            switch (status) {
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
            }

            if (!colorStyle.isEmpty() && gameStrategy instanceof OfflineGameStrategy) {
                var winningCoords = ((OfflineGameStrategy) gameStrategy).getGameLogic().getWinningCoords();
                for (Pair<Integer, Integer> coord : winningCoords) {
                    boardButtons[coord.getKey()][coord.getValue()].setStyle(colorStyle + " -fx-opacity: 0.8;");
                }
            }

            final String finalVideo = videoFile;
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(600));
            pause.setOnFinished(e -> {
                if (!finalVideo.isEmpty()) {
                    playVideoOverlay(finalVideo);
                } else {
                    askToPlayAgain();
                }
            });
            pause.play();
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

            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
            delay.setOnFinished(e -> {
                mediaPlayer.stop();
                rootPane.getChildren().remove(videoOverlay);
                if (currentMode == GameMode.ONLINE_MULTIPLAYER && !lastGameSteps.isEmpty()) {
                    askToSaveGame();
                } else {
                    askToPlayAgain();
                }
            });
            delay.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void askToSaveGame() {
        Functions.showConfirmAlert("Save Match", null, "Would you like to save this match replay?", "Yes", "No",
                () -> {
                    saveReplayToFile(lastGameSteps);
                    lastGameSteps = "";
                    askToPlayAgain();
                    return null;
                },
                () -> {
                    lastGameSteps = "";
                    askToPlayAgain();
                    return null;
                }
        );
    }

    private void saveReplayToFile(String data) {
        String fileName = "replay_" + System.currentTimeMillis() + ".txt";
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(fileName))) {
            writer.write(data);
            Platform.runLater(() -> Functions.showInformationAlert("Saved", "Replay saved: " + fileName));
        } catch (java.io.IOException e) {
            Platform.runLater(() -> Functions.showErrorAlert(e));
        }
    }

    private void askToPlayAgain() {
        Functions.showConfirmAlert("Match Ended", null, "Do you want to play again?", "Yes", "No",
                () -> {
                    if (currentMode == GameMode.ONLINE_MULTIPLAYER) {
                        Client.getInstance().sendMessage("MOVE:REPLAY_REQUEST");
                    } else {
                        Router.getInstance().goBack();
                        Platform.runLater(() -> Router.getInstance().navigateTo("game"));
                    }
                    return null;
                },
                () -> {
                    if (currentMode == GameMode.ONLINE_MULTIPLAYER) {
                        Client.getInstance().sendMessage("MOVE:QUIT_MATCH");
                    }
                    Platform.runLater(() -> Router.getInstance().navigateTo("onlineMenu"));
                    return null;
                }
        );
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

    public void setLastGameSteps(String steps) {
        this.lastGameSteps = steps;
    }
}

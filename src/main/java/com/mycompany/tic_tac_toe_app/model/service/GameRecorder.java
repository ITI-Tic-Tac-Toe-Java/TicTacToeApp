package com.mycompany.tic_tac_toe_app.model.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameRecorder {

    private static final String SAVE_DIRECTORY = "saved_games/";

    public static void saveGame(String fileName, String gameSteps) {
        File dir = new File(SAVE_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(SAVE_DIRECTORY + fileName + ".txt")) {
            writer.write(gameSteps);
            System.out.println("Game saved successfully: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getSavedFiles() {
        List<String> fileNames = new ArrayList<>();
        File folder = new File(SAVE_DIRECTORY);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    fileNames.add(file.getName().replace(".txt", ""));
                }
            }
        }
        return fileNames;
    }

    public static String loadGameSteps(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_DIRECTORY + fileName + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}

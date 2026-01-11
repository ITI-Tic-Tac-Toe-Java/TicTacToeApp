package com.mycompany.tic_tac_toe_app.game;

import static com.mycompany.tic_tac_toe_app.game.XOGameLogic.Symbol.EMPTY;
import static com.mycompany.tic_tac_toe_app.game.XOGameLogic.Symbol.X;
import static com.mycompany.tic_tac_toe_app.game.XOGameLogic.Symbol.O;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.util.Pair;

public class XOGameLogic {

    public final static class Symbol {
        public static final int EMPTY = 0;
        public static final int X = 1;
        public static final int O = 2;
    }

    private int[][] board = new int[3][3];
    private int stepCount = 0;
    private StringBuffer steps = new StringBuffer();

    public boolean makeMove(int r, int c, int symbol) {
        if (board[r][c] != EMPTY) {
            return false;
        }
        board[r][c] = symbol;
        stepCount++;
        steps.append(stepCount).append(":").append(r).append(",").append(c).append(",").append(symbol).append(";");
        return true;
    }

    private List<Pair<Integer, Integer>> winningCoords = new ArrayList<>();

    public List<Pair<Integer, Integer>> getWinningCoords() {
        return winningCoords;
    }

    public boolean checkWin(int symbol) {
        winningCoords.clear();
        int[][] winConditions = {
            {0, 0, 0, 1, 0, 2}, {1, 0, 1, 1, 1, 2}, {2, 0, 2, 1, 2, 2}, 
            {0, 0, 1, 0, 2, 0}, {0, 1, 1, 1, 2, 1}, {0, 2, 1, 2, 2, 2}, 
            {0, 0, 1, 1, 2, 2}, {0, 2, 1, 1, 2, 0} 
        };

        for (int[] c : winConditions) {
            if (board[c[0]][c[1]] == symbol && board[c[2]][c[3]] == symbol && board[c[4]][c[5]] == symbol) {
                winningCoords.add(new Pair<>(c[0], c[1]));
                winningCoords.add(new Pair<>(c[2], c[3]));
                winningCoords.add(new Pair<>(c[4], c[5]));
                return true;
            }
        }
        return false;
    }

    public boolean isDraw() {
        return stepCount == 9 && (!checkWin(X) && !checkWin(O));
    }

    public int getSymbol(String sym) {
        return sym.equals("X") ? X : O;
    }

    public void reset() {
        board = new int[3][3];
        stepCount = 0;
    }

    public int[] getRandomEmptyPosition() {
        while (!isDraw()) {
            Random r = new Random();
            int random = r.nextInt(9);
            int row = random / 3;
            int col = random % 3;

            if (board[row][col] == EMPTY) {
                return new int[]{row, col};
            }
        }

        return new int[]{-1, -1};
    }

    public List<Pair<Integer, Integer>> getAvailableMoves() {
        List<Pair<Integer, Integer>> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    moves.add(new Pair<>(i, j));
                }
            }
        }
        return moves;
    }

    public void undoMove(int r, int c) {
        if (board[r][c] != EMPTY) {
            board[r][c] = EMPTY;
            stepCount--;
        }
    }
    public String getSteps() {
        return steps.toString();
    }
}

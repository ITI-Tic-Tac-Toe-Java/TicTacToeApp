package com.mycompany.tic_tac_toe_app.model.service;

import java.util.Random;

public class XOGameLogic {

    public static final int EMPTY = 0;
    public static final int X = 1;
    public static final int O = 2;

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

    public boolean checkWin(int symbol) {
        return (board[0][0] == symbol && board[0][1] == symbol && board[0][2] == symbol)
                || (board[1][0] == symbol && board[1][1] == symbol && board[1][2] == symbol)
                || (board[2][0] == symbol && board[2][1] == symbol && board[2][2] == symbol)
                || (board[0][0] == symbol && board[1][0] == symbol && board[2][0] == symbol)
                || (board[0][1] == symbol && board[1][1] == symbol && board[2][1] == symbol)
                || (board[0][2] == symbol && board[1][2] == symbol && board[2][2] == symbol)
                || (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol)
                || (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol);
    }

    public boolean isDraw() {
        return stepCount == 9;
    }

    public int getSymbol(String sym) {
        return sym.equals("X") ? X : O;
    }

    public void reset() {
        board = new int[3][3];
        stepCount = 0;
    }

    public String getEmptyPos() {
        while (!isDraw() && !checkWin(1) && !checkWin(2)) {
            Random r = new Random();
            int random = r.nextInt(9);
            int row = random / 3;
            int col = random % 3;
            /*
            [0] [1] [2]
            [0] [1] [2]
            [0] [1] [2]
             */
            if (board[row][col] == 0) {
                return row + "," + col;
            }
        }
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                if (board[i][j] == 0) {
//                    return i + "," + j;
//                }
//            }
//        }
        return "";
    }
}

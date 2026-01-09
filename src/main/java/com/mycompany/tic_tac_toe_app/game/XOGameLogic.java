package com.mycompany.tic_tac_toe_app.game;

import static com.mycompany.tic_tac_toe_app.game.XOGameLogic.Symbol.EMPTY;
import static com.mycompany.tic_tac_toe_app.game.XOGameLogic.Symbol.X;
import static com.mycompany.tic_tac_toe_app.game.XOGameLogic.Symbol.O;
import java.util.Random;

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

    public boolean hasPlayerWon(int symbol) {
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
        return stepCount == 9 && (!hasPlayerWon(X) && !hasPlayerWon(O));
    }

    public int getSymbol(String sym) {
        return sym.equals("X") ? X : O;
    }

    public void reset() {
        board = new int[3][3];
        stepCount = 0;
    }

    public int[] getEmptyPos() {
        while (!isDraw()) {
            Random r = new Random();
            int random = r.nextInt(9);
            int row = random / 3;
            int col = random % 3;

            if (board[row][col] == EMPTY) {
                return new int[]{row, col};
            }
        }

        return new int[]{-1,-1};
    }
}

package player;

import game.*;
import java.awt.*;
import java.util.ArrayList;

public class AIpositionalPlayer extends AIPlayer {

    public AIpositionalPlayer(int mark, int maxDepth){
        super(mark,maxDepth);
    }

    private static final int[][] POSITIONAL_WEIGHTS = {
            { 500, -150, 30, 10, 10, 30, -150, 500},
            { -150, -250, 0, 0, 0, 0, -250, -150},
            { 30, 0, 1, 2, 2, 1, 0, 30},
            { 10, 0, 2, 16, 16, 2, 0, 10},
            { 10, 0, 2, 16, 16, 2, 0, 10},
            { 30, 0, 1, 2, 2, 1, 0, 30},
            { -150, -250, 0, 0, 0, 0, -250, -150},
            { 500, -150, 30, 10, 10, 30, -150, 500},
    };



    private static final int[][] POSITIONAL_WEIGHTS_2 = {
            { 100, -20, 10, 5, 5, 10, -20, 100},
            { -20, -50, -2, -2, -2, -2, -50, -20},
            { 10, -2, -1, -1, -1, -1, -2, 10},
            { 5, -2, -1, -1, -1, -1, -2, 5},
            { 5, -2, -1, -1, -1, -1, -2, 5},
            { 10, -2, -1, -1, -1, -1, -2, 10},
            { -20, -50, -2, -2, -2, -2, -50, -20},
            { 100, -20, 10, 5, 5, 10, -20, 100}
    };

    protected double evaluateBoard(int[][] board, int player) {
        //System.out.println("appel à evaluateBoard de AIpositionalPlayer");
        int opponent = (player == 1) ? 2 : 1;
        int playerScore = 0, opponentScore = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == player) {
                    playerScore += POSITIONAL_WEIGHTS[i][j];
                } else if (board[i][j] == opponent) {
                    opponentScore += POSITIONAL_WEIGHTS[i][j];
                }
            }
        }
        return playerScore - opponentScore;
    }

}

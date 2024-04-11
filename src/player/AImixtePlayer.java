package player;

import game.GameLogic;

import java.awt.*;
import java.util.ArrayList;

public class AImixtePlayer extends AIPlayer {

    public AImixtePlayer(int mark, int maxDepth){
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

    @Override
    protected double evaluateBoard(int[][] board, int player) {
        int totalMoves = getTotalMoves(board);
        int totalPawns = getTotalPawns(board);

        if (totalMoves <= 25 || totalPawns <= 20) {  // Phase de début de partie
            return evaluatePositional(board, player);
        } else if (totalMoves > 25 && totalMoves <= 40 || totalPawns > 20 && totalPawns <= 44) {  // Phase de milieu de partie
            return evaluateMobility(board, player);
        } else {  // Phase de fin de partie
            return evaluateAbsolute(board, player);
        }
    }

    private int getTotalMoves(int[][] board) {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private int getTotalPawns(int[][] board) {
        int count = 0;
        for (int[] row : board) {
            for (int cell : row) {
                if (cell != 0) count++;
            }
        }
        return count;
    }

    private double evaluatePositional(int[][] board, int player) {
        // Assume POSITIONAL_WEIGHTS is defined somewhere in your class
        int opponent = (player == 1) ? 2 : 1;
        int playerScore = 0, opponentScore = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == player) {
                    playerScore += POSITIONAL_WEIGHTS[i][j];
                } else if (board[i][j] == opponent) {
                    opponentScore += POSITIONAL_WEIGHTS[i][j];
                }
            }
        }
        return playerScore - opponentScore;
    }

    private double evaluateMobility(int[][] board, int player) {
        int opponent = (player == 1) ? 2 : 1;
        ArrayList<Point> playerMoves = GameLogic.getAllPossibleMoves(board, player);
        ArrayList<Point> opponentMoves = GameLogic.getAllPossibleMoves(board, opponent);
        int cornerBonus = calculateCornerBonus(board, player);

        int mobilityScore = playerMoves.size() - opponentMoves.size();
        return mobilityScore + cornerBonus;
    }

    private int calculateCornerBonus(int[][] board, int player) {
        int opponent = (player == 1) ? 2 : 1;
        int score = 0;
        // Corners are at (0,0), (0,7), (7,0), (7,7)
        int[][] corners = {{0, 0}, {0, 7}, {7, 0}, {7, 7}};
        for (int[] corner : corners) {
            if (board[corner[0]][corner[1]] == player) {
                score += 25;  // Bonus for player owning a corner
            } else if (board[corner[0]][corner[1]] == opponent) {
                score -= 25;  // Penalty if opponent owns a corner
            }
        }
        return score;
    }

    private int evaluateAbsolute(int[][] board, int player) {
        int opponent = (player == 1) ? 2 : 1;
        int playerCount = 0;
        int opponentCount = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == player) {
                    playerCount++;
                } else if (board[i][j] == opponent) {
                    opponentCount++;
                }
            }
        }
        return playerCount - opponentCount;
    }

}

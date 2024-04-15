package player;

import game.GameLogic;

import java.awt.*;
import java.util.ArrayList;


public class AIstabilityPlayer extends AIPlayer {

    public AIstabilityPlayer(int mark, int maxDepth){
        super(mark,maxDepth);
    }

    @Override
    protected double evaluateBoard(int[][] board, int player) {
        int stabilityScore = calculateStabilityScore(board, player);
        return stabilityScore;
    }

    private int calculateStabilityScore(int[][] board, int player) {
        int opponent = (player == 1) ? 2 : 1;
        int stableCount = 0;
        int opponentStableCount = 0;

        // Analyze the board for stable discs
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == player && isStable(board, i, j, player)) {
                    stableCount++;
                } else if (board[i][j] == opponent && isStable(board, i, j, opponent)) {
                    opponentStableCount++;
                }
            }
        }

        // Le score est la différence entre le nombre de disques stables du joueur et de son adversaire
        return stableCount - opponentStableCount;
    }

    private boolean isStable(int[][] board, int x, int y, int player) {
        // Corners
        if ((x == 0 || x == board.length - 1) && (y == 0 || y == board[x].length - 1)) {
            return true;
        }

        boolean stableInAllDirections = true;

        int[] directionsX = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] directionsY = {-1, 0, 1, 1, 1, 0, -1, -1};

        for (int i = 0; i < directionsX.length; i++) {
            int dirX = directionsX[i];
            int dirY = directionsY[i];
            boolean lineIsStable = false;

            int currentX = x + dirX;
            int currentY = y + dirY;

            while (currentX >= 0 && currentX < board.length && currentY >= 0 && currentY < board[currentX].length) {
                if (board[currentX][currentY] != player) {
                    break;
                }
                if (isCorner(board, currentX, currentY) || (isEdge(board,currentX, currentY) && isEdgeStable(board, currentX, currentY, dirX, dirY, player))) {
                    lineIsStable = true;
                    break;
                }
                currentX += dirX;
                currentY += dirY;
            }

            if (!lineIsStable) {
                stableInAllDirections = false;
                break;
            }
        }

        return stableInAllDirections;
    }

    private boolean isCorner(int[][] board,int x, int y) {
        return (x == 0 || x == board.length - 1) && (y == 0 || y == board[x].length - 1);
    }

    private boolean isEdge(int[][] board,int x, int y) {
        return x == 0 || x == board.length - 1 || y == 0 || y == board[x].length - 1;
    }

    private boolean isEdgeStable(int[][] board, int x, int y, int dirX, int dirY, int player) {
        // Check the stability of the edge recursively
        while (x >= 0 && x < board.length && y >= 0 && y < board[x].length) {
            if (board[x][y] != player) {
                return false;
            }
            if (isCorner(board,x, y)) {
                return true;
            }
            x += dirX;
            y += dirY;
        }
        return false;
    }


}

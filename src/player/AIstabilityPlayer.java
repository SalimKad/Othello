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

        // Score is the difference in the number of stable discs
        return stableCount - opponentStableCount;
    }

    private boolean isStable(int[][] board, int x, int y, int player) {
        // Stability is complex to compute, this is a simplified version
        // We consider a disc stable if it's surrounded by discs of the same color
        int[] dx = {-1, 0, 1, 0, -1, -1, 1, 1};
        int[] dy = {0, -1, 0, 1, -1, 1, -1, 1};
        for (int dir = 0; dir < dx.length; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if (nx >= 0 && nx < board.length && ny >= 0 && ny < board[0].length) {
                if (board[nx][ny] != player) {
                    return false;
                }
            }
        }
        return true;
    }
}

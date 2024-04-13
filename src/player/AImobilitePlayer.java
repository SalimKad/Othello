package player;

import game.GameLogic;

import java.awt.*;
import java.util.ArrayList;


public class AImobilitePlayer extends AIPlayer {

    public AImobilitePlayer(int mark, int maxDepth){
        super(mark,maxDepth);
    }

    @Override
    protected double evaluateBoard(int[][] board, int player) {
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


}

package player;

import game.*;
import java.awt.*;
import java.util.ArrayList;

public class AIPlayer extends Player {
    private final int maxDepth;

    public AIPlayer(int mark, int maxDepth) {
        super(mark);
        this.maxDepth = maxDepth;
    }

    @Override
    public boolean isUserPlayer() {
        return false;
    }

    @Override
    public String playerName() {
        return "AI Player";
    }

    @Override
    public Point play(int[][] board) {
        System.out.println("AIPlayer play utilisé");
        Point bestMove = findBestMove(board, myMark);
        System.out.println("AIPlayer a choisi : " + bestMove);
        return bestMove;
    }

    private Point findBestMove(int[][] board, int player) {
        double bestValue = Double.NEGATIVE_INFINITY;
        Point bestMove = null;
        ArrayList<Point> possibleMoves = GameLogic.getAllPossibleMoves(board, player);

        for (Point move : possibleMoves) {
            int[][] newBoard = GameLogic.getNewBoardAfterMove(board, move.x, move.y, player);
            double moveValue = minMax(newBoard, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private double minMax(int[][] board, int depth, double alpha, double beta, boolean isMaximizing) {
        if (depth == maxDepth || GameLogic.isGameFinished(board)) {
            return evaluateBoard(board, myMark);
        }

        if (isMaximizing) {
            double maxEval = Double.NEGATIVE_INFINITY;
            for (Point move : GameLogic.getAllPossibleMoves(board, myMark)) {
                int[][] newBoard = GameLogic.getNewBoardAfterMove(board, move.x, move.y, myMark);
                double eval = minMax(newBoard, depth + 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break; // Alpha-Beta Pruning
            }
            return maxEval;
        } else {
            double minEval = Double.POSITIVE_INFINITY;
            for (Point move : GameLogic.getAllPossibleMoves(board, 3 - myMark)) { // Assuming 2 players: 1 and 2
                int[][] newBoard = GameLogic.getNewBoardAfterMove(board, move.x, move.y, 3 - myMark);
                double eval = minMax(newBoard, depth + 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break; // Alpha-Beta Pruning
            }
            return minEval;
        }
    }

    private double evaluateBoard(int[][] board, int player) {
        return GameLogic.getPlayerStoneCount(board, player);
    }

}

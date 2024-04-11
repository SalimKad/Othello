package player;

import game.*;
import java.awt.*;
import java.util.ArrayList;

public class AIabsolutePlayer extends AIPlayer {

    public AIabsolutePlayer(int mark, int maxDepth){
        super(mark,maxDepth);
    }

    @Override
    protected double evaluateBoard(int[][] board, int player) {
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

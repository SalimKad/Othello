package player;

import game.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class AIClassicPlayer extends Player {

    Random rnd = new Random();

    public AIClassicPlayer(int mark) {
        super(mark);
    }

    @Override
    public boolean isUserPlayer() {
        return true;
    }

    @Override
    public Point play(int[][] board) {
        ArrayList<Point> myPossibleMoves = GameLogic.getAllPossibleMoves(board,myMark);

        if(myPossibleMoves.size() > 0){
            Point move = new Point(myPossibleMoves.get(rnd.nextInt(myPossibleMoves.size())));
            System.out.println(move);
            return move;
        }else{
            return null;
        }

    }

}
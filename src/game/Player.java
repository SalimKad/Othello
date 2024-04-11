package game;

import java.awt.*;

public abstract class Player {

    protected int myMark;
    public Player(int mark){
        myMark = mark;
    }

    abstract public boolean isUserPlayer();

    abstract public String playerName();

    abstract public Point play(int[][] board);

    public int getMark() {
        return myMark;
    }

}

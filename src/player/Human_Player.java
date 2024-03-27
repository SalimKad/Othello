package player;

import java.awt.*;
import game.Player;

public class Human_Player extends Player {

    public Human_Player(int mark) {
        super(mark);
    }

    @Override
    public boolean isUserPlayer() {
        return true;
    }

    @Override
    public String playerName() {
        return "User" ;
    }

    @Override
    public Point play(int[][] board) {
        return null;
    }

}
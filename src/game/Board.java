package game;

import javax.swing.*;
import java.awt.*;

//grille, tableau de cellules
//board printer
//je vois pas l'utilité pour l'instant
public class Board extends JFrame implements BoardInterface {

    public Cell[][] cells;
    int [][] board;

    public Board(String title, int [][] board) {
        this.board = board;

        Panel othelloBoard = new Panel();

        cells = new Cell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new Cell(this,othelloBoard,i,j);
                othelloBoard.add(cells[i][j]);
            }
        }

        this.add(othelloBoard);
        this.setTitle(title);
        this.pack();
        this.setVisible(true);

        othelloBoard.repaint();
    }

    @Override
    public int getBoardValue(int i, int j) {
        return board[i][j];
    }

    @Override
    public void setBoardValue(int i, int j, int value) {
        System.err.println("Error");
    }

    @Override
    public void handleClick(int i, int j) {

    }
}

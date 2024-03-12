package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//cellule dans la grille
// board cell
public class Cell extends JLabel implements MouseListener {

    int i, j; //ligne & colonne
    JPanel grid; //grille
    BoardInterface boardInterface; //interface de la grille

    public Cell(BoardInterface boardInterface, JPanel grid, int i, int j) {
        this.boardInterface = boardInterface;
        this.grid = grid;
        this.i = i;
        this.j = j;
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int margin_left = this.getWidth() / 10;
        int margin_top = this.getHeight() / 10;

        //contour de la cellule
        g.setColor(Color.black);
        g.drawRect(0, 0, this.getWidth(), this.getHeight());

        //pièce noire ou blanche dans la cellule
        int value = boardInterface.getBoardValue(i, j);
        if (value == 1) { //joueur 1
            g.setColor(Color.black);
            g.fillOval(margin_left,margin_top,this.getWidth()-2*margin_left,this.getHeight()-2*margin_top);
            //revoir les fillOval
        }
        else if (value == 2) {
            g.setColor(Color.WHITE);
            g.fillOval(margin_left,margin_top,this.getWidth()-2*margin_left,this.getHeight()-2*margin_top);
        }

        super.paintComponent(g);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
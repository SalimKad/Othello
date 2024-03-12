package game;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

//game panel
//tous les composants de la fenêtre
public class Panel extends JPanel implements BoardInterface {

    int [][] board;
    Cell[][] cells;

    int turn;

    JLabel title;
    JLabel description;
    JLabel score1;
    JLabel score2;

    int totalscore1 = 0;
    int totalscore2 = 0;

    JLabel totscore1;
    JLabel totscore2;

    Timer timerplayer1;
    Timer timerplayer2;

    @Override
    public int getBoardValue(int i,int j){
        return board[i][j];
    }

    @Override
    public void setBoardValue(int i,int j, int value){
        board[i][j] = value;
    }

    @Override
    public void handleClick(int i, int j) {

    }

    public Panel() {
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        JPanel othelloBoard = new JPanel();
        othelloBoard.setLayout(new GridLayout(8,8));
        othelloBoard.setBackground(new Color(7,50,18));
        othelloBoard.setSize(new Dimension(600,600)); //taille de la fenêtre

        //init board
        resetBoard();

        cells = new Cell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new Cell(this,othelloBoard,i,j);
                othelloBoard.add(cells[i][j]);
            }
        }

        JPanel rightbar = new JPanel();
        rightbar.setLayout(new BoxLayout(rightbar,BoxLayout.Y_AXIS));
        rightbar.setPreferredSize(new Dimension(200,0));

        title = new JLabel("  Othello  ", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.darkGray);

        description = new JLabel("La partie s’achève lorsque aucun des deux joueurs ne peut plus jouer de coup légal.");
        //faire en sorte que la description se mette sur +sieurs lignes automatiquement
        description.setFont(new Font ("Arial", Font.ITALIC, 15));
        description.setForeground(Color.darkGray);

        score1 = new JLabel("Score Joueur 1");
        score2 = new JLabel("Score Joueur 2");
        totscore1 = new JLabel("Total Score Joueur 1");
        totscore2 = new JLabel("Total Score Joueur 2");
        EmptyBorder leftBorder = new EmptyBorder(0, 20,0,0);
        EmptyBorder descrBorder = new EmptyBorder(0,0,20,0);

        EmptyBorder borderTitleScore = new EmptyBorder(20, 0, 20, 0);
        LineBorder line = new LineBorder(Color.darkGray, 2, true);
        CompoundBorder titleBorder = new CompoundBorder(borderTitleScore, line);

        rightbar.add(title);
        title.setBorder(titleBorder);
        rightbar.setBorder(leftBorder);

        rightbar.add(description);
        description.setBorder(descrBorder);
        rightbar.add(score1);
        rightbar.add(score2);
        rightbar.add(new JLabel("---------------------------"));
        rightbar.add(totscore1);
        rightbar.add(totscore2);


        this.add(rightbar,BorderLayout.EAST);
        this.add(othelloBoard);


    }

    public void resetBoard(){
        board = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j]=0;
            }
        }
        //initial board state
        setBoardValue(3,3,2); //blanc
        setBoardValue(4,4,2);
        setBoardValue(3,4,1); //noir
        setBoardValue(4,3,1);

    }

}

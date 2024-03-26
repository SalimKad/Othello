package game;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

//game panel
//tous les composants de la fenêtre
public class Panel extends JPanel implements BoardInterface {

    int [][] board;
    Cell[][] cells;

    int turn = 1; //noir
    Color boardColor = new Color(201,193,253); //violet clair

    JLabel title;
    JTextArea description;
    JLabel score1;
    JLabel score2;
    private boolean awaitForClick = true; //remettre a false + tard

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
        System.out.println("Clicked case");

        if(awaitForClick && GameLogic.canPlay(board,turn,i,j)){
            System.out.println("User Played in : "+ i + " , " + j);

            //update board
            board = GameLogic.getNewBoardAfterMove(board,new Point(i,j),turn);
            repaint();

            //advance turn
            turn = (turn == 1) ? 2 : 1;

            //awaitForClick = false;

            //manageTurn();
        }
    }

    public Panel() {
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        JPanel othelloBoard = new JPanel();
        othelloBoard.setLayout(new GridLayout(8,8));
        othelloBoard.setBackground(boardColor);
        othelloBoard.setSize(new Dimension(800,800)); //taille de la fenêtre

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

        description = new JTextArea("La partie s’achève lorsque aucun des deux joueurs ne peut plus jouer de coup légal.");
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        //faire en sorte que la description se mette sur +sieurs lignes automatiquement
        description.setFont(new Font ("Arial", Font.ITALIC, 15));
        description.setForeground(Color.darkGray);
        description.setBackground(UIManager.getColor(rightbar));
        description.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

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

        updateBoardInfo();
        //updateTotalScore()
        //time
        //turn
    }

    public void resetBoard(){
        board = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j]=0;
            }
        }
        //retour à la grille initiale
        setBoardValue(3,3,2); //blanc
        setBoardValue(4,4,2);
        setBoardValue(3,4,1); //noir
        setBoardValue(4,3,1);

    }

    public void updateBoardInfo(){

        int p1score = 0;
        int p2score = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] == 1) p1score++;
                if(board[i][j] == 2) p2score++;

                if(GameLogic.canPlay(board,turn,i,j)){
                    cells[i][j].highlight = 1;
                }else{
                    cells[i][j].highlight = 0;
                }
            }
        }

        //score1.setText(player1.playerName() + " : " + p1score);
        //score2.setText(player2.playerName() + " : " + p2score);
    }


    /*
    public void manageTurn(){
        if(GameLogic.hasAnyMoves(board,1) || GameLogic.hasAnyMoves(board,2)) {
            updateBoardInfo();
            if (turn == 1) {
                if(GameLogic.hasAnyMoves(board,1)) {
                    if (player1.isUserPlayer()) {
                        awaitForClick = true;
                        //after click this function should be call backed
                    } else {
                        player1HandlerTimer.start();
                    }
                }else{
                    //forfeit this move and pass the turn
                    System.out.println("Player 1 has no legal moves !");
                    turn = 2;
                    manageTurn();
                }
            } else {
                if(GameLogic.hasAnyMoves(board,2)) {
                    if (player2.isUserPlayer()) {
                        awaitForClick = true;
                        //after click this function should be call backed
                    } else {
                        player2HandlerTimer.start();
                    }
                }else{
                    //forfeit this move and pass the turn
                    System.out.println("Player 2 has no legal moves !");
                    turn = 1;
                    manageTurn();
                }
            }
        }else{
            //game finished
            System.out.println("Game Finished !");

            int winner = GameLogic.getWinner(board);
            if(winner==1) totalscore1++;
            else if(winner==2) totalscore2++;
            //updateTotalScore();
            //restart
            //resetBoard();
            //turn=1;
            //manageTurn();
        }
    }*/

    //total score

}

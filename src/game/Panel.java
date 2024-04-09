package game;

import player.AIClassicPlayer;
import player.Human_Player;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;

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

    Human_Player player1 = new Human_Player(1);
    //Human_Player player2 = new Human_Player(2);
    AIClassicPlayer player2 = new AIClassicPlayer(2);


    @Override
    public int getBoardValue(int i,int j){
        return board[i][j];
    }

    @Override
    public void setBoardValue(int i,int j, int value){
        board[i][j] = value;
    }

    @Override
    public void handleClick(int i, int j) throws InterruptedException {
        //System.out.println("Clicked case "+i+","+j);
        if(awaitForClick && GameLogic.canPlay(board,turn,i,j)){
            System.out.println("Player " + turn + " played in case : "+ i + " , " + j);
            board = GameLogic.getNewBoardAfterMove(board,i,j,turn);
            repaint();

            turn = (turn == 1) ? 2 : 1;
            updateBoardInfo();

            awaitForClick = false;

            manageTurn();
        }
    }

    public Panel() throws InterruptedException {
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        JPanel othelloBoard = new JPanel();
        othelloBoard.setLayout(new GridLayout(8,8));
        othelloBoard.setBackground(boardColor);
        othelloBoard.setSize(new Dimension(800,500)); //taille de la fenêtre

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
        rightbar.setPreferredSize(new Dimension(300,0));

        title = new JLabel("  Othello  ", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.darkGray);

        description = new JTextArea("La partie s’achève lorsque aucun des deux joueurs ne peut plus jouer de coup légal.");
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setFont(new Font ("Poppins", Font.ITALIC, 15));
        description.setForeground(Color.darkGray);
        description.setBackground(UIManager.getColor(rightbar));


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

        manageTurn();

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

        ArrayList<Point> possibleMoves = new ArrayList<>();
        possibleMoves = GameLogic.getAllPossibleMoves(board, turn);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 1) p1score++;
                if (board[i][j] == 2) p2score++;

                Point point = new Point(i,j);
                if(possibleMoves.contains(point)) {
                    cells[i][j].highlight = 1;
                }
                else {
                    cells[i][j].highlight = 0;
                }
            }
        }

        //score1.setText(player1.playerName() + " : " + p1score);
        //score2.setText(player2.playerName() + " : " + p2score);
    }


    public void manageTurn() throws InterruptedException {
        if(GameLogic.hasAnyMoves(board,1) || GameLogic.hasAnyMoves(board,2)) {
            updateBoardInfo();
            if (turn == 1) {
                if(GameLogic.hasAnyMoves(board,1)) {
                    if (player1.isUserPlayer()) {
                        awaitForClick = true;
                        //after click this function should be call backed
                    } else {
                        //timerplayer1.start();
                    }
                }else{
                    System.out.println("Player 1 can't play !");
                    turn = 2;
                    manageTurn();
                }
            } else {
                if(GameLogic.hasAnyMoves(board,2)) {
                    if (player2.isUserPlayer()) {
                        handleAI(player2);
                        //awaitForClick = false;
                    } else {
                        //timerplayer2.start();
                    }
                }else{
                    System.out.println("Player 2 can't play !");
                    turn = 1;
                    manageTurn();
                }
            }
        }else{
            System.out.println("Game Finished !");
            int winner = GameLogic.getWinner(board);
            if(winner==1) {
                totalscore1++;
            }
            else if(winner==2) totalscore2++;
            System.out.println("Player " + winner + " is the winner !");
            //updateTotalScore();
            //restart
            resetBoard();
            turn=1;
            manageTurn();
        }
    }

    public void handleAI(AIClassicPlayer ai) throws InterruptedException {
        Point aiPlayPoint = ai.play(board);
        //System.out.println(aiPlayPoint);
        int i = aiPlayPoint.x;
        int j = aiPlayPoint.y;
        if(!GameLogic.canPlay(board,ai.myMark,i,j)) System.err.println("AI Invalid Move !");
        System.out.println("Player " + turn + " played in case : "+ i + " , " + j);

        board = GameLogic.getNewBoardAfterMove(board,aiPlayPoint.x, aiPlayPoint.y,turn);

        turn = (turn == 1) ? 2 : 1;
        updateBoardInfo();
        manageTurn();
        repaint();
        Thread.sleep(1000);
    }

    //total score

}

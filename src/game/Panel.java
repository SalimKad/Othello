package game;

import player.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

//game panel
//tous les composants de la fenêtre
public class Panel extends JPanel implements BoardInterface {

    int[][] board;
    Cell[][] cells;

    boolean gameIsOver = false;

    int turn = 1; //noir
    Color boardColor = new Color(201, 193, 253); //violet clair

    JLabel title;
    JTextArea description;
    JLabel score1;
    JLabel score2;

    private boolean awaitForClick = true; //remettre a false + tard

    int totalscore1 = 0;
    int totalscore2 = 0;

    private Timer aiTimer;

    JLabel totscore1;
    JLabel totscore2;

    Timer timerplayer1;
    Timer timerplayer2;
    int timeLeftPlayer1;  // En secondes
    int timeLeftPlayer2;  // En secondes
    private long startTimePlayer1;
    private long startTimePlayer2;


    Player player1;
    Player player2;

    public Panel() {
        setLayout(new BorderLayout());
        SwingUtilities.invokeLater(() -> {
            try {
                showPlayerSetupDialog();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });  // Utilise SwingUtilities pour s'assurer que cela est exécuté dans l'EDT


        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        JPanel othelloBoard = new JPanel();
        othelloBoard.setLayout(new GridLayout(8, 8));
        othelloBoard.setBackground(boardColor);
        othelloBoard.setSize(new Dimension(800, 500)); //taille de la fenêtre

        resetBoard();
        cells = new Cell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new Cell(this, othelloBoard, i, j);
                othelloBoard.add(cells[i][j]);
            }
        }

        JPanel rightbar = new JPanel();
        rightbar.setLayout(new BoxLayout(rightbar, BoxLayout.Y_AXIS));
        rightbar.setPreferredSize(new Dimension(300, 0));

        title = new JLabel("  Othello  ", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.darkGray);

        description = new JTextArea("La partie s’achève lorsque aucun des deux joueurs ne peut plus jouer de coup légal.");
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setFont(new Font("Poppins", Font.ITALIC, 15));
        description.setForeground(Color.darkGray);
        description.setBackground(UIManager.getColor(rightbar));

        totscore1 = new JLabel("Total Score Joueur 1 : 0");
        totscore1.setFont(new Font("Arial", Font.BOLD, 15));
        totscore2 = new JLabel("Total Score Joueur 2 : 0");
        totscore2.setFont(new Font("Arial", Font.BOLD, 15));
        EmptyBorder leftBorder = new EmptyBorder(0, 20, 0, 0);
        EmptyBorder descrBorder = new EmptyBorder(0, 0, 20, 0);

        EmptyBorder borderTitleScore = new EmptyBorder(20, 0, 20, 0);
        LineBorder line = new LineBorder(Color.darkGray, 2, true);
        CompoundBorder titleBorder = new CompoundBorder(borderTitleScore, line);

        rightbar.add(title);
        title.setBorder(titleBorder);
        rightbar.setBorder(leftBorder);

        rightbar.add(description);
        description.setBorder(descrBorder);

        rightbar.add(totscore1);
        rightbar.add(totscore2);

        rightbar.add(Box.createVerticalGlue()); // Ajoute de l'espace flexible autour des éléments pour une meilleure disposition

        this.add(rightbar, BorderLayout.EAST);
        this.add(othelloBoard);


        updateBoardInfo();
        //time
        // Créer les chronomètres pour chaque joueur
        timerplayer1 = new Timer(1000, e -> {
            try {
                updateTimerConsole(1);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        timerplayer2 = new Timer(1000, e -> {
            try {
                updateTimerConsole(2);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        //turn
    }

    public void resetBoard() {
        board = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 0;
            }
        }
        //retour à la grille initiale
        setBoardValue(3, 3, 2); //blanc
        setBoardValue(4, 4, 2);
        setBoardValue(3, 4, 1); //noir
        setBoardValue(4, 3, 1);

        timeLeftPlayer1 = 30;
        timeLeftPlayer2 = 30;
        gameIsOver = false;  // S'assurer que le jeu est marqué comme non terminé
    }


    @Override
    public int getBoardValue(int i, int j) {
        return board[i][j];
    }

    @Override
    public void setBoardValue(int i, int j, int value) {
        board[i][j] = value;
    }

    @Override
    public void handleClick(int i, int j) throws InterruptedException {
        //System.out.println("Clicked case "+i+","+j);
        if (awaitForClick && GameLogic.canPlay(board, turn, i, j)) {
            System.out.println("Joueur " + turn + " a joué dans la case : " + i + " , " + j);
            board = GameLogic.getNewBoardAfterMove(board, i, j, turn);
            repaint();

            turn = (turn == 1) ? 2 : 1;
            //System.out.println("Turn : " + turn);
            updateBoardInfo();
            //System.out.println("Board updated");

            awaitForClick = false;

            /*long endTime = System.currentTimeMillis();
            if (turn == 1) {
                timeLeftPlayer1 -= (int) ((endTime - startTimePlayer1) / 1000); // Convertir en secondes
                System.out.println("Timer Joueur 1: " + formatTime(timeLeftPlayer1));
            } else {
                timeLeftPlayer2 -= (int) ((endTime - startTimePlayer2) / 1000); // Convertir en secondes
                System.out.println("Timer Joueur 2: " + formatTime(timeLeftPlayer2));
            }*/

            // Gérer le timer dans l'EDT
            SwingUtilities.invokeLater(() -> {
                if (turn == 1) {
                    try {
                        updateTimerConsole(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        updateTimerConsole(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            manageTurn();
        }
    }


    public void updateBoardInfo() {

        int p1score = 0;
        int p2score = 0;

        ArrayList<Point> possibleMoves;
        possibleMoves = GameLogic.getAllPossibleMoves(board, turn);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 1) p1score++;
                if (board[i][j] == 2) p2score++;

                Point point = new Point(i, j);
                if (possibleMoves.contains(point)) {
                    cells[i][j].highlight = 1;
                } else {
                    cells[i][j].highlight = 0;
                }
            }
        }
    }


    public void manageTurn() throws InterruptedException {
        // Arrêt de tous les Timers avant de commencer à gérer le tour pour éviter les déclenchements non désirés.
        timerplayer1.stop();
        timerplayer2.stop();

        // démarrage du Timer après le début du tour de chaque joueur.
        if (turn == 1) {
            startTimePlayer1 = System.currentTimeMillis();
            timerplayer1.start();
        } else {
            startTimePlayer2 = System.currentTimeMillis();
            timerplayer2.start();
        }

        if (!GameLogic.hasAnyMoves(board, turn)) {
            System.out.println("Joueur " + turn + " ne peut pas jouer !");
            turn = (turn == 1) ? 2 : 1;  // Passer le tour à l'autre joueur
            System.out.println("Turn : " + turn);
            if (!GameLogic.hasAnyMoves(board, turn)) {
                System.out.println("Aucun des deux joueurs ne peut jouer !");
                endGame();// Terminer la partie si aucun des deux joueurs ne peut jouer
            } else {
                manageTurn(); // Continuer avec le prochain tour
            }
        } else {
            updateBoardInfo();
            Player currentPlayer = (turn == 1) ? player1 : player2;

            if (currentPlayer.isUserPlayer()) {
                awaitForClick = true; // Attendre un clic de l'utilisateur s'il s'agit d'un joueur humain
            } else {
                //handleAI(currentPlayer); // Gérer le tour automatiquement s'il s'agit d'une IA
                aiTimer = new Timer(500, e -> {
                    try {
                        handleAI(currentPlayer);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                });
                aiTimer.setRepeats(false); // S'assurer que le Timer s'exécute une seule fois
                aiTimer.start();
            }
        }
    }

    private void handleAI(Player ai) throws InterruptedException {
        Point aiPlayPoint = ai.play(board);
        if (aiPlayPoint != null) {
            executeMove(aiPlayPoint.x, aiPlayPoint.y, ai);
        } else {
            System.out.println(ai + " AIPlayer n'a pas de mouvement valide.");
            turn = (turn == 1) ? 2 : 1; // Passer le tour à l'adversaire
            System.out.println("Tour : " + turn);

            manageTurn(); // Continuer avec le prochain tour
        }
    }

    private void executeMove(int x, int y, Player player) throws InterruptedException {
        if (GameLogic.canPlay(board, player.getMark(), x, y)) {
            System.out.println("Joueur " + turn + " a joué dans la case : " + x + " , " + y);
            board = GameLogic.getNewBoardAfterMove(board, x, y, player.getMark());
            turn = (turn == 1) ? 2 : 1;
            repaint();
            updateBoardInfo();
            //Thread.sleep(500); // Attendre pour visualisation
            manageTurn();
        } else {
            System.err.println("Mouvement invalide par l'IA");
        }
    }

    private void endGame() throws InterruptedException {
        // Arrêter les timers dès que le jeu se termine
        timerplayer1.stop();
        timerplayer2.stop();
        gameIsOver = true;  // Ajoutez un indicateur de fin de partie

        int winner = GameLogic.getWinner(board);
        String winnerName = (winner == 1) ? "Joueur 1" : "Joueur 2";
        //winnerLabel.setText("Dernier gagnant: " + winnerName);  // Mettre à jour le label avec le nom du gagnant
        System.out.println("Joueur " + winner + " est le gagnant !");
        totalscore1 += GameLogic.getPlayerStoneCount(board, 1);
        totalscore2 += GameLogic.getPlayerStoneCount(board, 2);

        // Mettre à jour le score total uniquement à la fin de la partie
        totscore1.setText("Total Score Joueur 1: " + totalscore1);
        totscore2.setText("Total Score Joueur 2: " + totalscore2);

        int response = JOptionPane.showConfirmDialog(null, winnerName + " a gagné !\nVoulez-vous rejouer?", "Partie terminée",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            showPlayerSetupDialog();  // Afficher à nouveau le popup pour choisir les joueurs
        } else {
            System.out.println("Le jeu est terminé. Fermeture de l'application.");
            System.exit(0);  // Quitter l'application
        }

        timerplayer1.stop(); // Arrêter les timers à la fin de la partie
        timerplayer2.stop();
        resetBoard();
        turn = 1;
        updateBoardInfo();
        repaint();
    }

    private void showPlayerSetupDialog() throws InterruptedException {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this); // Récupérer la fenêtre parente
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JComboBox<String> player1Type = new JComboBox<>(new String[]{"Humain", "IA Classique", "IA Positionnelle","IA Absolue", "IA Mobilité", "IA Mixte", "IA Stabilité"});
        JComboBox<String> player2Type = new JComboBox<>(new String[]{"Humain", "IA Classique", "IA Positionnelle","IA Absolue", "IA Mobilité", "IA Mixte", "IA Stabilité"});
        JSpinner player1Depth = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        JSpinner player2Depth = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        player1Depth.setEnabled(false);
        player2Depth.setEnabled(false);

        // Ajouter des écouteurs pour activer/désactiver les spinners
        player1Type.addActionListener(e -> player1Depth.setEnabled("IA Classique".equals(player1Type.getSelectedItem()) || "IA Positionnelle".equals(player1Type.getSelectedItem()) || "IA Absolue".equals(player1Type.getSelectedItem()) ||
                "IA Mobilité".equals(player1Type.getSelectedItem()) || "IA Mixte".equals(player1Type.getSelectedItem()) || "IA Stabilité".equals(player1Type.getSelectedItem())));
        player2Type.addActionListener(e -> player2Depth.setEnabled("IA Classique".equals(player2Type.getSelectedItem()) || "IA Positionnelle".equals(player2Type.getSelectedItem()) || "IA Absolue".equals(player2Type.getSelectedItem()) ||
                "IA Mobilité".equals(player2Type.getSelectedItem()) || "IA Mixte".equals(player2Type.getSelectedItem()) || "IA Stabilité".equals(player2Type.getSelectedItem())));

        panel.add(new JLabel("Choisir Joueur 1:"));
        panel.add(player1Type);
        panel.add(new JLabel("Profondeur max:"));
        panel.add(player1Depth);
        panel.add(new JLabel("Choisir Joueur 2:"));
        panel.add(player2Type);
        panel.add(new JLabel("Profondeur max:"));
        panel.add(player2Depth);

        // Utiliser 'frame' comme parent pour la boîte de dialogue
        int result = JOptionPane.showConfirmDialog(frame, panel, "Configuration des joueurs",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            int depth1 = (int) player1Depth.getValue();
            int depth2 = (int) player2Depth.getValue();
            player1 = createPlayer((String) Objects.requireNonNull(player1Type.getSelectedItem()), 1, depth1);
            player2 = createPlayer((String) Objects.requireNonNull(player2Type.getSelectedItem()), 2, depth2);
            resetBoard();
            updateBoardInfo();
            repaint();
            manageTurn();
        } else {
            System.out.println("La configuration du jeu a été annulée.");
            System.exit(0);  // Quitter l'application
        }
    }

    private Player createPlayer(String selection, int mark, int maxDepth) {
        switch (selection) {
            case "Humain":
                return new Human_Player(mark);
            case "IA Classique":
                return new AIPlayer(mark, maxDepth);
            case "IA Positionnelle":
                return new AIpositionalPlayer(mark, maxDepth);
            case "IA Absolue":
                return new AIabsolutePlayer(mark, maxDepth);
            case "IA Mobilité":
                return new AImobilitePlayer(mark, maxDepth);
            case "IA Mixte":
                return new AImixtePlayer(mark, maxDepth);
            case "IA Stabilité":
                return new AIstabilityPlayer(mark, maxDepth);
            default:
                return new Human_Player(mark);  // Default to human if something goes wrong
        }
    }

    private void updateTimerConsole(int playerNum) throws InterruptedException {
        if (gameIsOver) {
            timerplayer1.stop();
            timerplayer2.stop();
            return; // Sortir directement si le jeu est fini
        }

        if (playerNum == 1) {
            if (timeLeftPlayer1 > 0) {
                timeLeftPlayer1--;
                if(timeLeftPlayer1 % 5 == 0){
                    System.out.println("Timer Joueur 1: " + formatTime(timeLeftPlayer1));
                }
                //System.out.println("le if " + (timeLeftPlayer1<=0) + " timeLeftPlayer1 " + timeLeftPlayer1);
                if (timeLeftPlayer1 <= 0) {
                    //System.out.println("Le temps est écoulé pour le Joueur 1, on call endGameDueToTime !");
                    timerplayer1.stop();
                    endGameDueToTime(1);
                }
            }
        } else {
            if (timeLeftPlayer2 > 0) {
                timeLeftPlayer2--;
                if(timeLeftPlayer2 % 5 == 0){
                    System.out.println("Timer Joueur 2: " + formatTime(timeLeftPlayer2));
                }
                //System.out.println("le if " + (timeLeftPlayer1<=0) + " timeLeftPlayer1 " + timeLeftPlayer1);
                if (timeLeftPlayer2 <= 0) {
                    //System.out.println("Le temps est écoulé pour le Joueur 1, on call endGameDueToTime !");
                    timerplayer2.stop();
                    endGameDueToTime(2);
                }
            }
        }
    }

    private void endGameDueToTime(int losingPlayerNum) throws InterruptedException {
        // Arrêter les timers pour éviter des appels multiples
        timerplayer1.stop();
        timerplayer2.stop();

        // Vérifier si le jeu n'est pas déjà fini
        if (!gameIsOver) {
            String winningPlayerName = (losingPlayerNum == 1) ? "Joueur 2" : "Joueur 1";
            System.out.println("Le temps est écoulé ! " + winningPlayerName + " gagne par le temps.");
            JOptionPane.showMessageDialog(this, "Le temps est écoulé ! " + winningPlayerName + " gagne par le temps.",
                    "Temps écoulé", JOptionPane.INFORMATION_MESSAGE);
            int response = JOptionPane.showConfirmDialog(null, "Voulez-vous rejouer ?", "Partie terminée",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                showPlayerSetupDialog();  // Afficher à nouveau le popup pour choisir les joueurs
            } else {
                System.out.println("Le jeu est terminé. Fermeture de l'application.");
                System.exit(0);  // Quitter l'application
            }
            resetBoard();
            turn = 1;
            updateBoardInfo();
            repaint();
            gameIsOver = true;  // Marquer le jeu comme terminé
        }
    }

    private String formatTime(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}

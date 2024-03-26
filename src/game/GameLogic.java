package game;
//logique du jeu, qui a gagné, est-ce que le jeu est fini...
//board helper

import java.awt.*;
import java.util.ArrayList;

public class GameLogic {
    public static int[][] start() {
        int[][] board = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j]=0;
            }
        }
        //retour à la grille initiale
        board[3][3] =2 ; //blanc
        board[4][4] = 2 ;
        board[3][4] = 1 ; //noir
        board[4][3] = 1;

        return board;
    }

    public static Point getMove(int[][] before , int[][] after){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(before[i][j]==0 && after[i][j]!=0){
                    return new Point(i,j);
                }
            }
        }
        return null;
    }

    public static int getTotalStoneCount(int[][] board){
        int c = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] != 0) c++;
            }
        }
        return c;
    }

    public static boolean canPlay(int[][] board,int player,int i,int j){

        if(board[i][j] != 0) return false;

        int mi , mj , c;
        int oplayer = ((player == 1) ? 2 : 1);

        //move up
        mi = i - 1;
        mj = j;
        c = 0;
        while(mi>0 && board[mi][mj] == oplayer){
            mi--;
            c++;
        }
        if(mi>=0 && board[mi][mj] == player && c>0) return true;


        //move down
        mi = i + 1;
        mj = j;
        c = 0;
        while(mi<7 && board[mi][mj] == oplayer){
            mi++;
            c++;
        }
        if(mi<=7 && board[mi][mj] == player && c>0) return true;

        //move left
        mi = i;
        mj = j - 1;
        c = 0;
        while(mj>0 && board[mi][mj] == oplayer){
            mj--;
            c++;
        }
        if(mj>=0 && board[mi][mj] == player && c>0) return true;

        //move right
        mi = i;
        mj = j + 1;
        c = 0;
        while(mj<7 && board[mi][mj] == oplayer){
            mj++;
            c++;
        }
        if(mj<=7 && board[mi][mj] == player && c>0) return true;

        //move up left
        mi = i - 1;
        mj = j - 1;
        c = 0;
        while(mi>0 && mj>0 && board[mi][mj] == oplayer){
            mi--;
            mj--;
            c++;
        }
        if(mi>=0 && mj>=0 && board[mi][mj] == player && c>0) return true;

        //move up right
        mi = i - 1;
        mj = j + 1;
        c = 0;
        while(mi>0 && mj<7 && board[mi][mj] == oplayer){
            mi--;
            mj++;
            c++;
        }
        if(mi>=0 && mj<=7 && board[mi][mj] == player && c>0) return true;

        //move down left
        mi = i + 1;
        mj = j - 1;
        c = 0;
        while(mi<7 && mj>0 && board[mi][mj] == oplayer){
            mi++;
            mj--;
            c++;
        }
        if(mi<=7 && mj>=0 && board[mi][mj] == player && c>0) return true;

        //move down right
        mi = i + 1;
        mj = j + 1;
        c = 0;
        while(mi<7 && mj<7 && board[mi][mj] == oplayer){
            mi++;
            mj++;
            c++;
        }
        if(mi<=7 && mj<=7 && board[mi][mj] == player && c>0) return true;

        return false;
    }

    public static ArrayList<Point> getAllPossibleMoves(int[][] board, int player){
        ArrayList<Point> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(canPlay(board,player,i,j)){
                    result.add(new Point(i,j));
                }
            }
        }
        return result;
    }

    public static boolean hasAnyMoves(int[][] board, int player){
        return getAllPossibleMoves(board,player).size() > 0;
    }

    public static int[][] updateCaseColor(int[][] board, int x, int y, int player) {
        // mettre à jour le highlight également

        int[][] newboard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, newboard[i], 0, 8);
        }

        int opponent = (player == 1) ? 2 : 1;

        // Vérifier si le coup est valide en vérifiant les cases voisines
        if (isValidMove(board, x, y, player)) {
            // Mettre à jour la case jouée
            newboard[x][y] = player;

            // Mettre à jour les pions capturés
            capturePieces(newboard, x, y, player);
        }

        return newboard;
    }

    // Vérifie si le coup est valide
    public static boolean isValidMove(int[][] board, int x, int y, int player) {
        // Vérifie si la case est vide
        if (board[x][y] != 0) {
            return false;
        }

        // Vérifie s'il y a des pièces à capturer dans toutes les directions
        boolean isValid = false;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (isCaptureDirection(board, x, y, dx, dy, player)) {
                    isValid = true;
                }
            }
        }

        return isValid;
    }

    // Vérifie si des pions sont capturés dans une direction donnée
    public static boolean isCaptureDirection(int[][] board, int x, int y, int dx, int dy, int player) {
        int opponent = (player == 1) ? 2 : 1;
        int currentX = x + dx;
        int currentY = y + dy;

        // Avancer dans la direction tant qu'on trouve des pions de l'adversaire
        while (currentX >= 0 && currentX < 8 && currentY >= 0 && currentY < 8 && board[currentX][currentY] == opponent) {
            currentX += dx;
            currentY += dy;
        }

        // Si on atteint une pièce de notre couleur, alors il y a capturation
        if (currentX >= 0 && currentX < 8 && currentY >= 0 && currentY < 8 && board[currentX][currentY] == player) {
            return true;
        }

        return false;
    }

    // Capture les pions dans toutes les directions si possible
    public static void capturePieces(int[][] board, int x, int y, int player) {
        int opponent = (player == 1) ? 2 : 1;

        // Parcourir toutes les directions
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                // Vérifier si des pions peuvent être capturés dans cette direction
                if (isCaptureDirection(board, x, y, dx, dy, player)) {
                    int currentX = x + dx;
                    int currentY = y + dy;

                    // Capturer les pions jusqu'à atteindre une pièce de notre couleur
                    while (board[currentX][currentY] == opponent) {
                        board[currentX][currentY] = player;
                        currentX += dx;
                        currentY += dy;
                    }
                }
            }
        }
    }


    public static int[][] getNewBoardAfterMove(int[][] board, int x, int y, int playerColor){
        int[][] midboard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                midboard[i][j] = board[i][j];
            }
        }
        midboard[x][y] = playerColor;

        int [][] newboard = new int[8][8];
        newboard = updateCaseColor(midboard, x, y, playerColor);

        return newboard;
    }

}

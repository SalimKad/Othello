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


    /*
    public static int[][] updateCaseColor(int[][] board, int x, int y, int player) {
        int[][] newBoard = cloneBoard(board);

        if (isValidMove(board, x, y, player)) {
            newBoard[x][y] = player;
            capturePieces(newBoard, x, y, player);
        }

        return newBoard;
    }

    public static boolean isValidMove(int[][] board, int x, int y, int player) {
        if (board[x][y] != 0) {
            return false;
        }

        int opponent = (player == 1) ? 2 : 1;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (isCaptureDirection(board, x, y, dx, dy, player)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isCaptureDirection(int[][] board, int x, int y, int dx, int dy, int player) {
        int opponent = (player == 1) ? 2 : 1;
        int currentX = x + dx;
        int currentY = y + dy;

        boolean foundOpponent = false;
        while (currentX >= 0 && currentX < 8 && currentY >= 0 && currentY < 8) {
            if (board[currentX][currentY] == opponent) {
                foundOpponent = true;
            } else if (board[currentX][currentY] == player) {
                return foundOpponent;
            } else {
                return false;
            }
            currentX += dx;
            currentY += dy;
        }

        return false;
    }

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

    public static int[][] cloneBoard(int[][] board) {
        int[][] newBoard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, 8);
        }
        return newBoard;
    }

    public static void printBoard(int[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

     */

    public static int[][] updateCaseColor(int[][] board, int x, int y, int player) {
        int[][] newboard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, newboard[i], 0, 8);
        }

        // Changer la couleur des pions encadrés
        // Vérifier les lignes horizontales
        for (int i = x - 1; i >= 0 && board[i][y] != player && board[i][y] != 0; i--) {
            if (board[i][y] != player) {
                newboard[i][y] = player;
            }
        }
        for (int i = x + 1; i < 8 && board[i][y] != player && board[i][y] != 0; i++) {
            if (board[i][y] != player) {
                newboard[i][y] = player;
            }
        }

        // Vérifier les lignes verticales
        for (int j = y - 1; j >= 0 && board[x][j] != player && board[x][j] != 0; j--) {
            if (board[x][j] != player) {
                newboard[x][j] = player;
            }
        }
        for (int j = y + 1; j < 8 && board[x][j] != player && board[x][j] != 0; j++) {
            if (board[x][j] != player) {
                newboard[x][j] = player;
            }
        }

        // Vérifier les diagonales
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];
            int i = x + dx;
            int j = y + dy;
            while (i >= 0 && i < 8 && j >= 0 && j < 8 && board[i][j] != player && board[i][j] != 0) {
                if (board[i][j] != player) {
                    newboard[i][j] = player;
                }
                i += dx;
                j += dy;
            }
        }

        // Mettre à jour la nouvelle position du pion joué
        newboard[x][y] = player;

        return newboard;
    }


    public static int[][] getNewBoardAfterMove(int[][] board, int x, int y, int playerColor){
        int[][] midboard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                midboard[i][j] = board[i][j];
            }
        }

        int [][] newboard = new int[8][8];
        newboard = updateCaseColor(midboard, x, y, playerColor);

        return newboard;
    }

}

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
        //System.out.println("Mouvements possibles pour le joueur " + player + ": " + result); // Ajout pour débogage
        return result;
    }

    public static boolean hasAnyMoves(int[][] board, int player){
        return getAllPossibleMoves(board,player).size() > 0;
    }

    public static int[][] updateCaseColor(int[][] board, int x, int y, int player) {
        int[][] newboard = new int[8][8];
        // Copier le plateau existant dans le nouveau plateau
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newboard[i][j] = board[i][j];
            }
        }
        // Placer le nouveau pion sur le plateau
        newboard[x][y] = player;

        // Changer la couleur des pions encadrés par deux pions adverses
        int opponent = (player == 1) ? 2 : 1; // Déterminer la couleur de l'adversaire
        // Pour chaque direction possible (horizontale, verticale, diagonale)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // Ignorer la position actuelle du pion
                int i = x + dx;
                int j = y + dy;
                // Tant que nous sommes dans les limites du plateau et que nous rencontrons des pions adverses
                while (i >= 0 && i < 8 && j >= 0 && j < 8 && board[i][j] == opponent) {
                    i += dx; // Avancer dans la même direction
                    j += dy;
                }
                // Si nous avons trouvé une ligne de pions adverses encadrés par les nôtres
                if (i >= 0 && i < 8 && j >= 0 && j < 8 && board[i][j] == player) {
                    // Retourner les pions adverses entre les deux pions du joueur actuel
                    int flipI = x + dx;
                    int flipJ = y + dy;
                    while (flipI != i || flipJ != j) {
                        newboard[flipI][flipJ] = player;
                        flipI += dx;
                        flipJ += dy;
                    }
                }
            }
        }

        return newboard;
    }

    public static boolean isMoveValid(int[][] board, int x, int y, int player) {
        if (board[x][y] != 0) {
            // La case est déjà occupée
            return false;
        }

        int opponent = (player == 1) ? 2 : 1;

        // Vérifier dans toutes les directions si le mouvement capture des pions adverses
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // Ignorer la position actuelle du pion
                int i = x + dx;
                int j = y + dy;
                boolean foundOpponent = false;
                // Tant que nous sommes dans les limites du plateau et que nous rencontrons des pions adverses
                while (i >= 0 && i < 8 && j >= 0 && j < 8 && board[i][j] == opponent) {
                    i += dx; // Avancer dans la même direction
                    j += dy;
                    foundOpponent = true;
                }
                // Si nous avons trouvé une ligne de pions adverses encadrés par les nôtres
                if (i >= 0 && i < 8 && j >= 0 && j < 8 && board[i][j] == player && foundOpponent) {
                    return true;
                }
            }
        }

        return false;
    }



    public static int[][] getNewBoardAfterMove(int[][] board, int x, int y, int playerColor){
        int[][] newboard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newboard[i][j] = board[i][j];
            }
        }
        if(isMoveValid(board,x,y,playerColor)) {
            newboard = updateCaseColor(newboard, x, y, playerColor);
        }
        else {
            System.out.println("Invalid move");
        }

        return newboard;
    }

    public static boolean isGameFinished(int[][] board){
        return !(hasAnyMoves(board,1) || hasAnyMoves(board,2));
    }

    /*
    public static int getTotalStoneCount(int[][] board){
        int c = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] != 0) c++;
            }
        }
        return c;
    }*/

    public static int getPlayerStoneCount(int[][] board, int player){
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] == player) score++;
            }
        }
        return score;
    }

    public static int getWinner(int[][] board){
        if(!isGameFinished(board))
            return -1;
        else{
            int p1s = getPlayerStoneCount(board,1);
            int p2s = getPlayerStoneCount(board,2);

            if(p1s == p2s){
                return 0;
            }else if(p1s > p2s){
                return 1;
            }else{
                return 2;
            }
        }
    }


}

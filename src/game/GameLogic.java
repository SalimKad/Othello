package game;
//logique du jeu, qui a gagné, est-ce que le jeu est fini...
//board helper

import java.awt.*;

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

}

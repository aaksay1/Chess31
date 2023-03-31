package chess;

import pieces.bishop;
import pieces.king;
import pieces.knight;
import pieces.pawn;
import pieces.piece;
import pieces.queen;
import pieces.rook;

/**
 * Create the board object with functionality of drawBoard 
 * and createBoard
 *
 * @author Akin Aksay
 */

public class board {

    public void drawBoard(piece[][] board) {

        System.out.println();

        String[][] b = new String[9][9];

        boolean white = true;

        for(int i = 0; i < 8; i++) {

            for(int j = 0; j < 8; j++) {
                if(white) { 
                    b[i][j] = "  ";
                    white = false;
                } else {
                    b[i][j] = "##";
                    white = true;
                }
            }

           white = !(white);
        }

        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if(board[x][y] != null) {
                    b[x][y] = board[x][y].getSign();
                }
            }
        }
        b[0][8] = " a";
        b[1][8] = " b";
        b[2][8] = " c";
        b[3][8] = " d";
        b[4][8] = " e";
        b[5][8] = " f";
        b[6][8] = " g";
        b[7][8] = " h";

        b[8][0] = "8";
        b[8][1] = "7";
        b[8][2] = "6";
        b[8][3] = "5";
        b[8][4] = "4";
        b[8][5] = "3";
        b[8][6] = "2";
        b[8][7] = "1";
        b[8][8] = "  ";


        for(int y = 0; y < 9; y++) {
            for(int x = 0; x < 9; x++) {
                System.out.print(b[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println();

    }

    public void createBoard(piece[][] board) {

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }

        board[0][1] = new pawn("bp", false, 0, 1, false);
        board[1][1] = new pawn("bp", false, 1, 1, false);
        board[2][1] = new pawn("bp", false, 2, 1, false);
        board[3][1] = new pawn("bp", false, 3, 1, false);
        board[4][1] = new pawn("bp", false, 4, 1, false);
        board[5][1] = new pawn("bp", false, 5, 1, false);
        board[6][1] = new pawn("bp", false, 6, 1, false);
        board[7][1] = new pawn("bp", false, 7, 1, false);

        board[0][0] = new rook("bR", false, 0, 0, false);
		board[1][0] = new knight("bN", false, 1, 0, false);
		board[2][0] = new bishop("bB", false, 2, 0, false);

		board[3][0] = new queen("bQ", false, 3, 0, false);
		board[4][0] = new king("bK", false, 4, 0, false);

		board[5][0] = new bishop("bB", false, 5, 0, false);
		board[6][0] = new knight("bN", false, 6, 0, false);
		board[7][0] = new rook("bR", false, 7, 0, false);

        board[0][6] = new pawn("wp", false, 0, 6, false);
        board[1][6] = new pawn("wp", false, 1, 6, false);
        board[2][6] = new pawn("wp", false, 2, 6, false);
        board[3][6] = new pawn("wp", false, 3, 6, false);
        board[4][6] = new pawn("wp", false, 4, 6, false);
        board[5][6] = new pawn("wp", false, 5, 6, false);
        board[6][6] = new pawn("wp", false, 6, 6, false);
        board[7][6] = new pawn("wp", false, 7, 6, false);

        board[0][7] = new rook("wR", false, 0, 7, false);
        board[1][7] = new knight("wN", false, 1, 7, false);
        board[2][7] = new bishop("wB", false, 2, 7, false);
        board[3][7] = new queen("wQ", false, 3, 7, false);
        board[4][7] = new king("wK", false, 4, 7, false);
        board[5][7] = new bishop("wB", false, 5, 7, false);
        board[6][7] = new knight("wN", false, 6, 7, false);
        board[7][7] = new rook("wR", false, 7, 7, false);

    }  

}

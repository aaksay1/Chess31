package chess;

import java.io.BufferedReader;
//import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pieces.bishop;
import pieces.knight;
import pieces.pawn;
import pieces.piece;
import pieces.queen;
import pieces.rook;

/**
 * Where the game is played and where all the components
 * of the program come together
 *
 * @author Akin Aksay
 */
public class Chess {

    static boolean white = true;

    static piece[][] b = new piece[8][8];

    static int oldX;
    static int oldY;

    static int newX;
    static int newY; 
    
    static piece prevPiece = null;

    // this is the main method of the program
   public static void main(String[] args) throws IOException {

        board board = new board();
        boolean gameIsPlayed = true;

        board.createBoard(b);
       
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new FileReader("src/trial.txt"));

		String input = null;

        int numberOfMoves = 1;

        boolean illegalMove = false;

        while(gameIsPlayed) {

            if(!illegalMove){
                board.drawBoard(b);
            }

            illegalMove = false;
            
            if(white) {
                System.out.print("White's move: ");
            } else {
                System.out.print("Black's move: ");
            } 

            input = br.readLine(); // input    

            if(input.equals("resign")) {

                if(white) { // white resigns
                    
                    System.out.println("Black wins");
                } else { // black resigns
                    
                    System.out.println("White wins");
                }
                gameIsPlayed = false;
                break;
            }

            if(input.length() > 4) {
                setCoordinates(input);
            } else {
                illegalMove = true;
            }

            piece p = b[oldX][oldY];

            if(p == null || !p.isValidMove(oldX, oldY, newX, newY, b, prevPiece) || putsKingInCheck(p, oldX, oldY, newX, newY)  ||!isRightColor(white, p)) { 
                
                // checks if the said piece can perform the move
                System.out.println("Illegal move, try again");
                illegalMove = true;
                    
            } 

            if((!illegalMove) && (input.length() > 5) && (input.substring(input.length() - 5).equals("draw?"))) {
                
                if(!white) {
                    System.out.println("White's move: draw");
                } else {
                    System.out.println("Black's move: draw");
                } 

                //BufferedReader brd = new BufferedReader(new InputStreamReader(System.in));
                //brd.readLine();
                gameIsPlayed = false;
                break;
                
            }
            
            
            if(!illegalMove && gameIsPlayed) { 

                if(p.getSign().charAt(1) == 'p' && (newY == 0 || newY == 7)) { // check for pawn promotion
                    char c = p.getSign().charAt(0); // get color
                    if(input.length() == 7) {

                        if(input.charAt(6) == 'R') { // rook
                            p = new rook(c+"R", true, newX, newY, false);

                        } else if(input.charAt(6) == 'B') { // bishop
                            p = new bishop(c+"B", true, newX, newY, false);

                        } else if(input.charAt(6) == 'N') { // knight
                            p = new knight(c+"N", true, newX, newY, false);
                        

                        } else if(input.charAt(6) == 'p') { // pawn
                            p = new pawn(c+"p", true, newX, newY, false);

                        } else {
                            p = new queen(c+"Q", true, newX, newY, false);
                        }
                        
                    } else {
                        p = new queen(c+"Q", true, newX, newY, false);
                    }
                }
                numberOfMoves++;
                p.setHasMoved(true);

                b[newX][newY] = p;
                b[oldX][oldY] = null;

                p.setX(newX);
                p.setY(newY);

                if(checkMate(p, b)) {

                    System.out.println();
                    board.drawBoard(b);
                    System.out.println("Checkmate");
                    if(white) {
                        System.out.println("White wins");
                        
                    } else {
                        System.out.println("Black wins");
                  
                    }
                    
                    gameIsPlayed = false;
                   
                }

                if(gameIsPlayed && check(p,b)) {
                    System.out.println("Check");
                }

            }

            if(numberOfMoves % 2 == 0) {
                white = false;
            } else {
                white = true;
            }

            prevPiece = p;

        }
    }

    // this method checks if the piece is the right color meaning that it is the current color
    public static boolean isRightColor(boolean white, piece piece) {
        return ((white && piece.getSign().charAt(0) == 'w') || (!white && piece.getSign().charAt(0) == 'b'));
      
    }

    // this method checks if a checkmate is made
    public static boolean checkMate(piece p, piece[][] board) {

        if(!check(p, board)) {
            return false;
        }

        int kX = 0;
        int kY = 0;

        // gets opposite colored kings location
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] != null) {
                    piece piece = board[i][j];
                    if(piece.getSign().charAt(1) == 'K' && p.getSign().charAt(0) != piece.getSign().charAt(0)) {
                        kX = i;
                        kY = j;
                        break;
                    }
                } 
            }
        }


        piece[][] hypotheticalBoard = new piece[8][8];

        // gets the hypothetical board
        for(int i = 0; i < b.length; i++){
            for(int j = 0; j < b[i].length; j++) {
                hypotheticalBoard[i][j] = b[i][j];
            }
        }

        if(canDefend(hypotheticalBoard[kX][kY], hypotheticalBoard)) {
            return false;
        }

        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {

                if((board[kX][kY]).isValidMove(kX, kY, x, y, board, p)) {
                    hypotheticalBoard[x][y] = board[kX][kY];
                    hypotheticalBoard[kX][kY] = null;

                    if(!check(hypotheticalBoard[x][y], hypotheticalBoard) || canDefend(hypotheticalBoard[x][y], hypotheticalBoard)) {
                        return false;
                    } 
                }
            }
        }

        return true;

    }

    public static boolean canDefend(piece king, piece[][] board) {
    // checks if the king that is in check can be defended by another piece
    
        int kX = king.getX();
        int kY = king.getY();

        char color = king.getSign().charAt(0);

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] != null && board[i][j].getSign().charAt(0) != color){
                    piece piece = board[i][j];
                    if(piece.isValidMove(piece.getX(), piece.getY(), kX, kY, board, prevPiece)){

                        for(int x = 0; x < 8; x++) {
                            for(int y = 0; y < 8; y++) {

                                if(board[x][y] != null && board[x][y].getSign().charAt(0) == color) {
                                    piece defender = board[x][y];
                                    if(defender.isValidMove(x, y, i, j, board, prevPiece)) {
                                        return true;
                                    }
                                }
                            }
                        }

                    }
                } 
                 
            }       
        }

        return false;


        
    }

    public static boolean check(piece p, piece[][] board) {

        int kX = 0;
        int kY = 0;
    
        // Find the king of the opposite color as the piece p
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] != null) {
                    piece piece = board[i][j];
                    if(piece.getSign().charAt(1) == 'K' && p.getSign().charAt(0) != piece.getSign().charAt(0)) {
                        kX = i;
                        kY = j;
                        break;
                    }
                } 
            }
        }

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] != null){
                    piece piece = board[i][j];
                    if((piece.getSign().charAt(0) == p.getSign().charAt(0)) && piece.isValidMove(i, j, kX, kY, board, prevPiece)){

                       return true;
                    }
                } 
                 
            }       
        }
        return false;
        
    }

    public static boolean putsKingInCheck(piece p, int oldX, int oldY, int newX, int newY) {
    
       piece[][] hypotheticalBoard = new piece[8][8];

        for(int i = 0; i < b.length; i++){
            for(int j = 0; j < b[i].length; j++) {
                hypotheticalBoard[i][j] = b[i][j];
            }
        }
    
        // Update the board with the hypothetical move
        hypotheticalBoard[newX][newY] = hypotheticalBoard[oldX][oldY];
        hypotheticalBoard[oldX][oldY] = null;

        int kX = 0;
        int kY = 0;
    
        // Find the king of the same color as the piece p
        for(int i = 0; i < hypotheticalBoard.length; i++) {
            for(int j = 0; j < hypotheticalBoard[i].length; j++) {
                if(hypotheticalBoard[i][j] != null) {
                    if(hypotheticalBoard[i][j].getSign().charAt(1) == 'K' && p.getSign().charAt(0) == hypotheticalBoard[i][j].getSign().charAt(0)) {
                        kX = i;
                        kY = j;
                        break;
                    }
                } 
            }
        }

        // Check if the king is under attack
        for(int i = 0; i < hypotheticalBoard.length; i++) {
            for(int j = 0; j < hypotheticalBoard[i].length; j++) {
                if(hypotheticalBoard[i][j] != null && hypotheticalBoard[i][j].getSign().charAt(0) != p.getSign().charAt(0)){

                    if(hypotheticalBoard[i][j].isValidMove(i, j, kX, kY, hypotheticalBoard, prevPiece)){
                        
                       return true;
                    }
                } 
                 
            }
        }

        return false;
        
    }

    public static void setCoordinates(String input) { // sets the coordinates from the input (seems to work)

        oldX = input.toLowerCase().charAt(0) - ('a');
        oldY = 7 - (input.toLowerCase().charAt(1) - ('1'));      

        newX = input.toLowerCase().charAt(3) - ('a');
        newY = 7 - (input.toLowerCase().charAt(4) - ('1'));  
    }

}

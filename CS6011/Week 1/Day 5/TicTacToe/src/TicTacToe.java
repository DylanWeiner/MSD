import java.util.ArrayList;
import java.util.Scanner;

public class TicTacToe {
    // You can create a matrix, which is an array of an array


    static int playerTurn = 1;

//    char board[3][3];
    private static ArrayList<Character> board = new ArrayList<Character>();

    private static boolean playerHasWon() {
        return false;
    }
        // Exercise to the reader/
    private static void createBoard() {
        for(int i = 0; i < 9; i++) {
            board.add('_');
        }
    }
    private static void showBoard() {
        for(int i = 0; i < 3; i++) {
            if(i>=1)
                System.out.println("----------");
            System.out.println(" " + board.get(0+(i*3)) + " | " + board.get(1+(i*3)) + " | " + board.get(2+(i*3)));
        }
    }

    static void main() {
        int square = 0;
        boolean gameOver = false;

        createBoard();

        while(!gameOver) {
            showBoard();

            System.out.println("Player " + playerTurn + ", choose a square: ");

            Scanner sc  = new Scanner(System.in);
            square = sc.nextInt();

            while(board.get(square) != '_') {
              System.out.println("Invalid square");
              square = sc.nextInt();
             }
        if( playerTurn == 1 ) {
            board.set(square, 'X');
            playerTurn = 2;

        }
        else {
            board.set(square, 'O');
            playerTurn = 1;
        }

        if(playerHasWon()){
            System.out.println("Tic-Tac-Toe, three in a row, Player " + playerTurn + "!");
            gameOver = true;
        }
    }
    }
}

package tictactoe;

import java.util.Scanner;

public class TicTacToeWebApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Player 1 username (X): ");
        String p1 = sc.nextLine();

        System.out.print("Enter Player 2 username (O): ");
        String p2 = sc.nextLine();

        char[][] board = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
        };

        char current = 'X';
        String currentPlayer = p1;

        System.out.println("\nGame Start!");
        printBoard(board);

        while (true) {
            System.out.println(currentPlayer + " (" + current + ") — enter your move.");

            int r, c;

            while (true) {
                System.out.print("Row (0-2): ");
                r = sc.nextInt();
                System.out.print("Col (0-2): ");
                c = sc.nextInt();

                if (r >= 0 && r < 3 && c >= 0 && c < 3 && board[r][c] == ' ') {
                    break;
                }

                System.out.println("Invalid move. Try again.");
            }

            board[r][c] = current;
            printBoard(board);

            if (checkWin(board, current)) {
                System.out.println("🎉 " + currentPlayer + " (" + current + ") wins!");
                break;
            }

            if (isDraw(board)) {
                System.out.println("It's a draw!");
                break;
            }

            // Switch player
            if (current == 'X') {
                current = 'O';
                currentPlayer = p2;
            } else {
                current = 'X';
                currentPlayer = p1;
            }
        }

        sc.close();
    }

    static void printBoard(char[][] b) {
        System.out.println("-------------");
        for (int r = 0; r < 3; r++) {
            System.out.print("| ");
            for (int c = 0; c < 3; c++) {
                System.out.print(b[r][c] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
        System.out.println();
    }

    static boolean checkWin(char[][] b, char p) {
        // Rows
        for (int r = 0; r < 3; r++)
            if (b[r][0] == p && b[r][1] == p && b[r][2] == p)
                return true;

        // Columns
        for (int c = 0; c < 3; c++)
            if (b[0][c] == p && b[1][c] == p && b[2][c] == p)
                return true;

        // Diagonals
        if (b[0][0] == p && b[1][1] == p && b[2][2] == p) return true;
        if (b[0][2] == p && b[1][1] == p && b[2][0] == p) return true;

        return false;
    }

    static boolean isDraw(char[][] b) {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (b[r][c] == ' ')
                    return false;
        return true;
    }
}

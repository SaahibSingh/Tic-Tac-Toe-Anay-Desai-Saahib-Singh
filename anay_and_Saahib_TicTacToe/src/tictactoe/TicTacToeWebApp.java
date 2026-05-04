package tictactoe;

public class TicTacToeWebApp {

    public static void main(String[] args) {

        char[][] board = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
        };

        char current = 'X';

        printBoard(board);

        // Simulated moves (you can change these)
        makeMove(board, 0, 0, current); current = switchPlayer(current);
        makeMove(board, 1, 1, current); current = switchPlayer(current);
        makeMove(board, 0, 1, current); current = switchPlayer(current);
        makeMove(board, 2, 2, current); current = switchPlayer(current);
        makeMove(board, 0, 2, current); // X wins here

        printBoard(board);

        if (checkWin(board, 'X')) {
            System.out.println("X wins!");
        } else if (checkWin(board, 'O')) {
            System.out.println("O wins!");
        } else if (isDraw(board)) {
            System.out.println("It's a draw!");
        } else {
            System.out.println("Game still in progress.");
        }
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

    static void makeMove(char[][] b, int r, int c, char p) {
        if (b[r][c] == ' ') {
            b[r][c] = p;
        }
        printBoard(b);
    }

    static char switchPlayer(char p) {
        return (p == 'X') ? 'O' : 'X';
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

    }
}

package tictactoe;

public class TicTacToeWebApp {
    public static void main(String[] args) {
        System.out.println("Backend running (no server).");
        System.out.println("You can test game logic here.");

        // Example test:
        Board board = new Board();
        GameLogic logic = new GameLogic();

        board.setCell(0, 0, 'X');
        board.setCell(1, 1, 'X');
        board.setCell(2, 2, 'X');

        if (logic.checkWin(board, 'X')) {
            System.out.println("X wins!");
        }
    }
}

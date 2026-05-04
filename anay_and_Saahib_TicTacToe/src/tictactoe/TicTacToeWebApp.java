package tictactoe;

public class TicTacToeWebApp {
    public static void main(String[] args) {

        // Example test:
        Board board = new Board("board.csv");
        GameLogic logic = new GameLogic();

        board.setCell(0, 0, 'X');
        board.setCell(1, 1, 'X');
        board.setCell(2, 2, 'X');

        if (logic.checkWin(board, 'X')) {
            System.out.println("X wins!");
        }
    }
}

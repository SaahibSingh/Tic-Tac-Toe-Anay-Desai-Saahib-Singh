package tictactoe;

/**Begin Phase III - 10 points
  roughly 3 points each**/
public class GameLogic {
  package tictactoe;

public class GameLogic {
    //Saahib
    public boolean checkWin(Board board, char player) {
        //Common variables to be used within the method
        char first, second, third;
        int r, c;
       
        // rows
        for (r = 0; r < grid.length; r++) {
            first = board.getCell(r, 0);
            second = board.getCell(r, 1);
            third = board.getCell(r, 2);
            if (first == player && second == player && third == player) {
                return true;
            }
        }

        // columns
        for (c = 0; c < grid[0].length; c++) {
            first = board.getCell(0, c);
            second = board.getCell(1, c);
            third = board.getCell(2, c);
            if (first == player && second == player && third == player) {
                return true;
            }
        }

        // main diagonal (top-left to bottom-right)
        if (board.getCell(0, 0) == player &&
            board.getCell(1, 1) == player &&
            board.getCell(2, 2) == player) {
            return true;
        }

        // anti-diagonal (top-right to bottom-left)
        if (board.getCell(0, 2) == player &&
            board.getCell(1, 1) == player &&
            board.getCell(2, 2) == player) {
            return true;
        }

        return false;
    }
}

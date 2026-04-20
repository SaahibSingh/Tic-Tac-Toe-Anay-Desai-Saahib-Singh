package tictactoe;

/**Begin Phase III - 10 points
  roughly 3 points each**/

public class GameLogic {
    //Common Variables to be used - NOT INSTANCE VARIABLES
    int r, c;
    char first, second, third;
  
    //Saahib
    public boolean checkWin(Board board, char player) {
        char[][] grid = board.getGrid();
      
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

  //Anay
  public boolean isDraw(Board board) {
      char[][] grid = board.getGrid();
      for (r = 0; r < grid.length; r++) {
        for (c = 0; c < grid[0].length; c++) {
          if (board.getCell(r, c) == 'E')
            return false;
          }
      }
    
      return !(checkWin(board, 'X') || checkWin(board, 'O'));
  }

  public boolean isGameOver(Board board) { return checkWin(board, 'X') || checkWin(board, 'O') || isDraw(board); } //Saahib 

  /**Begin phase 4
  Two methods - 5 points each**/
  //Saahib
  public char getCurrentPlayer(Board board) {
    char[][] grid = board.getGrid();
    int xCount = 0, oCount = 0;
    for (r = 0; r <= grid.length; r++) {
      for (c = 0; c < grid[r].length; c++) {
        if (board.getCell(r, c) == 'X') xCount++;
        if (board.getCell(r, c) == 'O') oCount++;
      }
    }
    
    return xCount == oCount ? 'X' : 'O';
  }
  
  public boolean makeMove(Board board, int row, int col)
  {
	  if(board.isValidBoardFile() && row >= 0 && row <= 2 && col >= 0 && col <= 2)
	  {
		  char player = getCurrentPlayer(board);
		  if(board.getCell(row, col) == 'E')
		  {
			board.setCell(row, col, player);
			return true;  
		  }
	  }
	  return false;
		
  }
}

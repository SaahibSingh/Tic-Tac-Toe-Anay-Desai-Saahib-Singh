package tictactoe;

// Imports
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Board
{
    private char[][] grid;   // holds game play data in an instance variable
    private String filename; // holds game play data in a CSV file

    // non-default constructor - [5 points]
    public Board(String filename)
    {
        this.filename = filename;              // set the file name
        if (isValidBoardFile())               // if the board is valid
        {
            this.grid = new char[3][3];       // create the 3x3 grid
            loadBoardFromFile();              // and load the board from the file
        }
        else
        {
            // if file is invalid or missing, start with empty board
            this.grid = new char[3][3];
            clearBoard();
        }
    }

    // loads the grid with the file contents - [5 points]
    public void loadBoardFromFile()
    {
        Scanner scanner = null;
        try
        {
            File file = new File("src/tictactoe/" + this.filename);
            scanner = new Scanner(file);
            int row = 0;
            while (scanner.hasNextLine() && row < 3)
            {
                String line = scanner.nextLine().trim(); // e.g. "E,X,O"
                String[] parts = line.split(",");

                // defensive: ensure exactly 3 entries
                if (parts.length != 3)
                    throw new IllegalArgumentException("Invalid row length in file");

                for (int col = 0; col < 3; col++)
                {
                    grid[row][col] = parts[col].charAt(0); // 'E', 'X', or 'O'
                }
                row++;
            }
        }
        catch (Exception error)
        {
            error.printStackTrace();
        }
        finally
        {
            if (scanner != null) scanner.close();
        }
    }

    // valid if it resembles a 3x3 board that contains only E, X, O
    public boolean isValidBoardFile()
    {
        Scanner scanner = null;
        try
        {
            File file = new File("src/tictactoe/" + this.filename);
            if (!file.exists()) return false;

            scanner = new Scanner(file);
            int xCount = 0, oCount = 0;
            int rowCount = 0;

            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine().trim();
                rowCount++;

                // must look like "[EXO],[EXO],[EXO]"
                if (!line.matches("[EXO],[EXO],[EXO]"))
                {
                    return false;
                }

                String[] lineArray = line.split(",");
                for (String element : lineArray)
                {
                    if (element.equals("X")) xCount++;
                    if (element.equals("O")) oCount++;
                }
            }

            // must be exactly 3 rows
            if (rowCount != 3) return false;

            // Tic-Tac-Toe turn rule: X goes first
            return (xCount == oCount || xCount == oCount + 1);
        }
        catch (Exception error)
        {
            error.printStackTrace();
            return false;
        }
        finally
        {
            if (scanner != null) scanner.close();
        }
    }

    // saves the grid to the file in the proper format (CSV)
    public void saveBoardToFile()
    {
        FileWriter writer = null;
        try
        {
            File file = new File("src/tictactoe/" + this.filename);
            writer = new FileWriter(file);
            StringBuilder boardContents = new StringBuilder();

            for (int row = 0; row < grid.length; row++)
            {
                for (int col = 0; col < grid[0].length; col++)
                {
                    boardContents.append(grid[row][col]);
                    if (col < 2) boardContents.append(",");
                }
                if (row < 2) boardContents.append("\n");
            }

            writer.write(boardContents.toString());
        }
        catch (Exception error)
        {
            error.printStackTrace();
        }
        finally
        {
            try { if (writer != null) writer.close(); } catch (Exception e) {}
        }
    }

    /***These are the methods used to test those above***/

    // prints the current grid
    public void printGrid()
    {
        if (this.grid != null)
        {
            for (int row = 0; row < grid.length; row++)
            {
                for (int col = 0; col < grid[0].length; col++)
                    System.out.print(grid[row][col] + " ");
                System.out.println();
            }
        }
    }

    // create a random *valid* board
    public void createRandomBoard()
    {
        if (this.grid == null) return;

        // start with all E
        clearBoard(); // this also saves to file

        int moves = (int)(Math.random() * 10); // 0–9 moves
        char current = 'X';

        for (int m = 0; m < moves; m++)
        {
            int row, col;
            // find a random empty cell
            do {
                row = (int)(Math.random() * 3);
                col = (int)(Math.random() * 3);
            } while (grid[row][col] != 'E');

            grid[row][col] = current;
            // alternate player
            current = (current == 'X') ? 'O' : 'X';
        }

        this.saveBoardToFile();
    }

    // clears the grid by placing E in every cell
    public void clearBoard()
    {
        char[][] clearedBoard = {
                {'E','E','E'},
                {'E','E','E'},
                {'E','E','E'}
        };
        this.grid = clearedBoard;
        this.saveBoardToFile();
    }

    public static void main(String args[])
    {
        Board b = new Board("board.csv");
        System.out.println(b.isValidBoardFile());
        b.createRandomBoard();
        b.printGrid();
        System.out.println(b.isValidBoardFile());
        /*
        b.saveBoardToFile();
        b.loadBoardFromFile();
        System.out.println();
        b.printGrid();
        */
    }
}

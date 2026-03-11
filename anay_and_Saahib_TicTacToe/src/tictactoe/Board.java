package tictactoe;

//Imports
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Board 
{
    private char[][] grid; //holds game play data in an instance variable
    private String filename; //holds game play data in a CSV file
        
    //non-default constructor - [5 points]
    public Board(String filename) 
    {
    	this.filename = filename;
    	if(isValidBoardFile())
    	{
    		this.grid = new char[3][3];
    		loadBoardFromFile();
    	}
       //set the file name
       //if the board is valid then create the 3x3 grid
       //and load the board from the file
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
            	String line = scanner.nextLine().trim();   // e.g. "E,X,O"
            	String[] parts = line.split(",");

            	// assume isValidBoardFile already checked format, so parts.length == 3
            	for (int col = 0; col < 3; col++) grid[row][col] = parts[col].charAt(0);  // 'E', 'X', or 'O'
			}
            row++;
			scanner.close();
        }
    	catch (Exception error)
    	{
        	error.printStackTrace();
    	}
	}
 
    //valid if it resembles a 3x3 board that contains only E, X, O
    public boolean isValidBoardFile() 
    {
    	try
    	{
    		File file = new File("src/tictactoe/" + this.filename);
    		Scanner scanner = new Scanner(file);
    		int xCount = 0, oCount = 0;
    		while(scanner.hasNextLine())
    		{
    			String line = scanner.nextLine().trim();
    			if(!line.matches("[EXO],[EXO],[EXO]"))
    			{
    				scanner.close();
    				return false;
    			}
    			String[] lineArray = line.split(",");
                for (String element : lineArray) 
                {
                    if (element.equals("X")) xCount++;
                    if (element.equals("O")) oCount++;
                }    
    		}
    		scanner.close();
    		return xCount == oCount || xCount == oCount + 1;
    	}
    	catch(Exception error)
    	{
    		error.printStackTrace();
    		return true;
    	}
    }

    //saves the grid to the file in the proper format (CSV)
    public void saveBoardToFile() 
    { 
    	try
        {
            File file = new File("src/tictactoe/" + this.filename);
            FileWriter writer = new FileWriter(file);
            String boardContents = "";
            for(int row = 0; row < grid.length; row++) 
            {
                for (int col = 0; col < grid[0].length; col++)
                {
                    boardContents += (grid[row][col] + col < 2 ? "," : ""); //Ternary operator
                    /**
                    if (col < 2) boardContents += grid[row][col] + ",";
                    else boardContents += grid[row][col];
                    **/
                }
                if (row < 2) boardContents += "\n";
            }
            writer.write(boardContents);
            writer.close();
        }
        catch (Exception error) 
        {
            error.printStackTrace();
        }
    }
    
    /***These are the methods used to test those above***/
    public void printGrid() //prints the current grid
    {
    	if(this.grid != null)
    	{
    		for(int row = 0; row < grid.length; row++) 
    		{
    			for(int col = 0; col < grid[0].length; col++)
    				System.out.print(grid[row][col] + " ");
    			System.out.println();
    		}
    	}
    	
    } 
    
    public void createRandomBoard() //create a random board
    { 
    	char[] options = {'E', 'X', 'O'};
    	if(this.grid != null)
    	{
	      for (int row = 0; row < grid.length; row++) 
	        {
	            for (int col = 0; col < grid[0].length; col++) 
	            {
	                int index = (int)(Math.random() * options.length);
	                grid[row][col] = options[index];
	            }
	        }
	        this.saveBoardToFile();
    	}
      
    } 
    
    public void clearBoard()  //clears the grid by placing E in every cell
    {  
    	char[][] clearedBoard = {{'E','E','E'},
    							 {'E','E','E'},
    							 {'E','E','E'}};
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

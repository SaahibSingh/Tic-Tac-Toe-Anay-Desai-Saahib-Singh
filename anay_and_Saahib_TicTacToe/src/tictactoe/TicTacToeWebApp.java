package tictactoe;
import java.util.Scanner; //Import
public class TicTacToeWebApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Tic Tac Toe ===");
        System.out.println("1. Console Version");
        System.out.println("2. Simple UI Version");
        System.out.print("Choose an option: ");
        int choice = sc.nextInt();
        sc.nextLine(); 
        if (choice == 1) ConsoleGame.start(sc);
        else if (choice == 2) SimpleUI.start();
        else System.out.println("Invalid choice.");
        sc.close();
    }
}

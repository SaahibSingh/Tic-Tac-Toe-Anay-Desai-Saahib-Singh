package tictactoe;

import java.util.Scanner; //Import
public class TicTacToeWebApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Tic Tac Toe ===");
        System.out.print("Enter Player 1 name (X): ");
        String p1 = sc.nextLine();

        System.out.print("Enter Player 2 name (O): ");
        String p2 = sc.nextLine();

        System.out.println("\nChoose game mode:");
        System.out.println("1. Console Version");
        System.out.println("2. Simple UI Version");
        System.out.print("Your choice: ");

        int choice = sc.nextInt();

        if (choice == 1) ConsoleGame.start(p1, p2, sc);
        else if (choice == 2) SimpleUI.start(p1, p2);
        else System.out.println("Invalid choice.");

        sc.close();
    }
}
package tictactoe;

//Imports
import javax.swing.*;
import java.awt.GridLayout;
import java.util.Scanner;

public class TicTacToeWebApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Tic Tac Toe ===");
        System.out.print("Enter Player 1 name (X): ");
        String p1 = sc.nextLine();

        System.out.print("Enter Player 2 name (O): ");
        String p2 = sc.nextLine();

        Scoreboard scoreboard = new Scoreboard();
        Leaderboard leaderboard = new Leaderboard();

        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setSize(380, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Sound.introChime();

        TitleScreen.show(frame, () -> {
            showMainMenu(frame, p1, p2, scoreboard, leaderboard, sc);
        });
    }

    private static void showMainMenu(JFrame frame, String p1, String p2,
                                     Scoreboard scoreboard, Leaderboard leaderboard,
                                     Scanner sc) {

        JPanel menu = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton consoleBtn = new JButton("Console Version");
        JButton uiBtn = new JButton("Simple UI Version");
        JButton scoreBtn = new JButton("Show Scoreboard");
        JButton leaderBtn = new JButton("Show Leaderboard");
        JButton exitBtn = new JButton("Exit");

        menu.add(consoleBtn);
        menu.add(uiBtn);
        menu.add(scoreBtn);
        menu.add(leaderBtn);
        menu.add(exitBtn);

        frame.setContentPane(menu);
        frame.revalidate();

        consoleBtn.addActionListener(e -> ConsoleGame.start(p1, p2, sc, scoreboard, leaderboard));
        uiBtn.addActionListener(e -> SimpleUI.start(p1, p2, scoreboard, leaderboard));
        scoreBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, scoreboard.getScoreboard(p1, p2)));
        leaderBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, leaderboard.getLeaderboard()));
        exitBtn.addActionListener(e -> System.exit(0));
    }
}
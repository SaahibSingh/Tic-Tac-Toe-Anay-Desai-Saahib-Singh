package tictactoe;

import javax.swing.*;
import java.awt.*;
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

    static void showMainMenu(JFrame frame, String p1, String p2,
                                     Scoreboard scoreboard, Leaderboard leaderboard,
                                     Scanner sc) {

        ModernUI.installGlobalFont();

        AnimatedGradientPanel bg = new AnimatedGradientPanel();
        bg.setLayout(new GridBagLayout());
        frame.setContentPane(bg);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Main Menu", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        gbc.gridy = 0;
        gbc.ipady = 20;
        bg.add(title, gbc);

        JButton consoleBtn = ModernUI.createPrimaryButton("Console Version");
        JButton uiBtn = ModernUI.createPrimaryButton("Simple UI Version");
        JButton scoreBtn = ModernUI.createSecondaryButton("Scoreboard");
        JButton leaderBtn = ModernUI.createSecondaryButton("Leaderboard");
        JButton exitBtn = ModernUI.createSecondaryButton("Exit");

        gbc.ipady = 0;

        gbc.gridy = 1; bg.add(consoleBtn, gbc);
        gbc.gridy = 2; bg.add(uiBtn, gbc);
        gbc.gridy = 3; bg.add(scoreBtn, gbc);
        gbc.gridy = 4; bg.add(leaderBtn, gbc);
        gbc.gridy = 5; bg.add(exitBtn, gbc);

        frame.revalidate();

        consoleBtn.addActionListener(e -> ConsoleGame.start(p1, p2, sc, scoreboard, leaderboard));

        uiBtn.addActionListener(e -> {
            PlayerIconSelector.show(frame, p1, p2, (p1Icon, p2Icon) -> {
                SimpleUI.start(p1, p2, scoreboard, leaderboard, p1Icon, p2Icon);
            });
        });

        scoreBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, scoreboard.getScoreboard(p1, p2)));
        leaderBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, leaderboard.getLeaderboard()));
        exitBtn.addActionListener(e -> System.exit(0));
    }
}
package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleUI {

    // ===== THEME COLORS =====
    static class Theme {
        Color headerBg, xColor, oColor, cellBg, cellHover, restartBg, backBg;
        boolean darkMode;

        Theme(Color headerBg, Color xColor, Color oColor,
              Color cellBg, Color cellHover, Color restartBg, Color backBg,
              boolean darkMode) {

            this.headerBg = headerBg;
            this.xColor = xColor;
            this.oColor = oColor;
            this.cellBg = cellBg;
            this.cellHover = cellHover;
            this.restartBg = restartBg;
            this.backBg = backBg;
            this.darkMode = darkMode;
        }
    }

    static Theme LIGHT = new Theme(
            new Color(230, 230, 230),
            new Color(0, 102, 204),
            new Color(204, 0, 0),
            Color.WHITE,
            new Color(240, 240, 240),
            new Color(144, 238, 144),
            new Color(255, 200, 200),
            false
    );

    static Theme DARK = new Theme(
            new Color(50, 50, 50),
            new Color(100, 149, 237),
            new Color(255, 99, 71),
            new Color(60, 60, 60),
            new Color(80, 80, 80),
            new Color(0, 150, 0),
            new Color(150, 0, 0),
            true
    );

    // ⭐ CREATIVE WIN MESSAGES
    private static String getCreativeWinMessage(String player, char symbol) {
        String[] messages = {
            player + " (" + symbol + ") just dominated the board!",
            "Victory! " + player + " (" + symbol + ") takes the crown!",
            player + " (" + symbol + ") wins with style!",
            "Unstoppable! " + player + " (" + symbol + ") claims the win!",
            "Boom! " + player + " (" + symbol + ") secures the victory!",
            "Legendary move! " + player + " (" + symbol + ") wins!"
        };

        return messages[(int)(Math.random() * messages.length)];
    }

    public static void start(String p1, String p2, Scoreboard scoreboard, Leaderboard leaderboard) {

        Theme[] currentTheme = {LIGHT};

        JFrame frame = new JFrame("Tic Tac Toe — " + p1 + " (X) vs " + p2 + " (O)");
        frame.setSize(380, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        AnimatedGradientPanel bg = new AnimatedGradientPanel();
        bg.setLayout(new BorderLayout());
        frame.setContentPane(bg);

        JLabel header = new JLabel(p1 + " (X) vs " + p2 + " (O) — Turn: X", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setOpaque(true);

        JButton[][] buttons = new JButton[3][3];
        char[][] board = new char[3][3];
        resetBoard(board);

        char[] currentPlayer = {'X'};
        boolean[] gameOver = {false};

        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
        gridPanel.setOpaque(false);

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        bottomPanel.setOpaque(false);

        JButton restartBtn = animatedButton("Restart Game");
        JButton backBtn = animatedButton("Back to Menu");
        JButton settingsBtn = animatedButton("Settings");

        bottomPanel.add(restartBtn);
        bottomPanel.add(backBtn);
        bottomPanel.add(settingsBtn);

        Runnable applyTheme = () -> {
            Theme t = currentTheme[0];

            header.setBackground(t.headerBg);

            restartBtn.setBackground(t.restartBg);
            backBtn.setBackground(t.backBg);
            settingsBtn.setBackground(new Color(200, 200, 255));

            bg.setDarkMode(t.darkMode);

            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++)
                    if (buttons[r][c] != null && buttons[r][c].getText().equals(" "))
                        buttons[r][c].setBackground(t.cellBg);
        };

        // ===== CREATE BOARD BUTTONS =====
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {

                JButton btn = new JButton(" ");
                btn.setFont(new Font("Arial", Font.BOLD, 48));
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

                int row = r, col = c;

                btn.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        if (btn.isEnabled() && btn.getText().equals(" "))
                            btn.setBackground(currentTheme[0].cellHover);
                    }
                    public void mouseExited(MouseEvent e) {
                        if (btn.isEnabled() && btn.getText().equals(" "))
                            btn.setBackground(currentTheme[0].cellBg);
                    }
                });

                btn.addActionListener(e -> {
                    if (gameOver[0]) return;
                    if (!btn.getText().equals(" ")) return;

                    Sound.beepClick();

                    btn.setText(String.valueOf(currentPlayer[0]));
                    btn.setForeground(currentPlayer[0] == 'X'
                            ? currentTheme[0].xColor
                            : currentTheme[0].oColor);

                    animateCellClick(btn);

                    board[row][col] = currentPlayer[0];

                    if (checkWin(board, currentPlayer[0])) {
                        gameOver[0] = true;

                        String winner = (currentPlayer[0] == 'X') ? p1 : p2;

                        // ⭐ CREATIVE WIN MESSAGE
                        String winMsg = getCreativeWinMessage(winner, currentPlayer[0]);

                        // ⭐ WIN JINGLE
                        Sound.winJingle();

                        // ⭐ GOLD FLASH
                        bg.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 6));
                        Timer glowTimer = new Timer(300, ev -> bg.setBorder(null));
                        glowTimer.setRepeats(false);
                        glowTimer.start();

                        JOptionPane.showMessageDialog(
                                frame,
                                "🎉 " + winMsg + "\n\n" + scoreboard.getScoreboard(p1, p2),
                                "Winner!",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        if (currentPlayer[0] == 'X') {
                            scoreboard.addWinForPlayer1();
                            leaderboard.recordWin(p1);
                            leaderboard.recordLoss(p2);
                        } else {
                            scoreboard.addWinForPlayer2();
                            leaderboard.recordWin(p2);
                            leaderboard.recordLoss(p1);
                        }

                        disableButtons(buttons);
                        return;
                    }

                    if (isDraw(board)) {
                        gameOver[0] = true;

                        Sound.beepDraw();

                        JOptionPane.showMessageDialog(
                                frame,
                                "It's a draw!\n" + scoreboard.getScoreboard(p1, p2),
                                "Draw",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        scoreboard.addDraw();
                        leaderboard.recordDraw(p1, p2);

                        disableButtons(buttons);
                        return;
                    }

                    currentPlayer[0] = (currentPlayer[0] == 'X') ? 'O' : 'X';
                    header.setText(p1 + " (X) vs " + p2 + " (O) — Turn: " + currentPlayer[0]);
                });

                buttons[r][c] = btn;
                gridPanel.add(btn);
            }
        }

        restartBtn.addActionListener(e -> {
            Sound.beepButton();
            resetBoard(board);
            gameOver[0] = false;
            currentPlayer[0] = 'X';
            header.setText(p1 + " (X) vs " + p2 + " (O) — Turn: X");

            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++) {
                    buttons[r][c].setText(" ");
                    buttons[r][c].setEnabled(true);
                    buttons[r][c].setBackground(currentTheme[0].cellBg);
                }
        });

        backBtn.addActionListener(e -> {
            Sound.beepButton();
            frame.dispose();
        });

        settingsBtn.addActionListener(e -> {
            Sound.beepButton();
            openSettingsMenu(frame, currentTheme, applyTheme, scoreboard, leaderboard, p1, p2);
        });

        applyTheme.run();

        frame.add(header, BorderLayout.NORTH);
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // ===== ANIMATIONS =====

    private static JButton animatedButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setFocusPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                btn.setSize(btn.getWidth() - 2, btn.getHeight() - 2);
            }
            public void mouseReleased(MouseEvent e) {
                btn.setSize(btn.getWidth() + 2, btn.getHeight() + 2);
            }
        });

        return btn;
    }

    private static void animateCellClick(JButton btn) {
        Timer timer = new Timer(50, null);
        final int[] step = {0};

        timer.addActionListener(e -> {
            float alpha = (float) Math.abs(Math.sin(step[0] * 0.4));
            btn.setBackground(new Color(255, 255, 180, (int) (alpha * 255)));

            step[0]++;
            if (step[0] > 6) {
                timer.stop();
                btn.setBackground(Color.WHITE);
            }
        });

        timer.start();
    }

    private static void animateWin(JButton[][] buttons, int[][] winCells) {
        Timer timer = new Timer(150, null);
        final int[] step = {0};

        timer.addActionListener(e -> {
            boolean on = (step[0] % 2 == 0);

            for (int[] cell : winCells) {
                int r = cell[0], c = cell[1];
                buttons[r][c].setBackground(on ? Color.YELLOW : Color.WHITE);
            }

            step[0]++;
            if (step[0] > 6) timer.stop();
        });

        timer.start();
    }

    private static int[][] getWinningCells(char[][] b, char p) {
        for (int r = 0; r < 3; r++)
            if (b[r][0] == p && b[r][1] == p && b[r][2] == p)
                return new int[][]{{r, 0}, {r, 1}, {r, 2}};

        for (int c = 0; c < 3; c++)
            if (b[0][c] == p && b[1][c] == p && b[2][c] == p)
                return new int[][]{{0, c}, {1, c}, {2, c}};

        if (b[0][0] == p && b[1][1] == p && b[2][2] == p)
            return new int[][]{{0, 0}, {1, 1}, {2, 2}};

        if (b[0][2] == p && b[1][1] == p && b[2][0] == p)
            return new int[][]{{0, 2}, {1, 1}, {2, 0}};

        return new int[0][0];
    }

    // ===== SETTINGS MENU =====

    private static void openSettingsMenu(JFrame parent, Theme[] currentTheme, Runnable applyTheme,
            Scoreboard scoreboard, Leaderboard leaderboard,
            String p1, String p2) {

			JDialog dialog = new JDialog(parent, "Settings", true);
			dialog.setSize(300, 300);
			dialog.setLayout(new GridLayout(4, 1, 10, 10));
			dialog.setLocationRelativeTo(parent);
			
			JButton themeBtn = animatedButton("Switch Theme");
			JButton scoreBtn = animatedButton("Show Scoreboard");
			JButton leaderBtn = animatedButton("Show Leaderboard");
			JButton backGameBtn = animatedButton("Back to Game");
			
			themeBtn.addActionListener(e -> {
				Sound.beepButton();
				currentTheme[0] = (currentTheme[0] == LIGHT) ? DARK : LIGHT;
				applyTheme.run();
			});
			
			scoreBtn.addActionListener(e -> {
				Sound.beepButton();
				JOptionPane.showMessageDialog(dialog, scoreboard.getScoreboard(p1, p2));
			});
			
			leaderBtn.addActionListener(e -> {
				Sound.beepButton();
				JOptionPane.showMessageDialog(dialog, leaderboard.getLeaderboard());
			});
			
			backGameBtn.addActionListener(e -> {
				Sound.beepButton();
				dialog.dispose();
			});
			
			dialog.add(themeBtn);
			dialog.add(scoreBtn);
			dialog.add(leaderBtn);
			dialog.add(backGameBtn);
			
			dialog.setVisible(true);
		}


    // ===== HELPERS =====

    private static void resetBoard(char[][] board) {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                board[r][c] = ' ';
    }

    private static void disableButtons(JButton[][] buttons) {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                buttons[r][c].setEnabled(false);
    }

    private static boolean checkWin(char[][] b, char p) {
        for (int r = 0; r < 3; r++)
            if (b[r][0] == p && b[r][1] == p && b[r][2] == p) return true;

        for (int c = 0; c < 3; c++)
            if (b[0][c] == p && b[1][c] == p && b[2][c] == p) return true;

        if (b[0][0] == p && b[1][1] == p && b[2][2] == p) return true;
        if (b[0][2] == p && b[1][1] == p && b[2][0] == p) return true;

        return false;
    }

    private static boolean isDraw(char[][] b) {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (b[r][c] == ' ') return false;
        return true;
    }
}
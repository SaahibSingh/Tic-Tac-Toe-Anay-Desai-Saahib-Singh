package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleUI {

    static class Theme {
        boolean darkMode;
        Theme(boolean darkMode) { this.darkMode = darkMode; }
    }

    static Theme LIGHT = new Theme(false);
    static Theme DARK = new Theme(true);

    static class Confetti {
        int x, y, size;
        Color color;
        float dx, dy;

        Confetti(int w, int h) {
            x = (int)(Math.random() * w);
            y = -20;
            size = 6 + (int)(Math.random() * 6);
            color = new Color(
                    (int)(Math.random() * 255),
                    (int)(Math.random() * 255),
                    (int)(Math.random() * 255)
            );
            dx = (float)(Math.random() * 2 - 1);
            dy = 2 + (float)(Math.random() * 2);
        }

        void update() {
            x += dx;
            y += dy;
        }
    }

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

    public static void start(String p1, String p2,
                             Scoreboard scoreboard, Leaderboard leaderboard,
                             PlayerIcon p1Icon, PlayerIcon p2Icon) {

        ModernUI.installGlobalFont();

        PlayerIcon[] icons = {p1Icon, p2Icon};
        Theme[] currentTheme = {LIGHT};

        JFrame frame = new JFrame("Tic Tac Toe — " + p1 + " vs " + p2);
        frame.setSize(380, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        AnimatedGradientPanel bg = new AnimatedGradientPanel();
        bg.setLayout(new BorderLayout());
        frame.setContentPane(bg);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(p1 + " (X) vs " + p2 + " (O) — Turn: X", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(new Color(230, 230, 230));

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);

        JButton[][] buttons = new JButton[3][3];
        char[][] board = new char[3][3];
        resetBoard(board);

        char[] currentPlayer = {'X'};
        boolean[] gameOver = {false};

        JPanel boardCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(6, 10, getWidth() - 12, getHeight() - 20, 30, 30);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 4, getWidth() - 12, getHeight() - 20, 30, 30);
                g2.dispose();
            }
        };
        boardCard.setOpaque(false);
        boardCard.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 16, 16, 16));

        JButton restartBtn = ModernUI.createSecondaryButton("Restart");
        JButton settingsBtn = ModernUI.createSecondaryButton("Settings");
        JButton backBtn = ModernUI.createSecondaryButton("Menu");

        bottomPanel.add(restartBtn);
        bottomPanel.add(settingsBtn);
        bottomPanel.add(backBtn);

        Runnable applyTheme = () -> bg.setDarkMode(currentTheme[0].darkMode);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {

                JButton btn = ModernUI.createCellButton();
                btn.setFont(new Font("SansSerif", Font.BOLD, 18));
                btn.setText(" ");
                btn.setIcon(null);

                int row = r, col = c;

                btn.addActionListener(e -> {
                    if (gameOver[0]) return;
                    if (btn.getIcon() != null) return;

                    Sound.beepClick();

                    btn.setText("");
                    btn.setIcon(currentPlayer[0] == 'X' ? icons[0] : icons[1]);

                    animateCellClick(btn);

                    board[row][col] = currentPlayer[0];

                    if (checkWin(board, currentPlayer[0])) {
                        gameOver[0] = true;

                        String winner = (currentPlayer[0] == 'X') ? p1 : p2;
                        String winMsg = getCreativeWinMessage(winner, currentPlayer[0]);

                        Sound.winJingle();

                        flashScreen(frame);
                        playConfetti(frame);
                        animateWinningCells(buttons, getWinningCells(board, currentPlayer[0]));

                        if (currentPlayer[0] == 'X') {
                            scoreboard.addWinForPlayer1();
                            leaderboard.recordWin(p1);
                            leaderboard.recordLoss(p2);
                        } else {
                            scoreboard.addWinForPlayer2();
                            leaderboard.recordWin(p2);
                            leaderboard.recordLoss(p1);
                        }

                        JOptionPane.showMessageDialog(
                                frame,
                                "🎉 " + winMsg + "\n\n" + scoreboard.getScoreboard(p1, p2),
                                "Winner!",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        disableButtons(buttons);
                        return;
                    }

                    if (isDraw(board)) {
                        gameOver[0] = true;
                        Sound.beepDraw();

                        scoreboard.addDraw();
                        leaderboard.recordDraw(p1, p2);

                        JOptionPane.showMessageDialog(
                                frame,
                                "It's a draw!\n" + scoreboard.getScoreboard(p1, p2),
                                "Draw",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        disableButtons(buttons);
                        return;
                    }

                    currentPlayer[0] = (currentPlayer[0] == 'X') ? 'O' : 'X';
                    subtitle.setText(p1 + " (X) vs " + p2 + " (O) — Turn: " + currentPlayer[0]);
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
            subtitle.setText(p1 + " (X) vs " + p2 + " (O) — Turn: X");

            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++) {
                    buttons[r][c].setText(" ");
                    buttons[r][c].setIcon(null);
                    buttons[r][c].setEnabled(true);
                }
        });

        backBtn.addActionListener(e -> {
            Sound.beepButton();
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.revalidate();
            TicTacToeWebApp.showMainMenu(
                    frame,
                    p1,
                    p2,
                    scoreboard,
                    leaderboard,
                    new java.util.Scanner(System.in)
            );
        });

        settingsBtn.addActionListener(e -> {
            Sound.beepButton();
            openSettingsMenu(frame, currentTheme, applyTheme, scoreboard, leaderboard, p1, p2);
        });

        applyTheme.run();

        boardCard.add(gridPanel, BorderLayout.CENTER);

        bg.add(headerPanel, BorderLayout.NORTH);
        bg.add(boardCard, BorderLayout.CENTER);
        bg.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void animateCellClick(JButton btn) {
        Timer timer = new Timer(40, null);
        final int[] step = {0};

        timer.addActionListener(e -> {
            float alpha = (float) Math.abs(Math.sin(step[0] * 0.4));
            btn.setForeground(new Color(0, 0, 0, 180 + (int)(alpha * 60)));
            step[0]++;
            if (step[0] > 6) timer.stop();
        });

        timer.start();
    }

    private static void playConfetti(JFrame frame) {
        JComponent layer = new JComponent() {
            java.util.List<Confetti> confetti = new java.util.ArrayList<>();

            {
                for (int i = 0; i < 60; i++)
                    confetti.add(new Confetti(frame.getWidth(), frame.getHeight()));

                Timer timer = new Timer(16, e -> {
                    for (Confetti c : confetti) c.update();
                    repaint();
                });
                timer.start();

                Timer end = new Timer(1500, e -> setVisible(false));
                end.setRepeats(false);
                end.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                for (Confetti c : confetti) {
                    g2.setColor(c.color);
                    g2.fillOval(c.x, c.y, c.size, c.size);
                }
            }
        };

        frame.setGlassPane(layer);
        layer.setVisible(true);
    }

    private static void flashScreen(JFrame frame) {
        JWindow flash = new JWindow();
        flash.setBackground(new Color(255, 255, 255, 0));
        flash.setBounds(frame.getBounds());
        flash.setVisible(true);

        Timer timer = new Timer(16, null);
        final int[] alpha = {0};

        timer.addActionListener(e -> {
            alpha[0] += 25;
            if (alpha[0] > 255) alpha[0] = 255;

            flash.setBackground(new Color(255, 255, 255, alpha[0]));

            if (alpha[0] == 255) {
                timer.stop();
                flash.setVisible(false);
                flash.dispose();
            }
        });

        timer.start();
    }

    private static void animateWinningCells(JButton[][] buttons, int[][] winCells) {
        Timer timer = new Timer(80, null);
        final int[] step = {0};

        timer.addActionListener(e -> {
            boolean on = (step[0] % 2 == 0);

            for (int[] cell : winCells) {
                JButton b = buttons[cell[0]][cell[1]];
                b.setBorder(BorderFactory.createLineBorder(on ? Color.YELLOW : Color.WHITE, 4));
            }

            step[0]++;
            if (step[0] > 10) timer.stop();
        });

        timer.start();
    }

    private static void openSettingsMenu(JFrame parent, Theme[] currentTheme, Runnable applyTheme,
                                         Scoreboard scoreboard, Leaderboard leaderboard,
                                         String p1, String p2) {

        JDialog dialog = new JDialog(parent, "Settings", true);
        dialog.setSize(360, 360);
        dialog.setLocationRelativeTo(parent);
        dialog.setUndecorated(true);

        AnimatedGradientPanel bg = new AnimatedGradientPanel();
        bg.setLayout(new GridBagLayout());
        dialog.setContentPane(bg);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(6, 6, getWidth() - 12, getHeight() - 12, 30, 30);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, getWidth() - 12, getHeight() - 12, 30, 30);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(300, 280));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Settings", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JButton themeBtn = ModernUI.createPrimaryButton("Switch Theme");
        JButton scoreBtn = ModernUI.createSecondaryButton("Show Scoreboard");
        JButton leaderBtn = ModernUI.createSecondaryButton("Show Leaderboard");
        JButton closeBtn = ModernUI.createSecondaryButton("Close");

        c.gridy = 0; card.add(title, c);
        c.gridy = 1; card.add(themeBtn, c);
        c.gridy = 2; card.add(scoreBtn, c);
        c.gridy = 3; card.add(leaderBtn, c);
        c.gridy = 4; card.add(closeBtn, c);

        gbc.gridy = 0;
        bg.add(card, gbc);

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

        closeBtn.addActionListener(e -> {
            Sound.beepButton();
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

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
}
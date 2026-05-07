package tictactoe;

import javax.swing.*;
import java.awt.*;

public class SimpleUI {

    public static void start(String p1, String p2) {

        JFrame frame = new JFrame("Tic Tac Toe — " + p1 + " (X) vs " + p2 + " (O)");
        frame.setSize(350, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JButton[][] buttons = new JButton[3][3];
        char[][] board = new char[3][3];
        resetBoard(board);

        char[] currentPlayer = {'X'};
        boolean[] gameOver = {false};

        JPanel gridPanel = new JPanel(new GridLayout(3, 3));

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {

                JButton btn = new JButton(" ");
                btn.setFont(new Font("Arial", Font.BOLD, 40));

                int row = r, col = c;

                btn.addActionListener(e -> {
                    if (gameOver[0]) return;
                    if (!btn.getText().equals(" ")) return;

                    btn.setText(String.valueOf(currentPlayer[0]));
                    board[row][col] = currentPlayer[0];

                    if (checkWin(board, currentPlayer[0])) {
                        gameOver[0] = true;

                        String winner = (currentPlayer[0] == 'X') ? p1 : p2;

                        JOptionPane.showMessageDialog(frame,
                                "🎉 " + winner + " (" + currentPlayer[0] + ") wins!");

                        disableButtons(buttons);
                        return;
                    }

                    if (isDraw(board)) {
                        gameOver[0] = true;
                        JOptionPane.showMessageDialog(frame, "It's a draw!");
                        disableButtons(buttons);
                        return;
                    }

                    currentPlayer[0] = (currentPlayer[0] == 'X') ? 'O' : 'X';
                });

                buttons[r][c] = btn;
                gridPanel.add(btn);
            }
        }

        // ⭐ Restart Button
        JButton restartBtn = new JButton("Restart Game");
        restartBtn.setFont(new Font("Arial", Font.BOLD, 20));

        restartBtn.addActionListener(e -> {
            resetBoard(board);
            gameOver[0] = false;
            currentPlayer[0] = 'X';

            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    buttons[r][c].setText(" ");
                    buttons[r][c].setEnabled(true);
                }
            }
        });

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(restartBtn, BorderLayout.SOUTH);

        frame.setVisible(true);
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
            if (b[r][0] == p && b[r][1] == p && b[r][2] == p)
                return true;

        for (int c = 0; c < 3; c++)
            if (b[0][c] == p && b[1][c] == p && b[2][c] == p)
                return true;

        if (b[0][0] == p && b[1][1] == p && b[2][2] == p) return true;
        if (b[0][2] == p && b[1][1] == p && b[2][0] == p) return true;

        return false;
    }

    private static boolean isDraw(char[][] b) {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (b[r][c] == ' ')
                    return false;
        return true;
    }
}

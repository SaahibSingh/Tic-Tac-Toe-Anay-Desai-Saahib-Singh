package tictactoe;
//Import
import javax.swing.*;
import java.awt.*;

public class SimpleUI {
    public static void start() {
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton[][] buttons = new JButton[3][3];
        char currentPlayer[] = {'X'};
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton btn = new JButton(" ");
                btn.setFont(new Font("Arial", Font.BOLD, 40));
                int row = r, col = c;
                btn.addActionListener(e -> {
                    if (btn.getText().equals(" ")) {
                        btn.setText(String.valueOf(currentPlayer[0]));
                        currentPlayer[0] = (currentPlayer[0] == 'X') ? 'O' : 'X';
                    }
                });
                buttons[r][c] = btn;
                panel.add(btn);
            }
        }
        frame.add(panel);
        frame.setVisible(true);
    }
}

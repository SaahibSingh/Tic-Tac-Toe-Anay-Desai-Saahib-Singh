package tictactoe;

//Imports
import javax.swing.*;
import java.awt.*;

public class TitleScreen {
    public interface StartCallback { void onStart(); }
    public static void show(JFrame frame, StartCallback callback) {

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.BLACK);

        frame.setContentPane(panel);
        frame.repaint();
        frame.revalidate();

        JLabel title = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 42));
        title.setForeground(Color.WHITE);
        title.setBounds(40, 180, 300, 60);
        title.setVisible(false);
        panel.add(title);

        JLabel subtitle = new JLabel("by Saahib", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 20));
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setBounds(40, 240, 300, 40);
        subtitle.setVisible(false);
        panel.add(subtitle);

        JButton startBtn = new JButton("Start Game");
        startBtn.setFont(new Font("Arial", Font.BOLD, 24));
        startBtn.setBounds(90, 500, 200, 50);
        startBtn.setVisible(false);
        panel.add(startBtn);

        Timer fadeTimer = new Timer(20, null);
        final float[] alpha = {1f};

        fadeTimer.addActionListener(e -> {
            alpha[0] -= 0.02f;
            if (alpha[0] <= 0f) {
                fadeTimer.stop();
                panel.setBackground(new Color(0, 0, 0, 0));
                title.setVisible(true);
                subtitle.setVisible(true);
                animateTitleZoom(title, subtitle, startBtn);
            } else {
                panel.setBackground(new Color(0, 0, 0, alpha[0]));
            }
        });

        fadeTimer.start();

        startBtn.addActionListener(e -> {
            Sound.beepButton();
            callback.onStart();
        });
    }

    private static void animateTitleZoom(JLabel title, JLabel subtitle, JButton startBtn) {
        Timer zoomTimer = new Timer(20, null);
        final float[] scale = {0.8f};

        zoomTimer.addActionListener(e -> {
            scale[0] += 0.02f;

            title.setFont(title.getFont().deriveFont(42f * scale[0]));
            subtitle.setFont(subtitle.getFont().deriveFont(20f * scale[0]));

            if (scale[0] >= 1f) {
                zoomTimer.stop();
                animateButtonsSlide(startBtn);
            }
        });

        zoomTimer.start();
    }

    private static void animateButtonsSlide(JButton startBtn) {
        startBtn.setVisible(true);

        Timer slideTimer = new Timer(10, null);
        final int[] y = {600};

        slideTimer.addActionListener(e -> {
            y[0] -= 8;
            startBtn.setLocation(startBtn.getX(), y[0]);

            if (y[0] <= 400) slideTimer.stop();
        });

        slideTimer.start();
    }
}
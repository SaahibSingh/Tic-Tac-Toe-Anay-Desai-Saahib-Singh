package tictactoe;

import javax.swing.*;
import java.awt.*;

public class TitleScreen {

    public interface StartCallback {
        void onStart();
    }

    public static void show(JFrame frame, StartCallback callback) {

        ModernUI.installGlobalFont();

        AnimatedGradientPanel bg = new AnimatedGradientPanel();
        bg.setLayout(null);

        frame.setContentPane(bg);
        frame.repaint();
        frame.revalidate();

        JLabel title = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 42));
        title.setForeground(Color.WHITE);
        title.setBounds(20, 140, 340, 60);
        title.setOpaque(false);
        title.setVisible(false);
        bg.add(title);

        JLabel subtitle = new JLabel("by Saahib", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 20));
        subtitle.setForeground(new Color(230, 230, 230));
        subtitle.setBounds(20, 200, 340, 40);
        subtitle.setVisible(false);
        bg.add(subtitle);

        JButton startBtn = ModernUI.createPrimaryButton("Start");
        startBtn.setBounds(110, 380, 160, 55);
        startBtn.setVisible(false);
        bg.add(startBtn);

        // Fade-in animation
        Timer fadeTimer = new Timer(20, null);
        final float[] alpha = {1f};

        fadeTimer.addActionListener(e -> {
            alpha[0] -= 0.02f;
            if (alpha[0] <= 0f) {
                fadeTimer.stop();
                title.setVisible(true);
                subtitle.setVisible(true);
                animateZoom(title, subtitle, startBtn);
            } else {
                bg.setBackground(new Color(0, 0, 0, alpha[0]));
            }
        });

        fadeTimer.start();

        startBtn.addActionListener(e -> {
            Sound.beepButton();
            callback.onStart();
        });
    }

    private static void animateZoom(JLabel title, JLabel subtitle, JButton startBtn) {
        Timer zoomTimer = new Timer(20, null);
        final float[] scale = {0.7f};

        zoomTimer.addActionListener(e -> {
            scale[0] += 0.02f;

            title.setFont(title.getFont().deriveFont(42f * scale[0]));
            subtitle.setFont(subtitle.getFont().deriveFont(20f * scale[0]));

            if (scale[0] >= 1f) {
                zoomTimer.stop();
                animateSlide(startBtn);
            }
        });

        zoomTimer.start();
    }

    private static void animateSlide(JButton btn) {
        btn.setVisible(true);

        Timer slideTimer = new Timer(10, null);
        final int[] y = {600};

        slideTimer.addActionListener(e -> {
            y[0] -= 8;
            btn.setLocation(btn.getX(), y[0]);

            if (y[0] <= 380) {
                slideTimer.stop();
            }
        });

        slideTimer.start();
    }
}
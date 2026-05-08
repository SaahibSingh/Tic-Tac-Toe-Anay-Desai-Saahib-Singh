package tictactoe;

//Imports
import javax.swing.*;
import java.awt.*;

public class AnimatedGradientPanel extends JPanel {
    private float hue = 0f;
    private boolean darkMode = false;

    public AnimatedGradientPanel() {
        Timer timer = new Timer(40, e -> {
            hue += 0.0015f;
            if (hue > 1f) hue = 0f;
            repaint();
        });
        timer.start();
    }

    public void setDarkMode(boolean dark) { this.darkMode = dark; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Color c1, c2;
        
        if (!darkMode) {
            c1 = Color.getHSBColor(hue, 0.25f, 1f);
            c2 = Color.getHSBColor((hue + 0.15f) % 1f, 0.25f, 1f);
        } else {
            c1 = Color.getHSBColor(hue, 0.9f, 1f);
            c2 = Color.getHSBColor((hue + 0.2f) % 1f, 0.9f, 1f);
        }

        GradientPaint gp = new GradientPaint(
                0, 0, c1,
                getWidth(), getHeight(), c2
        );

        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
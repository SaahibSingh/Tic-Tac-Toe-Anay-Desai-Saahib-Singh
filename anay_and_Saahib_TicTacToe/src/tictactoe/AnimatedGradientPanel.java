package tictactoe;

import javax.swing.*;
import java.awt.*;

public class AnimatedGradientPanel extends JPanel {

    private float t = 0f;
    private boolean darkMode = false;

    public AnimatedGradientPanel() {
        Timer timer = new Timer(35, e -> {
            t += 0.002f;
            if (t > 1f) t = 0f;
            repaint();
        });
        timer.start();
        setOpaque(false);
    }

    public void setDarkMode(boolean dark) {
        this.darkMode = dark;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        float h1 = (t + 0.0f) % 1f;
        float h2 = (t + 0.25f) % 1f;
        float h3 = (t + 0.5f) % 1f;

        Color c1, c2, c3;
        if (!darkMode) {
            c1 = Color.getHSBColor(h1, 0.35f, 1f);
            c2 = Color.getHSBColor(h2, 0.35f, 0.95f);
            c3 = Color.getHSBColor(h3, 0.35f, 0.9f);
        } else {
            c1 = Color.getHSBColor(h1, 0.8f, 0.9f);
            c2 = Color.getHSBColor(h2, 0.8f, 0.7f);
            c3 = Color.getHSBColor(h3, 0.8f, 0.6f);
        }

        int w = getWidth();
        int h = getHeight();

        GradientPaint gp1 = new GradientPaint(0, 0, c1, w, 0, c2);
        g2.setPaint(gp1);
        g2.fillRect(0, 0, w, h);

        GradientPaint gp2 = new GradientPaint(0, h, new Color(c3.getRed(), c3.getGreen(), c3.getBlue(), 160),
                                              w, 0, new Color(0, 0, 0, 0));
        g2.setPaint(gp2);
        g2.fillRect(0, 0, w, h);

        g2.dispose();
    }
}
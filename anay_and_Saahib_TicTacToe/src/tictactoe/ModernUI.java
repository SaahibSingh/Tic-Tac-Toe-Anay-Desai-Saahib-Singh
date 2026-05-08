package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernUI {

    private static final int ARC = 18;

    public static void installGlobalFont() {
        Font f = new Font("SansSerif", Font.PLAIN, 16);
        UIManager.put("Label.font", f);
        UIManager.put("Button.font", f);
        UIManager.put("OptionPane.messageFont", f);
        UIManager.put("OptionPane.buttonFont", f);
    }

    public static JButton createPrimaryButton(String text) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color base = getModel().isPressed()
                        ? new Color(60, 120, 255)
                        : (getModel().isRollover()
                            ? new Color(90, 150, 255)
                            : new Color(70, 130, 255));

                g2.setColor(new Color(0, 0, 0, 60));
                g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 6, ARC, ARC);

                g2.setPaint(new GradientPaint(0, 0, base.brighter(), 0, getHeight(), base.darker()));
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 8, ARC, ARC);

                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2 - 4;
                g2.drawString(getText(), tx, ty);

                g2.dispose();
            }

            @Override public void setContentAreaFilled(boolean b) {}
            @Override public void setBorderPainted(boolean b) {}
        };
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        return b;
    }

    public static JButton createSecondaryButton(String text) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color base = getModel().isPressed()
                        ? new Color(40, 40, 40, 200)
                        : (getModel().isRollover()
                            ? new Color(60, 60, 60, 200)
                            : new Color(30, 30, 30, 180));

                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 6, ARC, ARC);

                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 8, ARC, ARC);

                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2 - 4;
                g2.drawString(getText(), tx, ty);

                g2.dispose();
            }

            @Override public void setContentAreaFilled(boolean b) {}
            @Override public void setBorderPainted(boolean b) {}
        };
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        return b;
    }

    public static JButton createCellButton() {
        JButton b = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color base = getModel().isPressed()
                        ? new Color(255, 255, 255, 220)
                        : (getModel().isRollover()
                            ? new Color(255, 255, 255, 210)
                            : new Color(255, 255, 255, 190));

                g2.setColor(new Color(0, 0, 0, 60));
                g2.fill(new RoundRectangle2D.Float(3, 4, getWidth() - 6, getHeight() - 6, 20, 20));

                g2.setColor(base);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 6, getHeight() - 8, 20, 20));

                super.paintComponent(g2);
                g2.dispose();
            }

            @Override public void setContentAreaFilled(boolean b) {}
            @Override public void setBorderPainted(boolean b) {}
        };
        b.setOpaque(false);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        b.setHorizontalTextPosition(SwingConstants.CENTER);
        b.setVerticalTextPosition(SwingConstants.BOTTOM);
        return b;
    }
}
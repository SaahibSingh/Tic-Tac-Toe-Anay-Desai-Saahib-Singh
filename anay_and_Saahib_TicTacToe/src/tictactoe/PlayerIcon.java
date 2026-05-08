package tictactoe;

import javax.swing.*;
import java.awt.*;

public class PlayerIcon implements Icon {

    public enum ShapeType {
        CIRCLE, TRIANGLE, DIAMOND, SQUARE, STAR, HEART
    }

    private final ShapeType shape;
    private final Color color;
    private final char letter;
    private final int size;

    public PlayerIcon(ShapeType shape, Color color, char letter, int size) {
        this.shape = shape;
        this.color = color;
        this.letter = letter;
        this.size = size;
    }

    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getIconWidth();
        int h = getIconHeight();

        int shapeHeight = (int)(h * 0.6);
        int letterHeight = h - shapeHeight;

        // Draw shape
        g2.setColor(color);
        int sx = x + w / 2;
        int sy = y + shapeHeight / 2;
        int r = (int)(Math.min(w, shapeHeight) * 0.4);

        switch (shape) {
            case CIRCLE -> g2.fillOval(sx - r, sy - r, 2 * r, 2 * r);
            case SQUARE -> g2.fillRect(sx - r, sy - r, 2 * r, 2 * r);
            case DIAMOND -> {
                Polygon p = new Polygon();
                p.addPoint(sx, sy - r);
                p.addPoint(sx + r, sy);
                p.addPoint(sx, sy + r);
                p.addPoint(sx - r, sy);
                g2.fillPolygon(p);
            }
            case TRIANGLE -> {
                Polygon p = new Polygon();
                p.addPoint(sx, sy - r);
                p.addPoint(sx - r, sy + r);
                p.addPoint(sx + r, sy + r);
                g2.fillPolygon(p);
            }
            case STAR -> {
                Polygon p = new Polygon();
                for (int i = 0; i < 10; i++) {
                    double angle = Math.toRadians(i * 36);
                    int rr = (i % 2 == 0) ? r : r / 2;
                    int px = sx + (int)(Math.cos(angle) * rr);
                    int py = sy + (int)(Math.sin(angle) * rr);
                    p.addPoint(px, py);
                }
                g2.fillPolygon(p);
            }
            case HEART -> {
                int hr = r;
                int hx = sx;
                int hy = sy;
                Polygon p = new Polygon();
                p.addPoint(hx, hy + hr);
                p.addPoint(hx - hr, hy);
                p.addPoint(hx - hr / 2, hy - hr);
                p.addPoint(hx, hy - hr / 2);
                p.addPoint(hx + hr / 2, hy - hr);
                p.addPoint(hx + hr, hy);
                g2.fillPolygon(p);
            }
        }

        // Draw letter below shape
        g2.setColor(Color.BLACK);
        Font font = c.getFont().deriveFont(Font.BOLD, (float)(letterHeight * 0.8));
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        String s = String.valueOf(letter);
        int tx = x + (w - fm.stringWidth(s)) / 2;
        int ty = y + shapeHeight + (letterHeight + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(s, tx, ty);

        g2.dispose();
    }
}
package tictactoe;

import javax.swing.*;
import java.awt.*;

public class PlayerIconSelector {

    public interface IconSelectionCallback {
        void onSelected(PlayerIcon p1Icon, PlayerIcon p2Icon);
    }

    public static void show(JFrame parent, String p1, String p2, IconSelectionCallback callback) {

        ModernUI.installGlobalFont();

        JDialog dialog = new JDialog(parent, "Choose Player Icons", true);
        dialog.setSize(420, 420);
        dialog.setLocationRelativeTo(parent);
        dialog.setUndecorated(true);

        AnimatedGradientPanel bg = new AnimatedGradientPanel();
        bg.setLayout(new GridBagLayout());
        dialog.setContentPane(bg);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== Card Panel =====
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
        card.setPreferredSize(new Dimension(360, 340));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        // ===== Title =====
        JLabel title = new JLabel("Choose Player Icons", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        gbc.gridy = 0;
        bg.add(title, gbc);

        // ===== Icon Options =====
        String[] options = {"Circle", "Triangle", "Diamond", "Square", "Star", "Heart"};

        JLabel p1Label = new JLabel(p1 + " Icon:", SwingConstants.CENTER);
        p1Label.setForeground(Color.WHITE);
        p1Label.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel p2Label = new JLabel(p2 + " Icon:", SwingConstants.CENTER);
        p2Label.setForeground(Color.WHITE);
        p2Label.setFont(new Font("SansSerif", Font.BOLD, 18));

        JComboBox<String> p1Box = new JComboBox<>(options);
        JComboBox<String> p2Box = new JComboBox<>(options);

        p1Box.setFont(new Font("SansSerif", Font.PLAIN, 16));
        p2Box.setFont(new Font("SansSerif", Font.PLAIN, 16));

        // ===== Preview Icons =====
        JLabel p1Preview = new JLabel();
        JLabel p2Preview = new JLabel();

        p1Preview.setHorizontalAlignment(SwingConstants.CENTER);
        p2Preview.setHorizontalAlignment(SwingConstants.CENTER);

        p1Preview.setIcon(new PlayerIcon(PlayerIcon.ShapeType.CIRCLE, new Color(0,102,204), 'X', 80));
        p2Preview.setIcon(new PlayerIcon(PlayerIcon.ShapeType.CIRCLE, new Color(204,0,0), 'O', 80));

        // Update preview on selection
        p1Box.addActionListener(e -> {
            p1Preview.setIcon(new PlayerIcon(toShape((String)p1Box.getSelectedItem()), new Color(0,102,204), 'X', 80));
        });

        p2Box.addActionListener(e -> {
            p2Preview.setIcon(new PlayerIcon(toShape((String)p2Box.getSelectedItem()), new Color(204,0,0), 'O', 80));
        });

        // ===== Layout inside card =====
        c.gridy = 0; card.add(p1Label, c);
        c.gridy = 1; card.add(p1Preview, c);
        c.gridy = 2; card.add(p1Box, c);

        c.gridy = 3; card.add(p2Label, c);
        c.gridy = 4; card.add(p2Preview, c);
        c.gridy = 5; card.add(p2Box, c);

        gbc.gridy = 1;
        bg.add(card, gbc);

        // ===== Buttons =====
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        btnPanel.setOpaque(false);

        JButton okBtn = ModernUI.createPrimaryButton("OK");
        JButton cancelBtn = ModernUI.createSecondaryButton("Cancel");

        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);

        gbc.gridy = 2;
        bg.add(btnPanel, gbc);

        // ===== Button Actions =====
        okBtn.addActionListener(e -> {
            Sound.beepButton();

            PlayerIcon.ShapeType s1 = toShape((String)p1Box.getSelectedItem());
            PlayerIcon.ShapeType s2 = toShape((String)p2Box.getSelectedItem());

            PlayerIcon p1IconFinal = new PlayerIcon(s1, new Color(0,102,204), 'X', 64);
            PlayerIcon p2IconFinal = new PlayerIcon(s2, new Color(204,0,0), 'O', 64);

            callback.onSelected(p1IconFinal, p2IconFinal);
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> {
            Sound.beepButton();
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private static PlayerIcon.ShapeType toShape(String name) {
        return switch (name) {
            case "Circle" -> PlayerIcon.ShapeType.CIRCLE;
            case "Triangle" -> PlayerIcon.ShapeType.TRIANGLE;
            case "Diamond" -> PlayerIcon.ShapeType.DIAMOND;
            case "Square" -> PlayerIcon.ShapeType.SQUARE;
            case "Star" -> PlayerIcon.ShapeType.STAR;
            case "Heart" -> PlayerIcon.ShapeType.HEART;
            default -> PlayerIcon.ShapeType.CIRCLE;
        };
    }
}

package Utill;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MakePrettyInterface {
    private static final Font TITLE_FONT = new Font("맑은 고딕", Font.BOLD, 40);
    private static final Font HEADING_FONT = new Font("맑은 고딕", Font.BOLD, 28);
    private static final Font BUTTON_FONT = new Font("맑은 고딕", Font.BOLD, 14);
    private static final Font BODY_FONT = new Font("맑은 고딕", Font.PLAIN, 14);
    private static final LineBorder DEFAULT_LINE_BORDER = new LineBorder(Color.BLACK, 1, true);

    // 공통 크기/간격
    public static void setFixedSize(JComponent component, int width, int height) {
        Dimension size = new Dimension(width, height);
        component.setPreferredSize(size);
        component.setMaximumSize(size);
        component.setMinimumSize(size);
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public static Component spacer(int width, int height) {
        return Box.createRigidArea(new Dimension(width, height));
    }

    public static void updateScreen(JPanel p) {
        p.revalidate();
        p.repaint();
    }

    public static void updateScreen(JFrame p) {
        p.revalidate();
        p.repaint();
    }

    // Shadow helpers
    public static void makeShadow(JPanel jPanel) {
        jPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.GRAY));
    }

    public static void makeShadow(JButton jButton) {
        jButton.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.GRAY));
    }

    public static void makeShadow(JTextField jButton) {
        jButton.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.GRAY));
    }

    public static void makeShadow(JPanel jPanel, boolean isBold) {
        if (isBold) {
            jPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.GRAY));
        } else {
            jPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.GRAY));
        }
    }

    public static void makeShadow(JButton jButton, boolean isBold) {
        if (isBold) {
            jButton.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.GRAY));
        } else {
            jButton.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.GRAY));
        }
    }

    public static void makeShadow(JTextField jButton, boolean isBold) {
        if (isBold) {
            jButton.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.GRAY));
        } else {
            jButton.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.GRAY));
        }
    }

    // White background helpers
    public static void makeWhite(JPanel jPanel) {
        jPanel.setBackground(Color.WHITE);
        jPanel.setOpaque(true);
    }

    public static void makeWhite(JButton jButton) {
        jButton.setBackground(Color.WHITE);
        jButton.setOpaque(true);
    }

    public static void makeWhite(JLabel jLabel) {
        jLabel.setBackground(Color.WHITE);
        jLabel.setOpaque(true);
    }

    public static void makeWhite(JComboBox<?> comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setOpaque(true);
    }

    public static void makeWhite(JTextArea textArea) {
        textArea.setBackground(Color.WHITE);
        textArea.setOpaque(true);
    }

    // ----- QuizUI 스타일 공통 구성 -----
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        return label;
    }

    public static JLabel createHeadingLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADING_FONT);
        return label;
    }

    public static void styleTopBar(JPanel topPanel) {
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(DEFAULT_LINE_BORDER);
        makeWhite(topPanel);
    }

    public static void styleMenuToggleButton(JButton menuButton) {
        menuButton.setFont(new Font("Arial", Font.BOLD, 30));
        setFixedSize(menuButton, 50, 50);
        menuButton.setFocusPainted(false);
        makeWhite(menuButton);
        menuButton.setBorder(DEFAULT_LINE_BORDER);
    }

    public static void stylePrimaryButton(JButton button) {
        stylePrimaryButton(button, 200, 70);
    }

    public static void stylePrimaryButton(JButton button, int width, int height) {
        button.setFont(BUTTON_FONT);
        setFixedSize(button, width, height);
        makeWhite(button);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorder(DEFAULT_LINE_BORDER);
        makeShadow(button);
    }

    public static void styleSecondaryButton(JButton button) {
        button.setFont(BUTTON_FONT);
        makeWhite(button);
        button.setFocusPainted(false);
        button.setBorder(DEFAULT_LINE_BORDER);
    }

    public static void styleComboBox(JComboBox<?> comboBox, int width, int height) {
        comboBox.setFont(BODY_FONT);
        makeWhite(comboBox);
        comboBox.setBorder(DEFAULT_LINE_BORDER);
        setFixedSize(comboBox, width, height);
    }

    public static void styleTextArea(JTextArea area) {
        area.setFont(BODY_FONT);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        area.setBackground(Color.WHITE);
    }

    public static void styleCardPanel(JPanel panel, int width, int height) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        makeWhite(panel);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        setFixedSize(panel, width, height);
        panel.setMaximumSize(new Dimension(width + 20, Integer.MAX_VALUE));
    }

    public static JPanel createContentContainer() {
        JPanel panel = new JPanel(new BorderLayout());
        makeWhite(panel);
        return panel;
    }

    public static JPanel createCenterColumn() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        makeWhite(panel);
        return panel;
    }
}

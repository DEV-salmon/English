package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SideMenu extends JPanel {

    private final JButton homeButton = createMenuButton("Home");
    private final JButton fileButton = createMenuButton("File");
    private final JButton statButton = createMenuButton("Stat");
    private final JButton quizButton = createMenuButton("Quiz");
    private final JButton logoutButton = createMenuButton("Log out");

    public SideMenu() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        setPreferredSize(new Dimension(180, 0)); // 너비 고정, 높이는 컨테이너에 맞춤

        add(homeButton);
        add(createSeparator());

        add(fileButton);
        add(createSeparator());

        add(statButton);
        add(createSeparator());

        add(quizButton);
        add(Box.createVerticalGlue());

        add(logoutButton);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 70));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        return button;
    }

    private JPanel createSeparator() {
        JPanel separator = new JPanel();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        separator.setBackground(Color.WHITE);
        return separator;
    }

    public JButton getHomeButton() {
        return homeButton;
    }

    public JButton getFileButton() {
        return fileButton;
    }

    public JButton getStatButton() {
        return statButton;
    }

    public JButton getQuizButton() {
        return quizButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }
}

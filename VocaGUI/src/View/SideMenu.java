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

    // 사이드 메뉴를 구성하는 생성자
    public SideMenu() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        setPreferredSize(new Dimension(180, 0));

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

    // 메뉴 버튼을 생성하고 스타일을 적용
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

    // 버튼 사이 구분선 패널을 생성
    private JPanel createSeparator() {
        JPanel separator = new JPanel();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        separator.setBackground(Color.WHITE);
        return separator;
    }

    // 홈 버튼을 반환
    public JButton getHomeButton() {
        return homeButton;
    }

    // 파일 버튼을 반환
    public JButton getFileButton() {
        return fileButton;
    }

    // 통계 버튼을 반환
    public JButton getStatButton() {
        return statButton;
    }

    // 퀴즈 버튼을 반환
    public JButton getQuizButton() {
        return quizButton;
    }

    // 로그아웃 버튼을 반환
    public JButton getLogoutButton() {
        return logoutButton;
    }
}

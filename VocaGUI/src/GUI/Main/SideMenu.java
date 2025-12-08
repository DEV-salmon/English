package GUI.Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import Signal.Controller;
import Utill.MakePrettyInterface;

public class SideMenu extends JPanel {

    private final JButton homeButton = createMenuButton("Home");
    private final JButton fileButton = createMenuButton("File");
    private final JButton statButton = createMenuButton("Stat");
    private final JButton quizButton = createMenuButton("Quiz");
    private final JButton incorrectButton = createMenuButton("InCorrect");
    private final JButton logoutButton = createMenuButton("Log out");

    private final JButton toggleButton = createToggleButton();
    private final Controller signalHandler;

    // 사이드 메뉴를 구성하는 생성자
    public SideMenu() {
        this(null);
    }

    public SideMenu(Controller signalHandler) {
        this.signalHandler = signalHandler != null ? signalHandler : (signal, data) -> {};
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        setPreferredSize(new Dimension(180, 0));

        add(homeButton);
        add(createSeparator());
        homeButton.addActionListener(e -> send(GlobalSignal.HOME, null));
        
        add(fileButton);
        add(createSeparator());
        fileButton.addActionListener(e -> send(GlobalSignal.FILE, null));

        add(statButton);
        add(createSeparator());
        statButton.addActionListener(e -> send(GlobalSignal.STAT, null));

        add(incorrectButton);
        add(createSeparator());
        incorrectButton.addActionListener(e -> send(GlobalSignal.INCORRECT, null));

        add(quizButton);
        add(Box.createVerticalGlue());
        quizButton.addActionListener(e -> send(GlobalSignal.QUIZ, null));

        add(logoutButton);
        logoutButton.addActionListener(e -> send(GlobalSignal.LOGOUT, null));
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

    // 토글 버튼 생성
    private JButton createToggleButton() {
        JButton menuBtn = new JButton("☰");
        menuBtn.setFont(new Font("Arial", Font.BOLD, 30));
        MakePrettyInterface.setFixedSize(menuBtn, 50, 50);
        menuBtn.addActionListener(e -> send(GlobalSignal.TOGGLE_MENU, null));
        return menuBtn;
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

    // 오답 버튼을 반환
    public JButton getIncorrectButton() {
        return incorrectButton;
    }

    // 로그아웃 버튼을 반환
    public JButton getLogoutButton() {
        return logoutButton;
    }

    // 상단에 붙이는 토글 버튼 반환
    public JButton getToggleButton() {
        return toggleButton;
    }

    private void send(GlobalSignal signal, Object payload) {
        signalHandler.send(signal, payload);
    }
}

package App.Home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import App.Login.LoginController;
import App.Login.LoginUI;
import Test.ExampleVector;
import Utill.MakePrettyInterface;
import Signal.Controller;
import voca.core.Word;
import App.Main.SideMenu;

public class HomeUI extends JPanel {

    private final SideMenu sideMenu;
    private final JPanel listContainer;
    private final JTextField searchField;
    private final Controller signalHandler;

    // 단어 목록을 받아 홈 화면을 구성하는 생성자
    public HomeUI(Vector<Word> voca, Controller signalHandler) {
        this.signalHandler = signalHandler != null ? signalHandler : (signal, data) -> {};

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        sideMenu = new SideMenu(signalHandler);
        sideMenu.setVisible(false);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        topPanel.setBackground(Color.WHITE);

        JButton menuBtn = sideMenu.getToggleButton();

        searchField = new JTextField(" 입력하세요");
        searchField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        MakePrettyInterface.setFixedSize(searchField, 400, 50);
        searchField.addActionListener(e -> sendSignal(HomeSignal.SEARCH, searchField.getText()));

        JButton searchBtn = new JButton("🔍");
        searchBtn.setFont(new Font("Arial", Font.BOLD, 30));
        MakePrettyInterface.setFixedSize(searchBtn, 50, 50);
        searchBtn.addActionListener(e -> sendSignal(HomeSignal.SEARCH, searchField.getText()));

        topPanel.add(menuBtn, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchBtn, BorderLayout.EAST);

        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(Color.WHITE);

        updateWords(voca);

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(new LineBorder(Color.GRAY, 1));

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(sideMenu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    // 단어 정보를 표시하는 리스트 행을 생성
    private JPanel createRowItem(Word word) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);

        panel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        String labelText = word.getEng() + " : " + String.join(", ", word.getKor());
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        panel.add(label, BorderLayout.CENTER);

        JButton plusButton = new JButton("+");
        plusButton.setFont(new Font("Arial", Font.BOLD, 20));
        plusButton.setPreferredSize(new Dimension(30, 30));
        plusButton.setForeground(Color.WHITE);
        plusButton.setBackground(Color.BLACK);
        plusButton.setOpaque(true);
        plusButton.setBorder(null);
        plusButton.setFocusPainted(false);
        plusButton.addActionListener(e -> sendSignal(HomeSignal.ADD, word));

        panel.add(plusButton, BorderLayout.EAST);

        return panel;
    }

    // 사이드 메뉴 표시 여부를 설정
    public void setSideMenuVisible(boolean visible) {
        sideMenu.setVisible(visible);
        MakePrettyInterface.updateScreen(this);
    }

    // 단어 목록을 업데이트
    public void updateWords(Vector<Word> voca) {
        listContainer.removeAll();
        if (voca != null) {
            for (Word w : voca) {
                JPanel rowPanel = createRowItem(w);
                listContainer.add(rowPanel);
                listContainer.add(Box.createVerticalStrut(10));
            }
        }
        listContainer.add(Box.createVerticalGlue());
        listContainer.revalidate();
        listContainer.repaint();
    }

    private void sendSignal(HomeSignal signal, Object payload) {
        signalHandler.send(signal, payload);
    }

    // 사이드 메뉴 인스턴스를 반환
    public SideMenu getSideMenu() {
        return sideMenu;
    }

    // 홈 화면을 확인하기 위한 테스트 메인
    public static void main(String[] args) {
        ExampleVector v = new ExampleVector();
        HomeController homeController = new HomeController(v.voca);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.add(homeController.getView());
        frame.setVisible(true);
    }
}

package View;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.LineBorder;

import voca.core.Word;

public class HomeInterface extends JPanel {

    private final SideMenu sideMenu;

    // 단어 목록을 받아 홈 화면을 구성하는 생성자
    public HomeInterface(Vector<Word> voca) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        sideMenu = new SideMenu();
        sideMenu.setVisible(false);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        topPanel.setBackground(Color.WHITE);

        JButton menuBtn = new JButton("☰");
        menuBtn.setFont(new Font("Arial", Font.BOLD, 30));
        MakePrettyInterface.setFixedSize(menuBtn, 50, 50);
        menuBtn.addActionListener(e -> toggleMenu());
        
        JTextField searchField = new JTextField(" 입력하세요");
        searchField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        MakePrettyInterface.setFixedSize(searchField, 400, 50);
        
        JButton searchBtn = new JButton("🔍");
        searchBtn.setFont(new Font("Arial", Font.BOLD, 30));
        MakePrettyInterface.setFixedSize(searchBtn, 50, 50);

        topPanel.add(menuBtn, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchBtn, BorderLayout.EAST);

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(Color.WHITE);

        for (Word w : voca) {
            JPanel rowPanel = createRowItem(w);
            listContainer.add(rowPanel);
            listContainer.add(Box.createVerticalStrut(10));
        }
        listContainer.add(Box.createVerticalGlue());

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

        panel.add(plusButton, BorderLayout.EAST);

        return panel;
    }

    // 사이드 메뉴 표시 여부를 토글
    private void toggleMenu() {
        sideMenu.setVisible(!sideMenu.isVisible());
        revalidate();
        repaint();
    }

    // 사이드 메뉴 인스턴스를 반환
    public SideMenu getSideMenu() {
        return sideMenu;
    }

    // 홈 화면을 확인하기 위한 테스트 메인
    public static void main(String[] args) {
        ExampleVector v = new ExampleVector();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.add(new HomeInterface(v.voca));
        frame.setVisible(true);
    }
}

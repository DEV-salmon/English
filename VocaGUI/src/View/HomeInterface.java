package View;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.LineBorder;

import voca.core.Word;

public class HomeInterface extends JPanel {

    private final SideMenu sideMenu;

    public HomeInterface(Vector<Word> voca) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        sideMenu = new SideMenu();
        sideMenu.setVisible(false); // 메뉴는 기본적으로 숨김

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

        
        // 리스트 아이템들을 담을 컨테이너 패널
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS)); // 세로 정렬
        listContainer.setBackground(Color.WHITE);


        // 데이터만큼 패널 생성해서 추가
        for (Word w : voca) {
            JPanel rowPanel = createRowItem(w); // 단어 객체 그대로 전달
            listContainer.add(rowPanel);
            listContainer.add(Box.createVerticalStrut(10)); // 아이템 사이 간격 10px
        }
        listContainer.add(Box.createVerticalGlue());

        // 스크롤판에 컨테이너 넣기
        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(new LineBorder(Color.GRAY, 1));

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER); // centerPanel 대신 scrollPane 직접 배치

        add(sideMenu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createRowItem(Word word) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);

        // 여백 및 높이 설정
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

    private void toggleMenu() {
        sideMenu.setVisible(!sideMenu.isVisible());
        revalidate();
        repaint();
    }

    public SideMenu getSideMenu() {
        return sideMenu;
    }

    public static void main(String[] args) {
        // 테스트용 프레임
        ExampleVector v = new ExampleVector();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.add(new HomeInterface(v.voca));
        frame.setVisible(true);
    }
}

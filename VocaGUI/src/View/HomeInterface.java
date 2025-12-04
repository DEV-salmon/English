package View;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class HomeInterface extends JPanel {
    private DefaultListModel<String> listModel;
    private final ImageIcon searchLogo;

    public HomeInterface() {
        ImageIcon originalIcon = new ImageIcon("VocaGUI/src/res/tl.webp");
        Image scaledImg = originalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        searchLogo = new ImageIcon(scaledImg);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout(5, 0)); // 간격 5px
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백
        topPanel.setBackground(Color.WHITE);

        JButton menuBtn = new JButton("☰");
        MakePrettyInterface.setFixedSize(menuBtn,50, 50);
        
        JTextField searchField = new JTextField(" 입력하세요");
        searchField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        
        JButton searchBtn = new JButton(searchLogo);
        MakePrettyInterface.setFixedSize(searchBtn, 50, 50);

        topPanel.add(menuBtn, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER); // 가운데 검색창이 늘어남
        topPanel.add(searchBtn, BorderLayout.EAST);



        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // 상하좌우 여백
        centerPanel.setBackground(Color.WHITE);

        // 2-1. 안내 문구 (헤더)
        JLabel headerLabel = new JLabel("JList 기반 보카 (단어 더블 클릭 시 단어별 통계)");
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        headerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // 아래쪽 여백

        // 2-2. 리스트 데이터 준비 (DefaultListModel 사용)
        listModel = new DefaultListModel<>();
        listModel.addElement("Apple - 사과");
        listModel.addElement("Banana - 바나나");
        listModel.addElement("Computer - 컴퓨터");
        listModel.addElement("Java - 자바");
        listModel.addElement("Polymorphism - 다형성");

        // 2-3. JList 생성
        JList<String> vocaList = new JList<>(listModel);
        vocaList.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        vocaList.setFixedCellHeight(50); // 한 줄 높이 고정
        
        // 2-4. 스크롤바 붙이기 + 테두리
        JScrollPane scrollPane = new JScrollPane(vocaList);
        scrollPane.setBorder(new LineBorder(Color.GRAY, 1)); // 회색 테두리

        centerPanel.add(headerLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);


        // ==========================================
        // [최종] 전체 조립
        // ==========================================
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    public static void main(String[] args) {
        new Test(new HomeInterface());
    }
}

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class VocabularyPanel extends JPanel {
    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"영어", "뜻", "예문"}, 0);
    private final JLabel sessionLabel = new JLabel("단어장을 불러오는 중입니다.");

    public VocabularyPanel(){
        super(new BorderLayout(8, 8));
        JTable table = new JTable(tableModel);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton addButton = new JButton("추가");
        JButton removeButton = new JButton("삭제");
        JButton editButton = new JButton("수정");
        JButton searchButton = new JButton("검색");
        toolbar.add(addButton);
        toolbar.add(removeButton);
        toolbar.add(editButton);
        toolbar.add(searchButton);

        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(sessionLabel, BorderLayout.SOUTH);

        tableModel.addRow(new Object[]{"sample", "뜻, 의미", "여기에 예문을 보여줄 수 있습니다."});

        addButton.addActionListener(e -> showGuide("단어 추가는 FileManager를 통해 voca.txt에 반영하도록 연결하세요."));
        removeButton.addActionListener(e -> showGuide("선택된 행을 삭제하고 저장까지 이어붙이면 됩니다."));
        editButton.addActionListener(e -> showGuide("테이블 셀 편집 또는 다이얼로그로 수정 기능을 구현하세요."));
        searchButton.addActionListener(e -> showGuide("부분 일치 검색 결과만 필터링하도록 확장하면 됩니다."));
    }

    public void setSession(UserFileInfo session){
        sessionLabel.setText(session.getUserId() + " 님의 단어장을 표시할 준비가 되었습니다.");
    }

    private void showGuide(String message){
        javax.swing.JOptionPane.showMessageDialog(
            this,
            message,
            "안내",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    }
}

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class IncorrectManager {
    private final JPanel panel = new JPanel(new BorderLayout(8, 8));
    private final JLabel placeholder = new JLabel("오답 노트가 아직 비어 있습니다.", JLabel.CENTER);

    public IncorrectManager(){
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton reviewButton = new JButton("오답 복습");
        JButton resetButton = new JButton("초기화");
        top.add(reviewButton);
        top.add(resetButton);
        panel.add(top, BorderLayout.NORTH);
        panel.add(placeholder, BorderLayout.CENTER);

        reviewButton.addActionListener(e -> showGuide("Incorrect.txt를 읽어 플래시카드/퀴즈로 보여주도록 연결하세요."));
        resetButton.addActionListener(e -> showGuide("오답 파일을 비우고 화면을 갱신하는 동작을 구현하세요."));
    }

    public JPanel getPanel(){
        return panel;
    }

    public void setSession(UserFileInfo session){
        placeholder.setText(session.getUserId() + " 님의 오답 노트를 불러올 준비가 되었습니다.");
    }

    private void showGuide(String message){
        javax.swing.JOptionPane.showMessageDialog(
            panel,
            message,
            "안내",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    }
}

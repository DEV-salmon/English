import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class StatManager {
    private final JPanel panel = new JPanel(new BorderLayout(8, 8));
    private final JLabel placeholder = new JLabel("아직 통계가 없습니다. 퀴즈를 풀면 데이터를 모을 수 있어요.", JLabel.CENTER);

    public StatManager(){
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton refreshButton = new JButton("새로고침");
        refreshButton.addActionListener(e -> showGuide("stat.txt 내용을 읽어 그래프/표로 렌더링하도록 구현하세요."));
        top.add(refreshButton);

        panel.add(top, BorderLayout.NORTH);
        panel.add(placeholder, BorderLayout.CENTER);
    }

    public JPanel getPanel(){
        return panel;
    }

    public void setSession(UserFileInfo session){
        placeholder.setText(session.getUserId() + " 님의 퀴즈 결과를 보여줄 준비가 되었습니다.");
    }

    private void showGuide(String message){
        javax.swing.JOptionPane.showMessageDialog(
            panel,
            message,
            "통계 안내",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    }
}

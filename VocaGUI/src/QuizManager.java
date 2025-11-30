import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class QuizManager {
    private final JPanel panel = new JPanel(new BorderLayout(8, 8));
    private final JTextArea statusArea = new JTextArea();

    public QuizManager(){
        statusArea.setEditable(false);
        statusArea.setText("퀴즈 준비를 위해 단어장을 불러오는 중입니다.");

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton korEngButton = new JButton("뜻 -> 영어");
        JButton engKorButton = new JButton("영어 -> 뜻");
        JButton exampleButton = new JButton("예문 빈칸");
        JButton spellingButton = new JButton("스펠링");
        toolbar.add(korEngButton);
        toolbar.add(engKorButton);
        toolbar.add(exampleButton);
        toolbar.add(spellingButton);

        korEngButton.addActionListener(e -> openPlaceholder("뜻 -> 영어 퀴즈", "QuizManagement.SUBkorEngQuiz/OBJkorEngQuiz 로직을 붙이세요."));
        engKorButton.addActionListener(e -> openPlaceholder("영어 -> 뜻 퀴즈", "QuizManagement.SUBengKorQuiz/OBJengKorQuiz 로직을 붙이세요."));
        exampleButton.addActionListener(e -> openPlaceholder("예문 빈칸 퀴즈", "exampleQuiz 로직과 예문 낭독 기능을 연동하세요."));
        spellingButton.addActionListener(e -> openPlaceholder("스펠링 퀴즈", "SUBspellingQuiz/OBJspellingQuiz 구현을 이식하세요."));

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(statusArea), BorderLayout.CENTER);
    }

    public JPanel getPanel(){
        return panel;
    }

    public void setSession(UserFileInfo session){
        statusArea.setText(
            """
            %s 님의 단어장으로 퀴즈를 준비하세요.
            - 상단 버튼을 눌러 각 퀴즈 유형을 구현할 수 있습니다.
            - 오답 기록, 힌트, 통계 연동은 여기에서 추가하세요.
            """.formatted(session.getUserId())
        );
    }

    private void openPlaceholder(String title, String guidance){
        javax.swing.JOptionPane.showMessageDialog(
            panel,
            guidance,
            title,
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    }
}

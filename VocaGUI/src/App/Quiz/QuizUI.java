package Quiz;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// 하기 싫다다다다다다다~~~~~
// 간단한 퀴즈 선택 화면
public class QuizUI extends JPanel {
    private final JLabel quizText;
    private final JButton spellingQuiz;
    private final JButton engToKorQuiz;
    private final JButton korToEngQuiz;
    private final JButton exQuiz;
    private final JComboBox<Integer> QuizNumber;
    private final JButton subjectQuiz;
    private final JButton objectQuiz;

    public QuizUI() {
        setLayout(new GridLayout(4, 1, 0, 10));
        setBackground(Color.WHITE);

        quizText = new JLabel("Select Quiz Mode", SwingConstants.CENTER);
        quizText.setFont(new Font("Arial", Font.BOLD, 20));

        spellingQuiz = new JButton("스펠링 퀴즈");
        engToKorQuiz = new JButton("ENG → KOR 퀴즈");
        korToEngQuiz = new JButton("KOR → ENG 퀴즈");
        exQuiz = new JButton("예문 빈칸 퀴즈");
        Integer[] nums = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        QuizNumber = new JComboBox<Integer>(nums);
        subjectQuiz = new JButton("주관식 퀴즈");
        objectQuiz = new JButton("객관식 퀴즈");

        add(quizText);
        add(spellingQuiz);
        add(engToKorQuiz);
        add(korToEngQuiz);
        add(exQuiz);

        
    }

    public JButton getSpellingQuizButton() {
        return spellingQuiz;
    }

    public JButton getEngToKorQuizButton() {
        return engToKorQuiz;
    }

    public JButton getKorToEngQuizButton() {
        return korToEngQuiz;
    }
}

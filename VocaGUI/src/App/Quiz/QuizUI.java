package Quiz;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
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

    public QuizUI() {
        setLayout(new GridLayout(4, 1, 0, 10));
        setBackground(Color.WHITE);

        quizText = new JLabel("Select Quiz Mode", SwingConstants.CENTER);
        quizText.setFont(new Font("Arial", Font.BOLD, 20));

        spellingQuiz = new JButton("Spelling Quiz");
        engToKorQuiz = new JButton("ENG → KOR");
        korToEngQuiz = new JButton("KOR → ENG");

        add(quizText);
        add(spellingQuiz);
        add(engToKorQuiz);
        add(korToEngQuiz);
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

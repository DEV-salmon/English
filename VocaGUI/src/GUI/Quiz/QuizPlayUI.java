package GUI.Quiz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import GUI.Main.SideMenu;
import Signal.Controller;
import Utill.MakePrettyInterface;
import voca.core.Word;

//Quiz목록 보여주기 용
public class QuizPlayUI extends JPanel {

    private final SideMenu sideMenu;
    private final Controller signalHandler;
    private final JPanel listPanel = new JPanel();
    private final JLabel titleLabel = MakePrettyInterface.createTitleLabel("Quiz");
    private Runnable backAction = () -> {};
    private Runnable afterGradeAction = () -> {};
    private boolean objectiveMode = false;
    private Consumer<GradeResult> gradeListener;
    private final List<QuestionRow> questionRows = new ArrayList<>();

    public QuizPlayUI(Controller signalHandler) {
        this.signalHandler = signalHandler;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        sideMenu = new SideMenu(signalHandler);
        sideMenu.setVisible(false);

        JPanel contentPanel = MakePrettyInterface.createContentContainer();

        JPanel topPanel = new JPanel();
        MakePrettyInterface.styleTopBar(topPanel);
        JButton menuBtn = sideMenu.getToggleButton();
        MakePrettyInterface.styleMenuToggleButton(menuBtn);
        topPanel.add(menuBtn);

        JPanel centerBasePanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(centerBasePanel);

        JPanel centerPanel = MakePrettyInterface.createCenterColumn();
        centerBasePanel.add(centerPanel);

        JPanel titleWrapper = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(titleWrapper);
        titleWrapper.add(titleLabel);

        listPanel.setLayout(new javax.swing.BoxLayout(listPanel, javax.swing.BoxLayout.Y_AXIS));
        MakePrettyInterface.makeWhite(listPanel);
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        scrollPane.setBorder(new javax.swing.border.LineBorder(Color.LIGHT_GRAY, 1, true));
        MakePrettyInterface.setFixedSize(scrollPane, 560, 540);

        JPanel btnsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        MakePrettyInterface.makeWhite(btnsPanel);
        JButton gradeBtn = new JButton("채점하기");
        MakePrettyInterface.stylePrimaryButton(gradeBtn, 140, 50);
        gradeBtn.addActionListener(e -> gradeQuiz());
        JButton backBtn = new JButton("뒤로가기");
        MakePrettyInterface.styleSecondaryButton(backBtn);
        MakePrettyInterface.setFixedSize(backBtn, 120, 45);
        backBtn.addActionListener(e -> backAction.run());
        btnsPanel.add(gradeBtn);
        btnsPanel.add(backBtn);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleWrapper);
        centerPanel.add(MakePrettyInterface.spacer(0, 20));
        centerPanel.add(scrollPane);
        centerPanel.add(MakePrettyInterface.spacer(0, 20));
        centerPanel.add(btnsPanel);
        centerPanel.add(Box.createVerticalGlue());

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(centerBasePanel, BorderLayout.CENTER);

        add(sideMenu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void setSideMenuVisible(boolean visible) {
        sideMenu.setVisible(visible);
        MakePrettyInterface.updateScreen(this);
    }

    public void setOnGradeListener(Consumer<GradeResult> listener) {
        this.gradeListener = listener;
    }

    public void setBackAction(Runnable backAction) {
        this.backAction = backAction == null ? () -> {} : backAction;
    }

    public void setAfterGradeAction(Runnable afterGradeAction) {
        this.afterGradeAction = afterGradeAction == null ? () -> {} : afterGradeAction;
    }

    public void showQuestions(String title, List<QuizQuestion> questions, boolean objectiveMode) {
        this.objectiveMode = objectiveMode;
        titleLabel.setText(title);
        listPanel.removeAll();
        questionRows.clear();

        for (int i = 0; i < questions.size(); i++) {
            QuizQuestion q = questions.get(i);
            QuestionRow row;
            if (objectiveMode) {
                row = new ObjectiveRow(i + 1, q);
            } else {
                row = new SubjectiveRow(i + 1, q);
            }
            questionRows.add(row);
            listPanel.add(row.getComponent());
            listPanel.add(MakePrettyInterface.spacer(0, 15));
        }
        listPanel.add(Box.createVerticalGlue());
        listPanel.revalidate();
        listPanel.repaint();
    }

    private void gradeQuiz() {
        int correct = 0;
        List<QuizQuestion> wrongQuestions = new ArrayList<>();
        for (QuestionRow row : questionRows) {
            if (row.isCorrect()) {
                correct++;
            } else {
                wrongQuestions.add(row.getQuestion());
            }
        }
        JOptionPane.showMessageDialog(
                this,
                "점수: " + correct + " / " + questionRows.size(),
                "결과",
                JOptionPane.INFORMATION_MESSAGE
        );
        if (gradeListener != null) {
            gradeListener.accept(new GradeResult(correct, questionRows.size(), wrongQuestions));
        }
        afterGradeAction.run();
    }

    public static class QuizQuestion {
        public final String prompt;
        public final List<String> answers;
        public final List<String> options;
        public final Word sourceWord;
        public final String quizType;

        public QuizQuestion(String prompt, List<String> answers, List<String> options, Word sourceWord, String quizType) {
            this.prompt = prompt;
            this.answers = answers;
            this.options = options;
            this.sourceWord = sourceWord;
            this.quizType = quizType;
        }
    }

    public static class GradeResult {
        public final int correct;
        public final int total;
        public final List<QuizQuestion> wrongQuestions;

        public GradeResult(int correct, int total, List<QuizQuestion> wrongQuestions) {
            this.correct = correct;
            this.total = total;
            this.wrongQuestions = wrongQuestions == null ? new ArrayList<>() : new ArrayList<>(wrongQuestions);
        }
    }

    private abstract static class QuestionRow {
        protected final QuizQuestion question;
        protected final JPanel container = new JPanel();

        protected QuestionRow(int index, QuizQuestion question) {
            this.question = question;
            container.setLayout(new javax.swing.BoxLayout(container, javax.swing.BoxLayout.Y_AXIS));
            MakePrettyInterface.makeWhite(container);
            JLabel title = new JLabel(index + ". " + question.prompt);
            title.setFont(new Font("맑은 고딕", Font.BOLD, 14));
            container.add(title);
            container.setBorder(new javax.swing.border.LineBorder(Color.LIGHT_GRAY, 1, true));
            container.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        }

        public JPanel getComponent() {
            return container;
        }

        public QuizQuestion getQuestion() {
            return question;
        }

        public abstract boolean isCorrect();
    }

    private static class SubjectiveRow extends QuestionRow {
        private final JTextField answerField = new JTextField(20);

        SubjectiveRow(int index, QuizQuestion question) {
            super(index, question);
            MakePrettyInterface.makeShadow(answerField, false);
            JPanel answerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            MakePrettyInterface.makeWhite(answerPanel);
            answerPanel.add(answerField);
            container.add(MakePrettyInterface.spacer(0, 8));
            container.add(answerPanel);
        }

        @Override
        public boolean isCorrect() {
            String user = answerField.getText() == null ? "" : answerField.getText().trim().toLowerCase();
            if (question.answers == null) return false;
            for (String ans : question.answers) {
                if (ans != null && user.equals(ans.trim().toLowerCase())) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class ObjectiveRow extends QuestionRow {
        private final ButtonGroup group = new ButtonGroup();
        private final List<JRadioButton> buttons = new ArrayList<>();

        ObjectiveRow(int index, QuizQuestion question) {
            super(index, question);
            JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
            MakePrettyInterface.makeWhite(optionsPanel);
            if (question.options != null) {
                for (String opt : question.options) {
                    JRadioButton btn = new JRadioButton(opt);
                    btn.setBackground(Color.WHITE);
                    btn.setFocusPainted(false);
                    buttons.add(btn);
                    group.add(btn);
                    optionsPanel.add(btn);
                }
            }
            container.add(MakePrettyInterface.spacer(0, 8));
            container.add(optionsPanel);
        }

        @Override
        public boolean isCorrect() {
            for (JRadioButton btn : buttons) {
                if (btn.isSelected()) {
                    String selected = btn.getText() == null ? "" : btn.getText().trim().toLowerCase();
                    if (question.answers == null) return false;
                    for (String ans : question.answers) {
                        if (ans != null && selected.equals(ans.trim().toLowerCase())) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
}

package GUI.Quiz;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import GUI.Main.GlobalSignal;
import Signal.Controller;
import Signal.Signal;
import voca.core.Word;
import voca.management.IncorrectManagement;
import voca.management.StatManagement;

public class QuizController implements Controller {
    private static final String CARD_SELECT = "SELECT";
    private static final String CARD_AFTER = "AFTER";
    private static final String CARD_PLAY = "PLAY";
    private static final String QUIZ_TYPE_KOR_ENG = "KOR_ENG";
    private static final String QUIZ_TYPE_ENG_KOR = "ENG_KOR";
    private static final String QUIZ_TYPE_EXAMPLE = "EXAMPLE";
    private static final String QUIZ_TYPE_SPELLING = "SPELLING";

    private Vector<Word> vocabulary;
    private boolean menuVisible;
    private final Controller globalHandler;
    private IncorrectManagement incorrectManagement;
    private Runnable incorrectRefreshListener;
    private StatManagement statManagement;

    private final CardLayout layout = new CardLayout();
    private final JPanel root = new JPanel(layout);
    private final QuizUI selector;
    private final AfterSelectionUI afterSelectionUI;
    private final QuizPlayUI quizPlayUI;

    private String currentQuizKey = null;

    public QuizController(Vector<Word> vocabulary, Controller controller) {
        this.vocabulary = vocabulary == null ? new Vector<>() : new Vector<>(vocabulary);
        this.globalHandler = controller;

        this.selector = new QuizUI(this::send);
        this.afterSelectionUI = new AfterSelectionUI(this::send);
        this.quizPlayUI = new QuizPlayUI(this::send);

        root.add(selector, CARD_SELECT);
        root.add(afterSelectionUI, CARD_AFTER);
        root.add(quizPlayUI, CARD_PLAY);

        selector.setSideMenuVisible(menuVisible);
        afterSelectionUI.setSideMenuVisible(menuVisible);
        quizPlayUI.setSideMenuVisible(menuVisible);
        quizPlayUI.setBackAction(this::showAfter);
        quizPlayUI.setOnGradeListener(this::handleGradeResult);
        quizPlayUI.setAfterGradeAction(this::showSelect);

        showSelect();
    }

    public JPanel getView() {
        return root;
    }

    @Override
    public void send(Signal signal, Object data) {
        if (signal instanceof GlobalSignal globalSignal && globalHandler != null) {
            globalHandler.send(globalSignal, data);
            return;
        }
        if (!(signal instanceof QuizSignal quizSignal)) {
            return;
        }

        switch (quizSignal) {
            case SPELLINGQUIZ_BUTTON -> showAfterSelection("SPELLING", "Spelling Quiz");
            case ENG_TO_KORQUIZ_BUTTON -> showAfterSelection("ENG_TO_KOR", "ENG → KOR Quiz");
            case KOR_TO_ENGQUIZ_BUTTON -> showAfterSelection("KOR_TO_ENG", "KOR → ENG Quiz");
            case EXQUIZ_BUTTON -> showAfterSelection("EX", "예문 빈칸 퀴즈");
            case SUBJECTQUIZ_BUTTON, OBJECTQUIZ_BUTTON -> handleQuizStart(quizSignal, data);
            case COMBOBOX_SELECT -> { }
            default -> { }
        }
    }

    private void handleQuizStart(QuizSignal quizSignal, Object data) {
        if (!(data instanceof Integer count) || count <= 0) {
            JOptionPane.showMessageDialog(root, "퀴즈 개수를 제대로 입력하세요.", "안내", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean objectiveMode = quizSignal == QuizSignal.OBJECTQUIZ_BUTTON;
        List<QuizPlayUI.QuizQuestion> questions = buildQuestions(currentQuizKey, count, objectiveMode);
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(root, "출제할 단어가 없습니다. 단어를 추가하거나 보카를 불러오세요.", "안내", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        quizPlayUI.showQuestions(getTitleForKey(currentQuizKey), questions, objectiveMode);
        layout.show(root, CARD_PLAY);
    }

    private void showAfterSelection(String key, String title) {
        currentQuizKey = key;
        afterSelectionUI.showQuizPanel(key, title);
        layout.show(root, CARD_AFTER);
    }

    private void showAfter() {
        layout.show(root, CARD_AFTER);
    }

    private String getTitleForKey(String key) {
        if (key == null) return "Quiz";
        return switch (key) {
            case "SPELLING" -> "Spelling Quiz";
            case "ENG_TO_KOR" -> "ENG → KOR Quiz";
            case "KOR_TO_ENG" -> "KOR → ENG Quiz";
            case "EX" -> "예문 빈칸 퀴즈";
            default -> "Quiz";
        };
    }

    private List<QuizPlayUI.QuizQuestion> buildQuestions(String key, int count, boolean objectiveMode) {
        List<QuizPlayUI.QuizQuestion> list = new ArrayList<>();
        if (vocabulary == null || vocabulary.isEmpty() || count <= 0) {
            return list;
        }
        Random random = new Random();
        List<Word> pool = new ArrayList<>(vocabulary);
        Collections.shuffle(pool, random);
        int limit = Math.min(count, pool.size());
        String typeSuffix = objectiveMode ? " - OBJECTIVE" : " - SUBJECTIVE";

        for (int i = 0; i < limit; i++) {
            Word w = pool.get(i);
            String prompt;
            List<String> answers = new ArrayList<>();
            List<String> options = null;
            String quizType;

            String eng = w.getEng();
            List<String> korList = new ArrayList<>();
            Collections.addAll(korList, w.getKor());
            String korJoined = String.join(", ", korList);

            if ("ENG_TO_KOR".equals(key)) {
                prompt = eng;
                answers.add(korJoined);
                answers.addAll(korList);
                if (objectiveMode) options = buildOptionsKor(korJoined, pool, random);
                quizType = QUIZ_TYPE_ENG_KOR + typeSuffix;
            } else if ("KOR_TO_ENG".equals(key)) {
                prompt = korJoined;
                answers.add(eng);
                if (objectiveMode) options = buildOptionsEng(eng, pool, random);
                quizType = QUIZ_TYPE_KOR_ENG + typeSuffix;
            } else if ("EX".equals(key)) {
                String ex = w.getEx();
                if (ex != null && !ex.trim().isEmpty()) {
                    if (ex.contains(eng)) {
                        prompt = ex.replace(eng, "_____");
                    } else {
                        prompt = ex + " (빈칸 단어: " + eng + ")";
                    }
                } else {
                    prompt = "예문이 없습니다. 단어: " + eng;
                }
                answers.add(eng);
                if (objectiveMode) options = buildOptionsEng(eng, pool, random);
                quizType = QUIZ_TYPE_EXAMPLE + typeSuffix;
            } else if ("SPELLING".equals(key)) {
                prompt = "🎧 들리는 단어의 스펠링을 입력하세요.";
                answers.add(eng);
                if (objectiveMode) options = buildOptionsEng(eng, pool, random);
                quizType = QUIZ_TYPE_SPELLING + typeSuffix;
            } else {
                prompt = eng;
                answers.add(korJoined);
                answers.addAll(korList);
                if (objectiveMode) options = buildOptionsKor(korJoined, pool, random);
                quizType = QUIZ_TYPE_ENG_KOR + typeSuffix;
            }
            list.add(new QuizPlayUI.QuizQuestion(prompt, answers, options, w, quizType));
        }
        return list;
    }

    private List<String> buildOptionsEng(String answer, List<Word> pool, Random random) {
        List<String> opts = new ArrayList<>();
        opts.add(answer);
        for (Word w : pool) {
            if (opts.size() >= 4) break;
            String opt = w.getEng();
            if (opt != null && !opts.contains(opt)) {
                opts.add(opt);
            }
        }
        Collections.shuffle(opts, random);
        return opts;
    }

    private List<String> buildOptionsKor(String answer, List<Word> pool, Random random) {
        List<String> opts = new ArrayList<>();
        opts.add(answer);
        for (Word w : pool) {
            if (opts.size() >= 4) break;
            String opt = String.join(", ", w.getKor());
            if (opt != null && !opt.isEmpty() && !opts.contains(opt)) {
                opts.add(opt);
            }
        }
        Collections.shuffle(opts, random);
        return opts;
    }

    public void showSelect() {
        layout.show(root, CARD_SELECT);
    }

    public void toggleMenu() {
        menuVisible = !menuVisible;
        selector.setSideMenuVisible(menuVisible);
        afterSelectionUI.setSideMenuVisible(menuVisible);
        quizPlayUI.setSideMenuVisible(menuVisible);
    }

    public void updateVocabulary(Vector<Word> newVoca) {
        this.vocabulary = newVoca == null ? new Vector<>() : new Vector<>(newVoca);
    }

    public void setIncorrectManagement(IncorrectManagement incorrectManagement) {
        this.incorrectManagement = incorrectManagement;
    }

    public void setIncorrectUpdateListener(Runnable listener) {
        this.incorrectRefreshListener = listener;
    }

    public void setStatManagement(StatManagement statManagement) {
        this.statManagement = statManagement;
    }

    private void handleGradeResult(QuizPlayUI.GradeResult result) {
        if (result == null) {
            return;
        }
        if (incorrectManagement != null && result.wrongQuestions != null && !result.wrongQuestions.isEmpty()) {
            for (QuizPlayUI.QuizQuestion question : result.wrongQuestions) {
                if (question == null || question.sourceWord == null) {
                    continue;
                }
                incorrectManagement.recordIncorrect(question.sourceWord, question.quizType);
            }
            if (incorrectRefreshListener != null) {
                incorrectRefreshListener.run();
            }
        }
        updateStats(result);
    }

    private void updateStats(QuizPlayUI.GradeResult result) {
        if (statManagement == null || currentQuizKey == null) {
            return;
        }
        int correct = Math.max(0, result.correct);
        int wrong = Math.max(0, result.total - result.correct);
        switch (currentQuizKey) {
            case "ENG_TO_KOR" -> {
                for (int i = 0; i < correct; i++) statManagement.addEngKorCorrect();
                for (int i = 0; i < wrong; i++) statManagement.addEngKorWrong();
            }
            case "KOR_TO_ENG" -> {
                for (int i = 0; i < correct; i++) statManagement.addKorEngCorrect();
                for (int i = 0; i < wrong; i++) statManagement.addKorEngWrong();
            }
            case "EX" -> {
                for (int i = 0; i < correct; i++) statManagement.addExampleCorrect();
                for (int i = 0; i < wrong; i++) statManagement.addExampleWrong();
            }
            case "SPELLING" -> {
                for (int i = 0; i < correct; i++) statManagement.addSpellingCorrect();
                for (int i = 0; i < wrong; i++) statManagement.addSpellingWrong();
            }
            default -> { }
        }
    }
}

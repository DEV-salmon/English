package GUI.Quiz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import GUI.Main.SideMenu;
import Signal.Controller;
import Utill.MakePrettyInterface;

// 하기 싫다다다다다다다~~~~~
// 간단한 퀴즈 선택 화면
public class QuizUI extends JPanel {
    private final Controller signalHandler;
    private final SideMenu sideMenu;
    private final JLabel quizText;
    private final JButton spellingQuiz;
    private final JButton engToKorQuiz;
    private final JButton korToEngQuiz;
    private final JButton exQuiz;
    private final JComboBox<Integer> QuizNumber;
    private final JButton subjectQuiz;
    private final JButton objectQuiz;
    private Font btnFont = new Font("맑은 고딕",Font.BOLD,12);

    public QuizUI(Controller signalHandler) {
        this.signalHandler = signalHandler;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setOpaque(true);

        sideMenu = new SideMenu(signalHandler);
        sideMenu.setVisible(false);
        JPanel contentPanel = new JPanel(new BorderLayout());
        MakePrettyInterface.makeWhite(contentPanel);

        JPanel btns = new JPanel(new GridLayout(2,2,50,50));
        MakePrettyInterface.makeWhite(btns);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new LineBorder(Color.BLACK,1, true));
        MakePrettyInterface.makeWhite(topPanel);
        topPanel.setBackground(Color.WHITE);
        topPanel.setOpaque(true);
        quizText = new JLabel("Quiz Mode");
        quizText.setFont(new Font("맑은 고딕", Font.BOLD, 40));

        JPanel centerBasePanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(centerBasePanel);
        JPanel centerPanel = new JPanel();
        MakePrettyInterface.makeWhite(centerPanel);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        JPanel centerTopPanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(centerTopPanel);

        //메뉴 버튼 만들기
        JButton menuBtn = sideMenu.getToggleButton();

        //하단 퀴즈 버튼
        spellingQuiz = new JButton("스펠링 퀴즈");
        btnSetting(spellingQuiz);
        spellingQuiz.addActionListener(e -> signalHandler.send(QuizSignal.SPELLINGQUIZ_BUTTON, null));
        engToKorQuiz = new JButton("ENG → KOR 퀴즈");
        engToKorQuiz.addActionListener(e -> signalHandler.send(QuizSignal.ENG_TO_KORQUIZ_BUTTON, null));
        btnSetting(engToKorQuiz);
        korToEngQuiz = new JButton("KOR → ENG 퀴즈");
        korToEngQuiz.addActionListener(e -> signalHandler.send(QuizSignal.KOR_TO_ENGQUIZ_BUTTON, null));
        btnSetting(korToEngQuiz);
        exQuiz = new JButton("예문 빈칸 퀴즈");
        exQuiz.addActionListener(e -> signalHandler.send(QuizSignal.EXQUIZ_BUTTON, null));
        btnSetting(exQuiz);

        Integer[] nums = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        QuizNumber = new JComboBox<Integer>(nums);
        subjectQuiz = new JButton("주관식 퀴즈");
        objectQuiz = new JButton("객관식 퀴즈");

        add(sideMenu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        contentPanel.add(topPanel,BorderLayout.NORTH);
        topPanel.add(menuBtn);
        contentPanel.add(centerBasePanel, BorderLayout.CENTER);
        centerBasePanel.add(centerPanel);
        centerPanel.add(centerTopPanel);
        centerTopPanel.add(quizText);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0,70)));

        centerPanel.add(btns);
        btns.add(spellingQuiz);
        btns.add(engToKorQuiz);
        btns.add(korToEngQuiz);
        btns.add(exQuiz);

        
    }
    
    private void btnSetting(JButton btn){
        btn.setFont(btnFont);
        MakePrettyInterface.setFixedSize(btn, 200, 70);
        btn.setBackground(Color.WHITE);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(Color.BLACK, 1, true));
        MakePrettyInterface.makeShadow(btn);
    }

    public void setSideMenuVisible(boolean visible){
        sideMenu.setVisible(visible);
        MakePrettyInterface.updateScreen(this);
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

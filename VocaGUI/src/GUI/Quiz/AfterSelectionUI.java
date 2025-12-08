package GUI.Quiz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import GUI.Main.SideMenu;
import Signal.Controller;
import Utill.MakePrettyInterface;

//카드레이아웃을 사용해서 구현. MakePrettyInterface를 통한 통일감있는 UI실현~!!
public class AfterSelectionUI extends JPanel {

    //항상있는 시그널핸들러와 사이드메뉴 타이틀라벨 설정
    private final Controller signalHandler;
    private final SideMenu sideMenu;
    private final JLabel titleLabel = MakePrettyInterface.createTitleLabel("Quiz");

    //생성자
    public AfterSelectionUI(Controller signalHandler) {
        //시그널핸들러 받기
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

        JPanel card = buildQuizCard();
        centerPanel.add(card);

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(centerBasePanel, BorderLayout.CENTER);

        add(sideMenu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void setSideMenuVisible(boolean visible) {
        sideMenu.setVisible(visible);
        MakePrettyInterface.updateScreen(this);
    }

    // 상단 제목을 바꿀 때 호출
    public void showQuizPanel(String key, String titleText) {
        titleLabel.setText(titleText);
    }

    // 중앙에 제목과 버튼 두 개를 붙인 패널을 만든다
    private JPanel buildQuizCard() {
        JPanel card = MakePrettyInterface.createCenterColumn();

        JPanel titleWrapper = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(titleWrapper);
        titleWrapper.add(titleLabel);

        JButton subjectButton = new JButton("주관식 퀴즈");
        MakePrettyInterface.stylePrimaryButton(subjectButton);
        subjectButton.addActionListener(e -> handleQuizSelection(QuizSignal.SUBJECTQUIZ_BUTTON));

        JButton objectButton = new JButton("객관식 퀴즈");
        MakePrettyInterface.stylePrimaryButton(objectButton);
        objectButton.addActionListener(e -> handleQuizSelection(QuizSignal.OBJECTQUIZ_BUTTON));

        JPanel buttonRow = new JPanel(new GridLayout(1, 2, 40, 0));
        MakePrettyInterface.makeWhite(buttonRow);
        buttonRow.add(subjectButton);
        buttonRow.add(objectButton);
        buttonRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(titleWrapper);
        card.add(MakePrettyInterface.spacer(0, 60));
        card.add(buttonRow);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private void handleQuizSelection(QuizSignal quizSignal) {
        Integer count = askQuizCount();
        if (count == null) return;
        if (signalHandler != null) {
            signalHandler.send(quizSignal, count);
        }
    }

    // 다이얼로그로 퀴즈 개수를 입력받는다
    private Integer askQuizCount() {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Label.background", Color.WHITE);

        JTextField countField = new JTextField(10);
        MakePrettyInterface.makeShadow(countField, false);
        JLabel label = new JLabel("풀이할 퀴즈 개수");
        label.setFont(new Font("맑은 고딕", Font.BOLD, 12));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        MakePrettyInterface.makeWhite(inputPanel);
        inputPanel.add(label);
        inputPanel.add(countField);

        JButton okBtn = new JButton("확인");
        JButton cancelBtn = new JButton("취소");
        prepareDialogButton(okBtn);
        prepareDialogButton(cancelBtn);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        MakePrettyInterface.makeWhite(buttonPanel);
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);

        JPanel basePanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(basePanel);
        basePanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        JPanel dialogColumn = MakePrettyInterface.createCenterColumn();
        basePanel.add(dialogColumn);
        dialogColumn.add(Box.createVerticalStrut(20));
        dialogColumn.add(inputPanel);
        dialogColumn.add(Box.createVerticalStrut(10));
        dialogColumn.add(buttonPanel);

        final int[] resultState = {JOptionPane.CANCEL_OPTION};
        okBtn.addActionListener(e -> {
            resultState[0] = JOptionPane.OK_OPTION;
            SwingUtilities.getWindowAncestor(okBtn).dispose();
        });
        cancelBtn.addActionListener(e -> {
            resultState[0] = JOptionPane.CANCEL_OPTION;
            SwingUtilities.getWindowAncestor(cancelBtn).dispose();
        });

        JOptionPane.showOptionDialog(
                this,
                basePanel,
                "퀴즈 개수 설정",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{},
                null
        );

        if (resultState[0] != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            int value = Integer.parseInt(countField.getText().trim());
            if (value <= 0) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "1 이상의 숫자를 입력하세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private void prepareDialogButton(JButton button) {
        MakePrettyInterface.setFixedSize(button, 60, 25);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        MakePrettyInterface.makeWhite(button);
        MakePrettyInterface.makeShadow(button, false);
        button.setFocusPainted(false);
    }
}

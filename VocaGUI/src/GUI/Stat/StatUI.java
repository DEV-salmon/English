package GUI.Stat;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import GUI.Main.SideMenu;
import Signal.Controller;
import Utill.MakePrettyInterface;

public class StatUI extends JPanel {

    private final Controller signalHandler;
    private final SideMenu sideMenu;

    private final JButton showStatButton;
    private final JButton saveStatButton;
    private final JComboBox<String> quizTypeBox;
    private final JTextArea statSummaryArea;

    public StatUI(Controller signalHandler) {
        this.signalHandler = signalHandler;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        sideMenu = new SideMenu(signalHandler);
        sideMenu.setVisible(false);

        JPanel contentPanel = new JPanel(new BorderLayout());
        MakePrettyInterface.makeWhite(contentPanel);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new LineBorder(Color.BLACK, 1, true));
        MakePrettyInterface.makeWhite(topPanel);

        JButton menuBtn = sideMenu.getToggleButton();
        topPanel.add(menuBtn);

        JLabel titleLabel = new JLabel("Stat Mode");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 40));

        showStatButton = new JButton("통계 그래프 생성");
        showStatButton.addActionListener(e -> sendSignal(StatSignal.STAT_SHOW_BUTTON, null));
        styleButton(showStatButton);

        saveStatButton = new JButton("통계 파일 저장");
        saveStatButton.addActionListener(e -> sendSignal(StatSignal.STAT_SAVE_BUTTON, null));
        styleButton(saveStatButton);

        quizTypeBox = new JComboBox<>(new String[]{
                "뜻 맞추기 (한→영)",
                "뜻 맞추기 (영→한)",
                "예문 퀴즈",
                "스펠링 퀴즈"
        });
        quizTypeBox.addActionListener(e ->
                sendSignal(StatSignal.STAT_QUIZTYPE_SELECT, quizTypeBox.getSelectedItem())
        );
        quizTypeBox.setBackground(Color.WHITE);
        quizTypeBox.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        quizTypeBox.setBorder(new LineBorder(Color.BLACK, 1, true));
        MakePrettyInterface.setFixedSize(quizTypeBox, 220, 35);

        statSummaryArea = new JTextArea("통계 화면");
        statSummaryArea.setEditable(false);
        statSummaryArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        statSummaryArea.setLineWrap(true);
        statSummaryArea.setWrapStyleWord(true);
        statSummaryArea.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));

        JScrollPane summaryScroll = new JScrollPane(statSummaryArea);
        summaryScroll.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        summaryScroll.setPreferredSize(new Dimension(440, 220));
        summaryScroll.setAlignmentX(CENTER_ALIGNMENT);

        JPanel quizTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        MakePrettyInterface.makeWhite(quizTypePanel);
        JLabel quizTypeLabel = new JLabel("퀴즈 유형");
        quizTypeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        quizTypePanel.add(quizTypeLabel);
        quizTypePanel.add(quizTypeBox);
        quizTypePanel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        MakePrettyInterface.makeWhite(actionPanel);
        actionPanel.add(showStatButton);
        actionPanel.add(saveStatButton);
        actionPanel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel statCard = new JPanel();
        statCard.setLayout(new BoxLayout(statCard, BoxLayout.Y_AXIS));
        MakePrettyInterface.makeWhite(statCard);
        statCard.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        MakePrettyInterface.setFixedSize(statCard, 500, 420);
        statCard.setMaximumSize(new Dimension(520, Integer.MAX_VALUE));
        statCard.setAlignmentX(CENTER_ALIGNMENT);
        statCard.add(Box.createVerticalStrut(20));
        statCard.add(quizTypePanel);
        statCard.add(Box.createVerticalStrut(20));
        statCard.add(actionPanel);
        statCard.add(Box.createVerticalStrut(20));
        statCard.add(summaryScroll);
        statCard.add(Box.createVerticalGlue());

        JPanel centerTopPanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(centerTopPanel);
        centerTopPanel.add(titleLabel);

        JPanel centerPanel = new JPanel();
        MakePrettyInterface.makeWhite(centerPanel);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(centerTopPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        centerPanel.add(statCard);

        JPanel centerBasePanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(centerBasePanel);
        centerBasePanel.add(centerPanel);

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(centerBasePanel, BorderLayout.CENTER);

        add(sideMenu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void setSideMenuVisible(boolean visible) {
        sideMenu.setVisible(visible);
        MakePrettyInterface.updateScreen(this);
    }

    public void updateSummary(String text) {
        statSummaryArea.setText(text);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void sendSignal(StatSignal statSignal, Object payload) {
        signalHandler.send(statSignal, payload);
    }

    private void styleButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(Color.BLACK, 1, true));
        MakePrettyInterface.setFixedSize(button, 200, 60);
        MakePrettyInterface.makeShadow(button, false);
    }
}

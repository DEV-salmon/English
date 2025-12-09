package GUI.Stat;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.JButton;

import GUI.Main.SideMenu;
import Signal.Controller;
import Utill.MakePrettyInterface;

public class StatUI extends JPanel {

    private final Controller signalHandler;
    private final SideMenu sideMenu;

    private final JTextArea statSummaryArea;
    private final JLabel chartLabel;
    private final CardLayout contentCardLayout;
    private final JPanel contentCardPanel;
    private static final String CARD_SUMMARY = "SUMMARY";
    private static final String CARD_CHART = "CHART";

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

        JLabel infoLabel = new JLabel("Stat 화면을 열면 통계와 그래프가 자동으로 갱신됩니다.");
        infoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        infoLabel.setAlignmentX(CENTER_ALIGNMENT);

        JButton summaryButton = new JButton("요약 보기");
        styleSwitchButton(summaryButton);
        JButton chartButton = new JButton("그래프 보기");
        styleSwitchButton(chartButton);

        statSummaryArea = new JTextArea("통계를 불러오는 중입니다...");
        statSummaryArea.setEditable(false);
        statSummaryArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        statSummaryArea.setLineWrap(true);
        statSummaryArea.setWrapStyleWord(true);
        statSummaryArea.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));

        JScrollPane summaryScroll = new JScrollPane(statSummaryArea);
        summaryScroll.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        summaryScroll.setPreferredSize(new Dimension(440, 220));
        summaryScroll.setAlignmentX(CENTER_ALIGNMENT);

        chartLabel = new JLabel("그래프를 불러오는 중입니다.");
        chartLabel.setHorizontalAlignment(JLabel.CENTER);
        chartLabel.setVerticalAlignment(JLabel.CENTER);
        chartLabel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        chartLabel.setOpaque(true);
        chartLabel.setBackground(Color.WHITE);

        JPanel chartPanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(chartPanel);
        chartPanel.add(chartLabel);

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        MakePrettyInterface.makeWhite(summaryPanel);
        summaryPanel.add(summaryScroll);

        contentCardLayout = new CardLayout();
        contentCardPanel = new JPanel(contentCardLayout);
        contentCardPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        contentCardPanel.setPreferredSize(new Dimension(460, 320));
        contentCardPanel.setBackground(Color.WHITE);
        contentCardPanel.add(summaryPanel, CARD_SUMMARY);
        contentCardPanel.add(chartPanel, CARD_CHART);

        JPanel statCard = new JPanel();
        statCard.setLayout(new BoxLayout(statCard, BoxLayout.Y_AXIS));
        MakePrettyInterface.makeWhite(statCard);
        statCard.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        MakePrettyInterface.setFixedSize(statCard, 520, 520);
        statCard.setMaximumSize(new Dimension(540, Integer.MAX_VALUE));
        statCard.setAlignmentX(CENTER_ALIGNMENT);
        statCard.add(Box.createVerticalStrut(20));
        statCard.add(infoLabel);
        statCard.add(Box.createVerticalStrut(20));
        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        MakePrettyInterface.makeWhite(switchPanel);
        switchPanel.add(summaryButton);
        switchPanel.add(chartButton);
        statCard.add(switchPanel);
        statCard.add(Box.createVerticalStrut(10));
        statCard.add(contentCardPanel);
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

        summaryButton.addActionListener(e -> showSummaryCard());
        chartButton.addActionListener(e -> showChartCard());
        showSummaryCard();
    }

    public void setSideMenuVisible(boolean visible) {
        sideMenu.setVisible(visible);
        MakePrettyInterface.updateScreen(this);
    }

    public void updateSummary(String text) {
        statSummaryArea.setText(text);
    }

    public void updateChart(String chartPath) {
        if (chartPath == null) {
            chartLabel.setIcon(null);
            chartLabel.setText("그래프를 표시하려면 로그인 후 퀴즈를 풀어주세요.");
            MakePrettyInterface.updateScreen(this);
            return;
        }

        ImageIcon icon = new ImageIcon(chartPath);
        if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            chartLabel.setIcon(null);
            chartLabel.setText("그래프 파일을 찾을 수 없습니다.");
        } else {
            int targetWidth = 420;
            int targetHeight = 260;
            Image scaled = icon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            chartLabel.setPreferredSize(new Dimension(targetWidth, targetHeight));
            chartLabel.setText("");
            chartLabel.setIcon(new ImageIcon(scaled));
        }
        MakePrettyInterface.updateScreen(this);
    }

    public void showSummaryCard() {
        contentCardLayout.show(contentCardPanel, CARD_SUMMARY);
        MakePrettyInterface.updateScreen(this);
    }

    public void showChartCard() {
        contentCardLayout.show(contentCardPanel, CARD_CHART);
        MakePrettyInterface.updateScreen(this);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void sendSignal(StatSignal statSignal, Object payload) {
        signalHandler.send(statSignal, payload);
    }

    private void styleSwitchButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(Color.BLACK, 1, true));
        MakePrettyInterface.setFixedSize(button, 120, 36);
        MakePrettyInterface.makeShadow(button, false);
    }
}

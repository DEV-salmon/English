package GUI.Stat;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Signal.SignalListener;

public class StatUI extends JFrame {

    private SignalListener listener;

    private JButton showStatButton;
    private JButton saveStatButton;
    private JComboBox<String> quizTypeBox;

    public StatUI(String title, SignalListener listener) {
        super(title);
        this.listener = listener;

        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initLayout();

        this.setVisible(true);
    }

    private void initLayout() {
        this.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));

        showStatButton = new JButton("통계 그래프 보기");
        showStatButton.addActionListener(e ->
                listener.onSignal(StatSignal.STAT_SHOW_BUTTON, null)
        );

        saveStatButton = new JButton("통계 파일 저장");
        saveStatButton.addActionListener(e ->
                listener.onSignal(StatSignal.STAT_SAVE_BUTTON, null)
        );

        quizTypeBox = new JComboBox<>(new String[]{
                "뜻 맞추기 (한→영)",
                "뜻 맞추기 (영→한)",
                "예문 퀴즈",
                "스펠링 퀴즈"
        });
        quizTypeBox.addActionListener(e ->
                listener.onSignal(StatSignal.STAT_QUIZTYPE_SELECT, quizTypeBox.getSelectedItem())
        );

        centerPanel.add(showStatButton);
        centerPanel.add(saveStatButton);
        centerPanel.add(quizTypeBox);

        this.add(centerPanel, BorderLayout.CENTER);
    }
}

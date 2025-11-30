import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class DashboardPanel extends JPanel {
    private final CardLayout contentLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(contentLayout);
    private final JLabel userLabel = new JLabel();
    private final VocabularyPanel vocabularyPanel = new VocabularyPanel();
    private final QuizManager quizManager;
    private final StatManager statManager;
    private final IncorrectManager incorrectManager;
    private UserFileInfo session;

    public DashboardPanel(
        UserFileInfo session,
        QuizManager quizManager,
        StatManager statManager,
        IncorrectManager incorrectManager,
        Runnable onLogout
    ){
        super(new BorderLayout());
        this.session = session;
        this.quizManager = quizManager;
        this.statManager = statManager;
        this.incorrectManager = incorrectManager;

        JPanel sidebar = buildSidebar(onLogout);
        sidebar.setPreferredSize(new Dimension(180, 0));
        add(sidebar, BorderLayout.WEST);

        contentPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        contentPanel.add(buildWelcomePanel(), "WELCOME");
        contentPanel.add(vocabularyPanel, "VOCAB");
        contentPanel.add(quizManager.getPanel(), "QUIZ");
        contentPanel.add(statManager.getPanel(), "STAT");
        contentPanel.add(incorrectManager.getPanel(), "INCORRECT");
        add(contentPanel, BorderLayout.CENTER);

        applySession();
        contentLayout.show(contentPanel, "WELCOME");
    }

    public void setSession(UserFileInfo session){
        this.session = session;
        applySession();
        contentLayout.show(contentPanel, "WELCOME");
    }

    private JPanel buildSidebar(Runnable onLogout){
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel header = new JPanel(new BorderLayout());
        header.add(new JLabel("VOCAGUI"), BorderLayout.NORTH);
        header.add(userLabel, BorderLayout.SOUTH);
        sidebar.add(header, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        buttons.setPreferredSize(new Dimension(160, 300));
        buttons.add(navButton("홈", "WELCOME"));
        buttons.add(navButton("단어장", "VOCAB"));
        buttons.add(navButton("퀴즈", "QUIZ"));
        buttons.add(navButton("통계", "STAT"));
        buttons.add(navButton("오답노트", "INCORRECT"));

        JButton logoutButton = new JButton("로그아웃");
        logoutButton.addActionListener(e -> onLogout.run());
        buttons.add(logoutButton);

        sidebar.add(buttons, BorderLayout.CENTER);
        return sidebar;
    }

    private JButton navButton(String text, String targetCard){
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 32));
        button.addActionListener(e -> contentLayout.show(contentPanel, targetCard));
        return button;
    }

    private JPanel buildWelcomePanel(){
        JPanel panel = new JPanel(new BorderLayout());
        JLabel message = new JLabel(
            "<html><body style='padding:8px'>"
            + "메뉴를 선택해 단어 등록, 퀴즈, 통계, 오답노트 기능을 이어서 구현하세요."
            + "</body></html>",
            JLabel.CENTER
        );
        panel.add(message, BorderLayout.CENTER);
        return panel;
    }

    private void applySession(){
        if(session == null){
            userLabel.setText("로그인 필요");
            return;
        }
        userLabel.setText(session.getUserId() + " 님");
        vocabularyPanel.setSession(session);
        quizManager.setSession(session);
        statManager.setSession(session);
        incorrectManager.setSession(session);
    }
}

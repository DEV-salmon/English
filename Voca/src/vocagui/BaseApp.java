package vocagui;

import voca.auth.LogInManagement;
import vocagui.core.UserFileInfo;
import vocagui.login.LoginManager;
import vocagui.login.LoginPanel;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Image;

public class BaseApp extends JFrame{
    private final CardLayout viewLayout = new CardLayout();
    private final JPanel viewContainer = new JPanel(viewLayout);
    private final FileManager fileManager = new FileManager();
    private final LoginManager loginManager = new LoginManager("Voca/src/res/LoginList",fileManager);
    private LoginPanel loginPanel;
    private final QuizManager quizManager = new QuizManager();
    private final StatManager statManager = new StatManager();
    private final IncorrectManager incorrectManager = new IncorrectManager();
    private DashboardPanel dashboardPanel;

    BaseApp(){
        //제목
        super("2조 단어장");
        //
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 650);
        setIconImage(loadLogo());
        setLocationRelativeTo(null);

        this.loginPanel = new LoginPanel(loginManager, this::handleLoginSuccess);
        viewContainer.add(loginPanel, "LOGIN");

        add(viewContainer, BorderLayout.CENTER);
        viewLayout.show(viewContainer, "LOGIN");
        setVisible(true);
    }

    private Image loadLogo(){
        ImageIcon logo = new ImageIcon("src/res/Logo.png");
        return logo.getImage();
    }

    private void handleLoginSuccess(UserFileInfo session){
        if(dashboardPanel == null){
            dashboardPanel = new DashboardPanel(
                session,
                quizManager,
                statManager,
                incorrectManager,
                this::showLoginScreen
            );
            viewContainer.add(dashboardPanel, "DASHBOARD");
        }else{
            dashboardPanel.setSession(session);
        }
        viewLayout.show(viewContainer, "DASHBOARD");
    }

    void showLoginScreen(){
        viewLayout.show(viewContainer, "LOGIN");
        if (loginPanel != null) {
            loginPanel.resetStatus("아이디와 비밀번호를 입력해주세요.");
        }
    }
}

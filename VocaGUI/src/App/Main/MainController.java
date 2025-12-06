package Main;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import Home.HomeController;
import Home.HomeSignal;
import Login.LoginController;
import Login.LoginSignal;
import Quiz.QuizController;
import Signal.Controller;
import Signal.Signal;
import Stat.StatController;
import Test.ExampleVector;

public class MainController implements Controller {
    private static final String CARD_LOGIN = "login";
    private static final String CARD_HOME = "home";

    private final HomeController homeController;
    private final LoginController loginController;
    private final StatController statController;
    private final QuizController quizController;

    private final JFrame frame;
    private final JPanel rootPanel;
    private final CardLayout cardLayout;
    
    public MainController(){
        this.frame = new JFrame("VocaGUI");
        this.cardLayout = new CardLayout();
        this.rootPanel = new JPanel(cardLayout);

        ExampleVector exampleVector = new ExampleVector();
        this.homeController = new HomeController(exampleVector.voca);
        this.loginController = new LoginController(this);
        this.statController = new StatController();
        this.quizController = new QuizController();

        rootPanel.add(loginController.getView(), CARD_LOGIN);
        rootPanel.add(homeController.getView(), CARD_HOME);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setContentPane(rootPanel);
    }

    public void start() {
        frame.setVisible(true);
        showLogin();
    }

    private void showLogin() {
        cardLayout.show(rootPanel, CARD_LOGIN);
    }

    private void showHome() {
        cardLayout.show(rootPanel, CARD_HOME);
    }

    @Override
    public void send(Signal s, Object d) {
        if (s instanceof HomeSignal) {
            homeController.send(s, d);
            return;
        }
        if (s instanceof LoginSignal loginSignal) {
            handleLogin(loginSignal, d);
            return;
        }
    }

    private void handleLogin(LoginSignal signal, Object data) {
        switch (signal) {
            case LOGIN_SUCCESS:
                showHome();
                break;
            case LOGIN_FAIL:
                JOptionPane.showMessageDialog(frame, "로그인 실패: 아이디와 비밀번호를 확인하세요.");
                break;
            case REGISTER:
                JOptionPane.showMessageDialog(frame, "회원가입 기능은 준비 중입니다.");
                break;
            case LOGIN:
            default:
                break;
        }
    }

    public static void main(String[] args) {
        new MainController();
    }
}

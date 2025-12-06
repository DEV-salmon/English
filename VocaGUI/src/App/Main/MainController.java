package Main;
import java.awt.CardLayout;
import javax.swing.JFrame;
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
import Main.GlobalSignal;

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
        this.homeController = new HomeController(exampleVector.voca, this);
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
        if (s instanceof GlobalSignal globalSignal) {
            handleGlobal(globalSignal, d);
            return;
        }
        if (s instanceof HomeSignal) {
            homeController.send(s, d);
            return;
        }
        if (s instanceof LoginSignal) {
            loginController.send(s, d);
            return;
        }
    }

    private void handleGlobal(GlobalSignal signal, Object data) {
        switch (signal) {
            case TOGGLE_MENU:
                homeController.send(HomeSignal.TOGGLE_MENU, null);
                break;
            case HOME:
                showHome();
                break;
            case FILE:
            case STAT:
            case QUIZ:
                break;
            case LOGOUT:
                showLogin();
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainController().start());
    }
}

package GUI.Main;

import java.awt.CardLayout;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import GUI.Home.HomeController;
import GUI.Home.HomeSignal;
import GUI.Login.LoginController;
import GUI.Login.LoginSignal;
import GUI.Quiz.QuizController;
import GUI.Quiz.QuizSignal;
import Signal.Controller;
import Signal.Signal;
import GUI.Stat.StatController;
import Test.ExampleVector;
import GUI.Main.GlobalSignal;
import voca.app.Voca;
import voca.core.Word;

public class MainController implements Controller {
    private static final String CARD_LOGIN = "login";
    private static final String CARD_HOME = "home";
    private static final String CARD_QUIZ = "Quiz";

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
        this.quizController = new QuizController(exampleVector.voca,this);

        rootPanel.add(loginController.getView(), CARD_LOGIN);
        rootPanel.add(homeController.getView(), CARD_HOME);
        rootPanel.add(quizController.getView(),CARD_QUIZ);
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

    private void showQuiz(){
        cardLayout.show(rootPanel, CARD_QUIZ);
    }

    @Override
    public void send(Signal s, Object d) {
        if (s instanceof GlobalSignal globalSignal) {
            handleGlobal(globalSignal, d);
            return;
        }
        else if (s instanceof HomeSignal) {
            homeController.send(s, d);
            return;
        }
        else if (s instanceof LoginSignal) {
            loginController.send(s, d);
            return;
        }
        else if (s instanceof QuizSignal){
            quizController.send(s,d);
        }
    }

    private void handleGlobal(GlobalSignal signal, Object data) {
        switch (signal) {
            case TOGGLE_MENU:
                homeController.send(HomeSignal.TOGGLE_MENU, null);
                break;
            case HOME:
                if (data instanceof Voca voca) {
                    Vector<Word> userVocabulary = voca.getVoca();
                    homeController.updateVocabulary(userVocabulary);
                }
                showHome();
                break;
            case FILE:
            case STAT:
            case QUIZ:
                showQuiz();
                break;
            case LOGOUT:
                loginController.resetFields();
                showLogin();
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        new MainController().start();
    }
}

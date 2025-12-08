package GUI.Main;

import java.awt.CardLayout;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
import voca.app.Voca;
import voca.core.UserFileInfo;
import voca.core.Word;
import voca.management.FileManagement;

public class MainController implements Controller {
    private static final String CARD_LOGIN = "login";
    private static final String CARD_HOME = "home";
    private static final String CARD_QUIZ = "Quiz";
    private static final String CARD_STAT = "stat";

    private final HomeController homeController;
    private final LoginController loginController;
    private final StatController statController;
    private final QuizController quizController;

    private final JFrame frame;
    private final JPanel rootPanel;
    private final CardLayout cardLayout;
    private String currentCard = CARD_LOGIN;
    
    public MainController(){
        this.frame = new JFrame("VocaGUI");
        this.cardLayout = new CardLayout();
        this.rootPanel = new JPanel(cardLayout);

        ExampleVector exampleVector = new ExampleVector();
        this.homeController = new HomeController(exampleVector.voca, this);
        this.loginController = new LoginController(this);
        this.statController = new StatController(this);
        this.quizController = new QuizController(exampleVector.voca,this);

        rootPanel.add(loginController.getView(), CARD_LOGIN);
        rootPanel.add(homeController.getView(), CARD_HOME);
        rootPanel.add(quizController.getView(),CARD_QUIZ);
        rootPanel.add(statController.getView(), CARD_STAT);
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
        currentCard = CARD_LOGIN;
    }

    private void showHome() {
        cardLayout.show(rootPanel, CARD_HOME);
        currentCard = CARD_HOME;
    }

    private void showQuiz(){
        cardLayout.show(rootPanel, CARD_QUIZ);
        currentCard = CARD_QUIZ;
    }

    private void showStat(){
        cardLayout.show(rootPanel, CARD_STAT);
        currentCard = CARD_STAT;
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
                toggleCurrentMenu();
                break;
            case HOME:
                if (data instanceof UserFileInfo userFileInfo) {
                    Voca voca = new Voca(userFileInfo);
                    Vector<Word> userVocabulary = voca.getVoca();
                    homeController.updateUserFileInfo(userFileInfo);
                    homeController.updateVocabulary(userVocabulary);
                    statController.updateUserInfo(userFileInfo);
                    FileManagement.saveVoca(voca.getVoca(),userFileInfo.getUserDirectory());
                }
                showHome();
                break;
            case FILE:
                showHome();
                break;
            case STAT:
                showStat();
                break;
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

    private void toggleCurrentMenu() {
        switch (currentCard) {
            case CARD_HOME -> homeController.send(HomeSignal.TOGGLE_MENU, null);
            case CARD_QUIZ -> quizController.toggleMenu();
            case CARD_STAT -> statController.toggleMenu();
            default -> { }
        }
    }

    public static void main(String[] args) {
        new MainController().start();
    }
}

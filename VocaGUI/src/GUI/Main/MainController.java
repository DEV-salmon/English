package GUI.Main;

import java.awt.CardLayout;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.File.FileController;
import GUI.File.FileSignal;
import GUI.Home.HomeController;
import GUI.Home.HomeSignal;
import GUI.InCorrect.IncorrectController;
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
import voca.management.IncorrectManagement;

public class MainController implements Controller {
    private static final String CARD_LOGIN = "login";
    private static final String CARD_HOME = "home";
    private static final String CARD_QUIZ = "Quiz";
    private static final String CARD_STAT = "stat";
    private static final String CARD_FILE = "file";
    private static final String CARD_INCORRECT = "incorrect";

    private final HomeController homeController;
    private final LoginController loginController;
    private final StatController statController;
    private final QuizController quizController;
    private final FileController fileController;
    private final IncorrectController incorrectController;

    private final JFrame frame;
    private final JPanel rootPanel;
    private final CardLayout cardLayout;
    private String currentCard = CARD_LOGIN;
    private Voca currentVoca;
    private Vector<Word> currentVocaWords = new Vector<>();
    private UserFileInfo currentUser;
    private IncorrectManagement incorrectManagement;
    
    public MainController(){
        this.frame = new JFrame("VocaGUI");
        this.cardLayout = new CardLayout();
        this.rootPanel = new JPanel(cardLayout);

        ExampleVector exampleVector = new ExampleVector();
        this.currentVocaWords = new Vector<>(exampleVector.voca);
        this.homeController = new HomeController(currentVocaWords, this);
        this.loginController = new LoginController(this);
        this.statController = new StatController(this);
        this.quizController = new QuizController(currentVocaWords,this);
        this.fileController = new FileController(currentVocaWords,this);
        this.incorrectController = new IncorrectController(this);

        rootPanel.add(loginController.getView(), CARD_LOGIN);
        rootPanel.add(homeController.getView(), CARD_HOME);
        rootPanel.add(quizController.getView(),CARD_QUIZ);
        rootPanel.add(statController.getView(), CARD_STAT);
        rootPanel.add(fileController.getView(), CARD_FILE);
        rootPanel.add(incorrectController.getView(), CARD_INCORRECT);
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

    private void showIncorrect(){
        cardLayout.show(rootPanel, CARD_INCORRECT);
        currentCard = CARD_INCORRECT;
    }

    private void showFile(){
        cardLayout.show(rootPanel, CARD_FILE);
        currentCard = CARD_FILE;
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
        else if (s instanceof FileSignal) {
            fileController.send(s, d);
        }
    }

    private void handleGlobal(GlobalSignal signal, Object data) {
        switch (signal) {
            case TOGGLE_MENU:
                toggleCurrentMenu();
                break;
            case HOME:
                if (data instanceof UserFileInfo userFileInfo) {
                    loadUserContext(userFileInfo);
                }
                showHome();
                break;
            case UPDATE_VOCA: 
                if (data instanceof Vector<?> newVocabulary) {
                    Vector<Word> userVocabulary = (Vector<Word>) newVocabulary;

                   
                    homeController.updateVocabulary(userVocabulary);

                    if (fileController.getUserFileInfo() != null) {
                        String savePath = fileController.getUserFileInfo().getVocaFilePath();
                        FileManagement.saveVoca(userVocabulary, savePath);
                    } else {
                        System.err.println("경고: 저장 경로가 없어 병합된 단어장이 파일에 저장되지 않았습니다.");
                    }
                }
                break;
            case FILE:
                showFile();
                break;
            case STAT:
                statController.refreshView();
                showStat();
                break;
            case INCORRECT:
                incorrectController.refreshList();
                showIncorrect();
                break;
            case QUIZ:
                quizController.showSelect();
                showQuiz();
                break;
            case LOGOUT:
                loginController.resetFields();
                currentUser = null;
                currentVoca = null;
                currentVocaWords.clear();
                incorrectManagement = null;
                quizController.updateVocabulary(new Vector<>());
                quizController.setIncorrectManagement(null);
                quizController.setIncorrectUpdateListener(null);
                quizController.setStatManagement(null);
                incorrectController.setIncorrectManagement(null);
                showLogin();
                break;
            default:
                break;
        }
    }

    private void loadUserContext(UserFileInfo userFileInfo) {
        currentUser = userFileInfo;
        currentVoca = new Voca(userFileInfo);
        currentVocaWords = currentVoca.getVoca();
        homeController.updateUserFileInfo(userFileInfo);
        homeController.updateVocabulary(currentVocaWords);
        statController.updateUserInfo(userFileInfo);
        fileController.updateUserInfo(userFileInfo);
        quizController.updateVocabulary(currentVocaWords);
        incorrectManagement = new IncorrectManagement(userFileInfo);
        quizController.setIncorrectManagement(incorrectManagement);
        quizController.setIncorrectUpdateListener(incorrectController::refreshList);
        quizController.setStatManagement(statController.getStatManager());
        incorrectController.setIncorrectManagement(incorrectManagement);
        FileManagement.saveVoca(currentVocaWords,userFileInfo.getVocaFilePath());
    }

    private void toggleCurrentMenu() {
        switch (currentCard) {
            case CARD_HOME -> homeController.send(HomeSignal.TOGGLE_MENU, null);
            case CARD_QUIZ -> quizController.toggleMenu();
            case CARD_STAT -> statController.toggleMenu();
            case CARD_FILE -> fileController.toggleMenu();
            case CARD_INCORRECT -> incorrectController.toggleMenu();
            default -> { }
        }
    }

    public static void main(String[] args) {
        new MainController().start();
    }
}

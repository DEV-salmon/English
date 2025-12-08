package GUI.Quiz;

import java.util.Vector;

import GUI.Main.GlobalSignal;
import Signal.Controller;
import Signal.Signal;
import voca.core.Word;

public class QuizController implements Controller {
    private final QuizUI quizUI;
    private final Vector<Word> vacabulary;
    private boolean menuVisible;
    private final Controller globalHandler;

    public QuizController(Vector<Word> vocabulary, Controller controller){
        this.vacabulary =vocabulary;
        this.globalHandler = controller;
        this.quizUI = new QuizUI((this::send));
        this.quizUI.setSideMenuVisible(menuVisible);
    }

    public QuizUI getView(){
        return quizUI;
    }
    @Override
    public void send(Signal signal, Object data) {
        if (signal instanceof GlobalSignal globalSignal && globalHandler != null) {
            globalHandler.send(globalSignal, data);
            return;
        }
        if (!(signal instanceof QuizSignal quizSignal)) {
            return;
        }

        switch (quizSignal) {
            case SPELLINGQUIZ_BUTTON:
            case ENG_TO_KORQUIZ_BUTTON:
            case KOR_TO_ENGQUIZ_BUTTON:
            case EXQUIZ_BUTTON:
            case OBJECTQUIZ_BUTTON:
            case SUBJECTQUIZ_BUTTON:
            case COMBOBOX_SELECT:
            default:
                break;
        }
    }

    public void toggleMenu() {
        menuVisible = !menuVisible;
        quizUI.setSideMenuVisible(menuVisible);
    }
}

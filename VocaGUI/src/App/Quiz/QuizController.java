package Quiz;

import java.util.Vector;

import Signal.Controller;
import Signal.Signal;
import voca.core.Word;

public class QuizController implements Controller {
    private final QuizUI quizUI;
    private final Vector<Word> vacabulary;
    private boolean menuVisible;
    private final Controller globalHandler;

    public QuizController(Vector<Word> vocabulary, Controller globalHandler){
        this.vacabulary =vocabulary;
        this.globalHandler = globalHandler;
        this.quizUI = new QuizUI((this::send));
        this.quizUI.setSideMenuVisible(menuVisible);
    }

    public QuizUI getView(){
        return quizUI;
    }
    @Override
    public void send(Signal signal, Object data) {
        switch (signal) {
            case QuizSignal.SPELLINGQUIZ_BUTTON:
                
                break;
        
            default:
                break;
        }
    }
}

package GUI.Home;

import java.util.Locale;
import java.util.Vector;
import java.util.stream.Collectors;

import GUI.Main.GlobalSignal;
import Signal.Controller;
import Signal.Signal;
import voca.core.Word;

public class HomeController implements Controller {
    private final HomeUI homeUI;
    private Vector<Word> vocabulary;
    private boolean menuVisible;
    private final Controller globalHandler;

    public HomeController() {
        this(new Vector<>(), null);
    }

    public HomeController(Vector<Word> vocabulary) {
        this(vocabulary, null);
    }

    public HomeController(Vector<Word> vocabulary, Controller globalHandler) {
        this.vocabulary = vocabulary == null ? new Vector<>() : new Vector<>(vocabulary);
        this.globalHandler = globalHandler;
        this.homeUI = new HomeUI(new Vector<>(this.vocabulary), this::send);
        this.homeUI.setSideMenuVisible(menuVisible);
    }

    public HomeUI getView() {
        return homeUI;
    }

    @Override
    public void send(Signal signal, Object payload) {
        if (!(signal instanceof HomeSignal homeSignal)) {
            if (globalHandler != null && signal instanceof GlobalSignal) {
                globalHandler.send(signal, payload);
            }
            return;
        }

        switch (homeSignal) {
            case TOGGLE_MENU:
                toggleMenu();
                break;
            case CHANGE_TEXT_FIELD:
                handleSearch(payload);
                break;
            case ADD_WORD:

            default:
                break;
        }
    }

    private void toggleMenu() {
        menuVisible = !menuVisible;
        homeUI.setSideMenuVisible(menuVisible);
    }

    private void addWord(String str){
        

    }

    private void handleSearch(Object payload) {
        String query = payload == null ? "" : payload.toString();
        String normalized = query.trim().toLowerCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            homeUI.updateWords(new Vector<>(vocabulary));
            return;
        }

        Vector<Word> filtered = vocabulary.stream()
                .filter(word -> matches(word, normalized))
                .collect(Collectors.toCollection(Vector::new));
        homeUI.updateWords(filtered);
    }

    private boolean matches(Word word, String query) {
        if (word == null) {
            return false;
        }

        if (word.getEng() != null && word.getEng().toLowerCase(Locale.ROOT).contains(query)) {
            return true;
        }

        for (String kor : word.getKor()) {
            if (kor.toLowerCase(Locale.ROOT).contains(query)) {
                return true;
            }
        }
        return false;
    }


    public void updateVocabulary(Vector<Word> newVocabulary) {
        this.vocabulary = new Vector<>(newVocabulary);
        homeUI.updateWords(newVocabulary);
    }
}

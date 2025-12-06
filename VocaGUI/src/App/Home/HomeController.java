package Home;

import java.util.Locale;
import java.util.Vector;
import java.util.stream.Collectors;

import Signal.Signal;
import voca.core.Word;

public class HomeController {
    private final HomeUI homeUI;
    private final Vector<Word> vocabulary;
    private boolean menuVisible;

    public HomeController() {
        this(new Vector<>());
    }

    public HomeController(Vector<Word> vocabulary) {
        this.vocabulary = vocabulary == null ? new Vector<>() : new Vector<>(vocabulary);
        this.homeUI = new HomeUI(new Vector<>(this.vocabulary), this::handleSignal);
        this.homeUI.setSideMenuVisible(menuVisible);
    }

    public HomeUI getView() {
        return homeUI;
    }

    public void send(Signal signal, Object payload) {
        handleSignal(signal, payload);
    }

    private void handleSignal(Signal signal, Object payload) {
        if (!(signal instanceof HomeSignal homeSignal)) {
            return;
        }

        switch (homeSignal) {
            case TOGGLE_MENU:
                toggleMenu();
                break;
            case SEARCH:
                handleSearch(payload);
                break;
            case ADD:
                break;
            case VIEW_MORE_INFO:
                break;
            case DELETE:
                break;
            default:
                break;
        }
    }

    private void toggleMenu() {
        menuVisible = !menuVisible;
        homeUI.setSideMenuVisible(menuVisible);
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
}

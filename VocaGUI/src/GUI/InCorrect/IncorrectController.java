package GUI.InCorrect;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import GUI.Main.GlobalSignal;
import Signal.Controller;
import Signal.Signal;
import voca.core.IncorrectWord;
import voca.management.IncorrectManagement;

public class IncorrectController implements Controller {

    private final IncorrectUI incorrectUI;
    private final Controller globalHandler;
    private IncorrectManagement incorrectManagement;
    private boolean menuVisible;

    public IncorrectController(Controller globalHandler) {
        this.globalHandler = globalHandler;
        this.incorrectUI = new IncorrectUI(this::send);
        this.incorrectUI.setSideMenuVisible(menuVisible);
        this.incorrectUI.setControlsEnabled(false);
        this.incorrectUI.setStatus("로그인 후 오답 기능을 사용할 수 있습니다.");
    }

    public JPanel getView() {
        return incorrectUI;
    }

    @Override
    public void send(Signal signal, Object data) {
        if (signal instanceof GlobalSignal globalSignal) {
            if (globalHandler != null) {
                globalHandler.send(globalSignal, data);
            }
            return;
        }

        if (!(signal instanceof IncorrectSignal incorrectSignal)) {
            return;
        }

        if (incorrectManagement == null) {
            incorrectUI.showMessage("로그인 후 오답 기능을 사용할 수 있습니다.");
            incorrectUI.setControlsEnabled(false);
            return;
        }

        switch (incorrectSignal) {
            case REFRESH:
                refreshList();
                break;
            case CLEAR_ALL:
                clearAll();
                break;
            case REMOVE_SELECTED:
                removeSelected(data);
                break;
            default:
                break;
        }
    }

    public void setIncorrectManagement(IncorrectManagement incorrectManagement) {
        this.incorrectManagement = incorrectManagement;
        if (incorrectManagement == null) {
            incorrectUI.updateIncorrectWords(new ArrayList<>());
            incorrectUI.setStatus("로그인 후 오답 기능을 사용할 수 있습니다.");
            incorrectUI.setFilePath("-");
            incorrectUI.setControlsEnabled(false);
            return;
        }
        incorrectUI.setControlsEnabled(true);
        incorrectUI.setFilePath(incorrectManagement.getIncorrectFilePath());
        refreshList();
    }

    public void refreshList() {
        if (incorrectManagement == null) {
            incorrectUI.updateIncorrectWords(new ArrayList<>());
            incorrectUI.setStatus("로그인 후 오답 기능을 사용할 수 있습니다.");
            incorrectUI.setControlsEnabled(false);
            return;
        }
        Vector<IncorrectWord> words = incorrectManagement.getIncorrectWordsSnapshot();
        incorrectUI.updateIncorrectWords(words);
    }

    public void toggleMenu() {
        menuVisible = !menuVisible;
        incorrectUI.setSideMenuVisible(menuVisible);
    }

    private void clearAll() {
        int confirm = JOptionPane.showConfirmDialog(
                incorrectUI,
                "모든 오답 노트를 삭제하시겠습니까?",
                "확인",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        incorrectManagement.clearAll();
        refreshList();
        incorrectUI.showMessage("오답 노트를 비웠습니다.");
    }

    private void removeSelected(Object payload) {
        List<IncorrectWord> targets = extractWords(payload);
        if (targets.isEmpty()) {
            incorrectUI.showMessage("삭제할 오답을 선택하세요.");
            return;
        }
        for (IncorrectWord word : targets) {
            incorrectManagement.markCorrect(word);
        }
        refreshList();
    }

    private List<IncorrectWord> extractWords(Object payload) {
        List<IncorrectWord> list = new ArrayList<>();
        if (payload instanceof List<?> rawList) {
            for (Object obj : rawList) {
                if (obj instanceof IncorrectWord word) {
                    list.add(word);
                }
            }
        }
        return list;
    }
}

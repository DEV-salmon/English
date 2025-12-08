package GUI.Stat;

import javax.swing.JOptionPane;

import GUI.Main.GlobalSignal;
import Signal.Controller;
import Signal.Signal;
import voca.core.UserFileInfo;
import voca.management.StatManagement;

public class StatController implements Controller {

    private final StatUI statUI;
    private final Controller globalHandler;
    private StatManagement statManager;
    private boolean menuVisible;
    private String selectedQuizType = "";

    public StatController(Controller globalHandler) {
        this(null, globalHandler);
    }

    public StatController(StatManagement statManager, Controller globalHandler) {
        this.statManager = statManager;
        this.globalHandler = globalHandler;
        this.statUI = new StatUI(this::send);
        this.statUI.setSideMenuVisible(menuVisible);
    }

    public StatUI getView() {
        return statUI;
    }

    @Override
    public void send(Signal signal, Object option) {
        if (signal instanceof GlobalSignal globalSignal) {
            if (globalHandler != null) {
                globalHandler.send(globalSignal, option);
            }
            return;
        }

        if (!(signal instanceof StatSignal statSignal)) {
            return;
        }

        if (statManager == null) {
            statUI.showMessage("로그인 후 통계 기능을 사용할 수 있습니다.");
            return;
        }

        switch (statSignal) {
            case STAT_SHOW_BUTTON:
                handleSaveAction("통계 파일과 그래프를 갱신했습니다.");
                break;
            case STAT_SAVE_BUTTON:
                handleSaveAction("통계를 저장했습니다.");
                break;
            case STAT_QUIZTYPE_SELECT:
                selectedQuizType = option == null ? "" : option.toString();
                statUI.updateSummary(buildSummaryText("선택된 퀴즈 유형을 업데이트했습니다."));
                break;
            default:
                break;
        }
    }

    public void updateUserInfo(UserFileInfo userInfo) {
        if (userInfo == null) {
            statManager = null;
            statUI.showMessage("유효하지 않은 사용자 정보입니다.");
            return;
        }
        this.statManager = new StatManagement(userInfo);
    }

    public StatManagement getStatManager() {
        return statManager;
    }

    public void toggleMenu() {
        menuVisible = !menuVisible;
        statUI.setSideMenuVisible(menuVisible);
    }

    private void handleSaveAction(String message) {
        statManager.saveStatToFile();
        statUI.updateSummary(buildSummaryText(message));
        JOptionPane.showMessageDialog(statUI, message);
    }

    private String buildSummaryText(String action) {
        StringBuilder builder = new StringBuilder();
        builder.append("최근 작업: ").append(action);
        if (selectedQuizType != null && !selectedQuizType.isEmpty()) {
            builder.append("\n선택된 퀴즈 유형: ").append(selectedQuizType);
        }
        builder.append("\n저장 위치: ").append(statManager == null ? "알 수 없음" : statManager.getStatFilePath());
        return builder.toString();
    }
}

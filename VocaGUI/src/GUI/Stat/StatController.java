package GUI.Stat;

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

        refreshView();
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

    /**
     * Stat 화면이 열릴 때 자동으로 통계와 그래프를 갱신합니다.
     */
    public void refreshView() {
        if (statManager == null) {
            statUI.updateSummary("로그인 후 통계를 볼 수 있습니다.");
            statUI.updateChart(null);
            return;
        }
        statManager.saveStatToFile();
        statUI.updateSummary(buildSummaryText());
        statUI.updateChart(statManager.getChartFilePath());
        statUI.showSummaryCard();
    }

    private String buildSummaryText() {
        if (statManager == null) {
            return "로그인 후 통계를 볼 수 있습니다.";
        }
        return statManager.buildSummaryForUi();
    }
}

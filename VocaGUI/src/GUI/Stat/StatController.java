package GUI.Stat;

import Signal.Signal;
import Signal.SignalListener;
import voca.management.StatManagement;

import java.util.Scanner;

public class StatController implements SignalListener {

    private StatUI ui;
    private StatManagement statManager;
    private Scanner scanner;

    public StatController(StatManagement statManager) {
        this.statManager = statManager;
        this.scanner = new Scanner(System.in);
        ui = new StatUI("통계 관리", this);
    }

    @Override
    public void onSignal(Signal signal, Object option) {
        switch ((StatSignal) signal) {

            case STAT_SHOW_BUTTON:
                statManager.createDistributionGraph();
                System.out.println("정규분포 그래프 생성 완료");
                break;

            case STAT_SAVE_BUTTON:
                statManager.saveStatToFileWithWait(scanner);
                System.out.println("통계 저장 완료");
                break;

            case STAT_QUIZTYPE_SELECT:
                System.out.println("선택된 퀴즈 유형: " + option);
                statManager.setSelectedType(option.toString()); 
                break;

            default:
                break;
        }
    }
}

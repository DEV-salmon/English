package voca.management;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import voca.core.UserFileInfo;

public class StatManagement extends BaseMenu {

    private final String statFilePath;

    private int korEng_correct;
    private int korEng_wrong;

    private int engKor_correct;
    private int engKor_wrong;

    private int example_correct;
    private int example_wrong;

    private int spelling_correct;
    private int spelling_wrong;

    private boolean autoSave = true;

    public StatManagement(UserFileInfo userInfo) {
        this.statFilePath = userInfo.getStatFilePath();
    }

    public String getStatFilePath() {
        return statFilePath;
    }

    /**
     * 뜻->영어 정답을 1 증가시키고 자동 저장합니다.
     */
    public void addKorEngCorrect() { korEng_correct++; autoSaveIfNeeded(); }
    /**
     * 뜻->영어 오답을 1 증가시키고 자동 저장합니다.
     */
    public void addKorEngWrong() { korEng_wrong++; autoSaveIfNeeded(); }

    /**
     * 영어->뜻 정답을 1 증가시키고 자동 저장합니다.
     */
    public void addEngKorCorrect() { engKor_correct++; autoSaveIfNeeded(); }
    /**
     * 영어->뜻 오답을 1 증가시키고 자동 저장합니다.
     */
    public void addEngKorWrong() { engKor_wrong++; autoSaveIfNeeded(); }

    /**
     * 예문 정답을 1 증가시키고 자동 저장합니다.
     */
    public void addExampleCorrect() { example_correct++; autoSaveIfNeeded(); }
    /**
     * 예문 오답을 1 증가시키고 자동 저장합니다.
     */
    public void addExampleWrong() { example_wrong++; autoSaveIfNeeded(); }

    /**
     * 스펠링 정답을 1 증가시키고 자동 저장합니다.
     */
    public void addSpellingCorrect() { spelling_correct++; autoSaveIfNeeded(); }
    /**
     * 스펠링 오답을 1 증가시키고 자동 저장합니다.
     */
    public void addSpellingWrong() { spelling_wrong++; autoSaveIfNeeded(); }

    public void menu(Scanner scanner){
        int choice = 0;
        while(choice !=4){
            cleanConsole();
            System.out.println("1) 현재 통계 보기 2) 통계 파일 저장 3) 통계 초기화 4) 종료");
            choice = readInt(scanner, "메뉴를 선택하세요 : ");
            System.out.println();
            switch (choice){
                case 1 -> {
                    printCurrentStats();
                    waitConsole(scanner);
                }
                case 2 -> saveStatToFileWithWait(scanner);
                case 3 -> {
                    resetStats();
                    saveStatToFile();
                    System.out.println("통계를 초기화했습니다.");
                    waitConsole(scanner);
                }
                case 4 -> {
                    System.out.println("통계 메뉴를 종료합니다.");
                    waitConsole(scanner);
                }
                default -> System.out.println("메뉴에 존재하지 않습니다. 다시 입력해주세요.");
            }
        }
    }

    /**
     * 자동 저장을 수행합니다.
     */
    private void autoSaveIfNeeded(){
        if(autoSave){
            saveStatToFile();
        }
    }

    /**
     * 평균(정답률)을 계산합니다.
     */
    private double mean(int c, int w) {
        if (c + w == 0) return 0.0;
        return (double) c / (c + w);
    }

    /**
     * 이항 분포 분산을 계산합니다.
     */
    private double variance(int c, int w) {
        double p = mean(c, w);
        return p * (1 - p);
    }

    /**
     * 통계를 파일과 그래프로 저장합니다.
     */
    public void saveStatToFile() {

        File statFile = new File(statFilePath);
        File parent = statFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (FileWriter fw = new FileWriter(statFile)) {

            fw.write("===== 통계 =====\n\n");

            fw.write("[뜻 → 영어]\n");
            writeBlock(fw, korEng_correct, korEng_wrong);

            fw.write("\n[영어 → 뜻]\n");
            writeBlock(fw, engKor_correct, engKor_wrong);

            fw.write("\n[예문 빈칸]\n");
            writeBlock(fw, example_correct, example_wrong);

            fw.write("\n[스펠링]\n");
            writeBlock(fw, spelling_correct, spelling_wrong);

            fw.write("===========================\n");

        } catch (IOException e) {
            System.out.println("저장 오류: " + e.getMessage());
        }

        saveBarChart();
    }

    /**
     * 통계 값을 모두 0으로 초기화합니다.
     */
    private void resetStats(){
        korEng_correct = 0;
        korEng_wrong = 0;
        engKor_correct = 0;
        engKor_wrong = 0;
        example_correct = 0;
        example_wrong = 0;
        spelling_correct = 0;
        spelling_wrong = 0;
    }

    /**
     * 현재 통계를 콘솔에 출력합니다.
     */
    private void printCurrentStats(){
        System.out.println("===== 현재 통계 =====");
        printBlock("[뜻 → 영어]", korEng_correct, korEng_wrong);
        printBlock("[영어 → 뜻]", engKor_correct, engKor_wrong);
        printBlock("[예문 빈칸]", example_correct, example_wrong);
        printBlock("[스펠링]", spelling_correct, spelling_wrong);
    }

    /**
     * 통계 블록을 출력합니다.
     */
    private void printBlock(String title, int c, int w){
        System.out.println(title);
        int total = c + w;
        System.out.println("총 문제 수    : " + total);
        System.out.println("맞은 문제 수  : " + c);
        System.out.println("틀린 문제 수  : " + w);
        System.out.println("평균(정답률)  : " + String.format("%.4f", mean(c, w)));
        System.out.println("분산          : " + String.format("%.4f", variance(c, w)));
        System.out.println("표준편차      : " + String.format("%.4f", Math.sqrt(variance(c, w))));
        System.out.println("------------------");
    }

    /**
     * 파일에 통계 블록을 기록합니다.
     */
    private void writeBlock(FileWriter fw, int c, int w) throws IOException {
        int total = c + w;
        fw.write("총 문제 수    : " + total + "\n");
        fw.write("맞은 문제 수  : " + c + "\n");
        fw.write("틀린 문제 수  : " + w + "\n");
        fw.write("평균(정답률)  : " + String.format("%.4f", mean(c, w)) + "\n");
        fw.write("분산          : " + String.format("%.4f", variance(c, w)) + "\n");
        fw.write("표준편차      : " + String.format("%.4f", Math.sqrt(variance(c, w))) + "\n");
    }

    /**
     * 막대그래프 형식으로 통계를 저장합니다.
     */
    private void saveBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(korEng_correct, "맞음", "뜻→영어");
        dataset.addValue(korEng_wrong, "틀림", "뜻→영어");

        dataset.addValue(engKor_correct, "맞음", "영어→뜻");
        dataset.addValue(engKor_wrong, "틀림", "영어→뜻");

        dataset.addValue(example_correct, "맞음", "예문");
        dataset.addValue(example_wrong, "틀림", "예문");

        dataset.addValue(spelling_correct, "맞음", "스펠링");
        dataset.addValue(spelling_wrong, "틀림", "스펠링");

        JFreeChart chart = ChartFactory.createBarChart(
                "퀴즈 정오답 막대그래프",
                "퀴즈 유형",
                "개수",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        try {
            File txtFile = new File(statFilePath);
            File dir = txtFile.getParentFile();
            File chartFile = new File(dir, "stat.png");

            BufferedImage image = chart.createBufferedImage(800, 600);
            ImageIO.write(image, "png", chartFile);

        } catch (IOException e) {
            System.out.println("그래프 저장 오류: " + e.getMessage());
        }
    }

    public void saveStatToFileWithWait(Scanner scanner, String waitMessage) {
        waitConsole(scanner, waitMessage);
        saveStatToFile();
    }

    public void saveStatToFileWithWait(Scanner scanner) {
        saveStatToFileWithWait(scanner, "엔터를 누르면 통계를 저장합니다...");
    }

    /**
     * UI에서 바로 보여줄 수 있는 간단한 통계 요약을 만듭니다.
     */
    public String buildSummaryForUi() {
        StringBuilder builder = new StringBuilder();
        builder.append("[뜻 → 영어] 맞음 ").append(korEng_correct)
                .append(" / 틀림 ").append(korEng_wrong)
                .append(" (정답률 ").append(formatRate(korEng_correct, korEng_wrong)).append(")\n");
        builder.append("[영어 → 뜻] 맞음 ").append(engKor_correct)
                .append(" / 틀림 ").append(engKor_wrong)
                .append(" (정답률 ").append(formatRate(engKor_correct, engKor_wrong)).append(")\n");
        builder.append("[예문 빈칸] 맞음 ").append(example_correct)
                .append(" / 틀림 ").append(example_wrong)
                .append(" (정답률 ").append(formatRate(example_correct, example_wrong)).append(")\n");
        builder.append("[스펠링] 맞음 ").append(spelling_correct)
                .append(" / 틀림 ").append(spelling_wrong)
                .append(" (정답률 ").append(formatRate(spelling_correct, spelling_wrong)).append(")\n");
        builder.append("\n통계 파일: ").append(statFilePath);
        builder.append("\n그래프 파일: ").append(getChartFilePath());
        return builder.toString();
    }

    /**
     * 그래프가 저장되는 PNG 파일 경로를 반환합니다.
     */
    public String getChartFilePath() {
        File txtFile = new File(statFilePath);
        File dir = txtFile.getParentFile();
        File chartFile = dir == null ? new File("stat.png") : new File(dir, "stat.png");
        return chartFile.getAbsolutePath();
    }

    private String formatRate(int c, int w) {
        double p = mean(c, w) * 100.0;
        return String.format("%.1f%%", p);
    }
}

package voca.management;

import java.io.FileWriter;
import java.io.IOException;

public class StatManagement {

    private int korEng_correct;
    private int korEng_wrong;

    private int engKor_correct;
    private int engKor_wrong;

    private int example_correct;
    private int example_wrong;

    private int spelling_correct;
    private int spelling_wrong;

    public void addKorEngCorrect() { korEng_correct++; }
    public void addKorEngWrong() { korEng_wrong++; }

    public void addEngKorCorrect() { engKor_correct++; }
    public void addEngKorWrong() { engKor_wrong++; }

    public void addExampleCorrect() { example_correct++; }
    public void addExampleWrong() { example_wrong++; }

    public void addSpellingCorrect() { spelling_correct++; }
    public void addSpellingWrong() { spelling_wrong++; }

    public int getKorEngTotal() { return korEng_correct + korEng_wrong; }
    public int getEngKorTotal() { return engKor_correct + engKor_wrong; }
    public int getExampleTotal() { return example_correct + example_wrong; }
    public int getSpellingTotal() { return spelling_correct + spelling_wrong; }

    private double calcMean(int c, int w) {
        if (c + w == 0) return 0.0;
        return (double) c / (c + w);
    }

    private double calcVar(int c, int w) {
        double p = calcMean(c, w);
        return p * (1 - p);
    }

    private double calcStd(int c, int w) {
        return Math.sqrt(calcVar(c, w));
    }

    public void saveStatToFile() {
        String filename = "Voca/src/res/stat.txt";

        try (FileWriter fw = new FileWriter(filename)) {

            fw.write("===== 통계 =====\n\n");

            fw.write("[뜻 → 영어]\n");
            writeTypeStats(fw, korEng_correct, korEng_wrong);

            fw.write("\n[영어 → 뜻]\n");
            writeTypeStats(fw, engKor_correct, engKor_wrong);

            fw.write("\n[예문 빈칸]\n");
            writeTypeStats(fw, example_correct, example_wrong);

            fw.write("\n[스펠링]\n");
            writeTypeStats(fw, spelling_correct, spelling_wrong);

            fw.write("===========================\n");

        } catch (IOException e) {
            System.out.println("저장 오류: " + e.getMessage());
        }
    }

    private void writeTypeStats(FileWriter fw, int correct, int wrong) throws IOException {
        int total = correct + wrong;
        fw.write("총 문제 수    : " + total + "\n");
        fw.write("맞은 문제 수  : " + correct + "\n");
        fw.write("틀린 문제 수  : " + wrong + "\n");
        fw.write("평균(정답률)  : " + String.format("%.4f", calcMean(correct, wrong)) + "\n");
        fw.write("분산          : " + String.format("%.6f", calcVar(correct, wrong)) + "\n");
        fw.write("표준편차      : " + String.format("%.6f", calcStd(correct, wrong)) + "\n");
    }

    public static void saveStat(StatManagement stat) {
        stat.saveStatToFile();
    }
}

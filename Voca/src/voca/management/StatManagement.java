package voca.management;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import voca.core.UserSession;

public class StatManagement {

    private int correct_obj;
    private int wrong_obj;
    private int correct_sub;
    private int wrong_sub;

    private final UserSession session;
    private final String vocaFileName;

    public StatManagement(UserSession session, String vocaFilePath) {
        this.session = session;
        this.vocaFileName = new File(vocaFilePath).getName();  
    }

    public int getCorrect_obj() { return correct_obj; }
    public void setCorrect_obj(int correct_obj) { this.correct_obj = correct_obj; }

    public int getWrong_obj() { return wrong_obj; }
    public void setWrong_obj(int wrong_obj) { this.wrong_obj = wrong_obj; }

    public int getCorrect_sub() { return correct_sub; }
    public void setCorrect_sub(int correct_sub) { this.correct_sub = correct_sub; }

    public int getWrong_sub() { return wrong_sub; }
    public void setWrong_sub(int wrong_sub) { this.wrong_sub = wrong_sub; }

    public int getTotal_obj() { return correct_obj + wrong_obj; }
    public int getTotal_sub() { return correct_sub + wrong_sub; }

    public double getMean_obj() {
        if (getTotal_obj() == 0) return 0.0;
        return (double) correct_obj / getTotal_obj();
    }

    public double getMean_sub() {
        if (getTotal_sub() == 0) return 0.0;
        return (double) correct_sub / getTotal_sub();
    }

    public double getVar_obj() {
        double p = getMean_obj();
        return p * (1 - p);
    }

    public double getVar_sub() {
        double p = getMean_sub();
        return p * (1 - p);
    }

    public double getStd_obj() { return Math.sqrt(getVar_obj()); }
    public double getStd_sub() { return Math.sqrt(getVar_sub()); }

    public void saveStatToFile() {

        String statDirPath = session.getUserDirectory() + "/stat/";
        File statDir = new File(statDirPath);
        if (!statDir.exists()) statDir.mkdirs();

        String savePath = statDirPath + vocaFileName + ".stat";

        try (FileWriter fw = new FileWriter(savePath)) {

            fw.write("===== 통계 =====\n");

            fw.write("=== 객관식 ===\n");
            fw.write("총 문제 수    : " + getTotal_obj() + "\n");
            fw.write("맞은 문제 수  : " + getCorrect_obj() + "\n");
            fw.write("틀린 문제 수  : " + getWrong_obj() + "\n");
            fw.write("평균(정답률)  : " + String.format(\"%.4f\", getMean_obj()) + "\n");
            fw.write("분산          : " + String.format(\"%.6f\", getVar_obj()) + "\n");
            fw.write("표준편차      : " + String.format(\"%.6f\", getStd_obj()) + "\n\n");

            fw.write("=== 주관식 ===\n");
            fw.write("총 문제 수    : " + getTotal_sub() + "\n");
            fw.write("맞은 문제 수  : " + getCorrect_sub() + "\n");
            fw.write("틀린 문제 수  : " + getWrong_sub() + "\n");
            fw.write("평균(정답률)  : " + String.format(\"%.4f\", getMean_sub()) + "\n");
            fw.write("분산          : " + String.format(\"%.6f\", getVar_sub()) + "\n");
            fw.write("표준편차      : " + String.format(\"%.6f\", getStd_sub()) + "\n");

            fw.write("===========================\n");

        } catch (IOException e) {
            System.out.println("저장 오류: " + e.getMessage());
        }
    }
}

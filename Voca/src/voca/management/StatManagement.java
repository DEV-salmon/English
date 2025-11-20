package voca.management;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import voca.core.UserSession;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class StatManagement {

    private final String statFilePath;

    private int korEng_correct;
    private int korEng_wrong;

    private int engKor_correct;
    private int engKor_wrong;

    private int example_correct;
    private int example_wrong;

    private int spelling_correct;
    private int spelling_wrong;

    public StatManagement(UserSession session) {
        this.statFilePath = session.getStatFilePath();
    }

    public void addKorEngCorrect() { korEng_correct++; }
    public void addKorEngWrong() { korEng_wrong++; }

    public void addEngKorCorrect() { engKor_correct++; }
    public void addEngKorWrong() { engKor_wrong++; }

    public void addExampleCorrect() { example_correct++; }
    public void addExampleWrong() { example_wrong++; }

    public void addSpellingCorrect() { spelling_correct++; }
    public void addSpellingWrong() { spelling_wrong++; }

    private double mean(int c, int w) {
        if (c + w == 0) return 0.0;
        return (double) c / (c + w);
    }

    private double variance(int c, int w) {
        double p = mean(c, w);
        return p * (1 - p);
    }

    private XYSeries createNormalSeries(String title, double mu, double var) {
        XYSeries series = new XYSeries(title);
        if (var == 0) return series;

        double std = Math.sqrt(var);
        double start = Math.max(0.0, mu - 3 * std);
        double end = Math.min(1.0, mu + 3 * std);

        for (double x = start; x <= end; x += 0.001) {
            double y = (1 / (std * Math.sqrt(2 * Math.PI)))
                    * Math.exp(-Math.pow(x - mu, 2) / (2 * var));
            series.add(x, y);
        }
        return series;
    }

    public void saveStatToFile() {

        try (FileWriter fw = new FileWriter(statFilePath)) {

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

        saveNormalDistributionChart();
    }

    private void writeBlock(FileWriter fw, int c, int w) throws IOException {
        int total = c + w;
        fw.write("총 문제 수    : " + total + "\n");
        fw.write("맞은 문제 수  : " + c + "\n");
        fw.write("틀린 문제 수  : " + w + "\n");
        fw.write("평균(정답률)  : " + String.format("%.4f", mean(c, w)) + "\n");
        fw.write("분산          : " + String.format("%.4f", variance(c, w)) + "\n");
        fw.write("표준편차      : " + String.format("%.4f", Math.sqrt(variance(c, w))) + "\n");
    }

    private void saveNormalDistributionChart() {

        double m1 = mean(korEng_correct, korEng_wrong);
        double v1 = variance(korEng_correct, korEng_wrong);

        double m2 = mean(engKor_correct, engKor_wrong);
        double v2 = variance(engKor_correct, engKor_wrong);

        double m3 = mean(example_correct, example_wrong);
        double v3 = variance(example_correct, example_wrong);

        double m4 = mean(spelling_correct, spelling_wrong);
        double v4 = variance(spelling_correct, spelling_wrong);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(createNormalSeries("뜻→영어", m1, v1));
        dataset.addSeries(createNormalSeries("영어→뜻", m2, v2));
        dataset.addSeries(createNormalSeries("예문", m3, v3));
        dataset.addSeries(createNormalSeries("스펠링", m4, v4));

        JFreeChart chart = ChartFactory.createXYLineChart(
                "퀴즈별 정답률 정규분포",
                "정답률",
                "확률밀도",
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

            ChartUtils.saveChartAsPNG(chartFile, chart, 800, 600);

        } catch (IOException e) {
            System.out.println("그래프 저장 오류: " + e.getMessage());
        }
    }
}

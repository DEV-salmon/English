package Test;


import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test {
    // 전달된 패널을 표시할 테스트 프레임을 초기화
    public Test(JPanel jPanel, JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);
        frame.setLocationRelativeTo(null);
        frame.add(jPanel);
        frame.setVisible(true);
    }
}

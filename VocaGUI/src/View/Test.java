package View;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test {
        public Test(JPanel jPanel,JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700); // 창 크기도 좀 넉넉하게
        frame.setLocationRelativeTo(null);
        frame.add(jPanel);
        frame.setVisible(true);
    }
}

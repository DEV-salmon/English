package View;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;

public class MakePrettyInterface {
        // 크기 고정
    public static void setFixedSize(JComponent component, int width, int height) {
        Dimension size = new Dimension(width, height);
        component.setPreferredSize(size);  // 희망 크기
        component.setMaximumSize(size);    // 최대 크기
        component.setMinimumSize(size);    // 최소 크기
        component.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬
    }

}

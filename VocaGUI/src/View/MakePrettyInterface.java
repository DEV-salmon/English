package View;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;

public class MakePrettyInterface {
    // 컴포넌트의 크기를 고정 설정
    public static void setFixedSize(JComponent component, int width, int height) {
        Dimension size = new Dimension(width, height);
        component.setPreferredSize(size);
        component.setMaximumSize(size);
        component.setMinimumSize(size);
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

}

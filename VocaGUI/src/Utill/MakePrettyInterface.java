package Utill;


import javax.swing.*;
import java.awt.*;

public class MakePrettyInterface {
    // 컴포넌트의 크기를 고정 설정
    public static void setFixedSize(JComponent component, int width, int height) {
        Dimension size = new Dimension(width, height);
        component.setPreferredSize(size);
        component.setMaximumSize(size);
        component.setMinimumSize(size);
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public static void updateScreen(JPanel p){
        p.revalidate();
        p.repaint();   
    }
    public static void updateScreen(JFrame p){
        p.revalidate();
        p.repaint();   
    }

    public static void makeShadow(JPanel jPanel){
        jPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.GRAY));
    }
    public static void makeShadow(JButton jButton){
        jButton.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.GRAY));
    }
    public static void makeWhite(JPanel jPanel){
        jPanel.setBackground(Color.WHITE);
        jPanel.setOpaque(true);
    }
    public static void makeWhite(JButton jButton){
        jButton.setBackground(Color.WHITE);
        jButton.setOpaque(true);
    }
    

}

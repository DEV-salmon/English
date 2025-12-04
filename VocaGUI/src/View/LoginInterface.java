package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.*;

public class LoginInterface extends JPanel {
    
    public JTextField userNameField;
    public JPasswordField passWordField;
    public JButton loginButton;
    public JButton registerButton;

    public LoginInterface() {
        // [1] 바깥 패널 (this): 화면 정중앙 배치용
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        // [2] 안쪽 패널 (panel): 세로 쌓기용
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // --- 여백 ---
        panel.add(Box.createVerticalStrut(50));

        // 1. Welcome
        JLabel welcome = new JLabel("Welcome");
        welcome.setFont(new Font("Arial", Font.BOLD, 32)); // 폰트 대폭 키움
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT); 
        panel.add(welcome);
        
        panel.add(Box.createVerticalStrut(50));

        // 2. UserName Label
        JLabel userNameLabel = new JLabel("UserName");
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // 라벨도 키움
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        panel.add(userNameLabel);
        
        panel.add(Box.createVerticalStrut(10));

        // 3. UserName Field (입력창)
        userNameField = new JTextField();
        userNameField.setHorizontalAlignment(JTextField.CENTER);
        userNameField.setFont(new Font("Arial", Font.PLAIN, 18)); // 입력 글씨 키움
        // ★ 핵심: 아래에서 만든 함수로 크기 강제 지정 (가로 350, 세로 50)
        MakePrettyInterface.setFixedSize(userNameField, 350, 50); 
        panel.add(userNameField);

        panel.add(Box.createVerticalStrut(20));

        // 4. Password Label
        JLabel passWordLabel = new JLabel("Password");
        passWordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passWordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(passWordLabel);
        
        panel.add(Box.createVerticalStrut(10));

        // 5. Password Field (비번창)
        passWordField = new JPasswordField();
        passWordField.setHorizontalAlignment(JPasswordField.CENTER);
        passWordField.setFont(new Font("Arial", Font.PLAIN, 18)); // 입력 글씨 키움
        MakePrettyInterface.setFixedSize(passWordField, 350, 50); // ★ 크기 강제 지정
        panel.add(passWordField);

        panel.add(Box.createVerticalStrut(50));

        // 6. Login Button
        loginButton = new JButton("LOGIN");
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        loginButton.setOpaque(true); // Mac에서 배경색 먹이려면 필수인 경우가 있음
        loginButton.setBorderPainted(false); // 버튼 테두리 없애서 깔끔하게 (선택)
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        
        MakePrettyInterface.setFixedSize(loginButton, 350, 60); // ★ 버튼은 좀 더 크게 (세로 60)
        panel.add(loginButton);
        
        panel.add(Box.createVerticalStrut(15)); 
        
        // 7. Register Button
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 18));
        MakePrettyInterface.setFixedSize(registerButton, 350, 60); // 크기픽스
        panel.add(registerButton);

        // 8. Team Label
        JLabel teamLabel = new JLabel("made by team2");
        teamLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        teamLabel.setForeground(Color.GRAY);
        teamLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        panel.add(teamLabel);
        
        panel.add(Box.createVerticalStrut(20));
        
        // 안쪽 패널을 바깥(this)에 부착
        add(panel);
    }
    


    // 테스트용 메인
}
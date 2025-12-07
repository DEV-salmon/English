package GUI.Login;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.*;

import Signal.Controller;
import Utill.MakePrettyInterface;

// 로그인 UI
public class LoginUI extends JPanel {

    // UI 부품 정의
    public JTextField userNameField;
    public JPasswordField passWordField;
    public JButton loginButton;
    public JButton registerButton;
    public JOptionPane optionPane;

    private final Controller signalHandler;

    public LoginUI() {
        this(null);
    }

    // 로그인 화면 UI를 구성하는 생성자
    public LoginUI(Controller signalHandler) {
        this.signalHandler = signalHandler;

        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        panel.add(Box.createVerticalStrut(50));

        JLabel welcome = new JLabel("Welcome");
        welcome.setFont(new Font("Arial", Font.BOLD, 32));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(welcome);

        panel.add(Box.createVerticalStrut(50));

        JLabel userNameLabel = new JLabel("UserName");
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(userNameLabel);

        panel.add(Box.createVerticalStrut(10));

        userNameField = new JTextField();
        userNameField.setHorizontalAlignment(JTextField.CENTER);
        userNameField.setFont(new Font("Arial", Font.PLAIN, 18));
        MakePrettyInterface.setFixedSize(userNameField, 350, 50);
        panel.add(userNameField);

        panel.add(Box.createVerticalStrut(20));

        JLabel passWordLabel = new JLabel("Password");
        passWordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passWordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(passWordLabel);

        panel.add(Box.createVerticalStrut(10));

        passWordField = new JPasswordField();
        passWordField.setHorizontalAlignment(JPasswordField.CENTER);
        passWordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passWordField.setText("");
        MakePrettyInterface.setFixedSize(passWordField, 350, 50);
        panel.add(passWordField);

        panel.add(Box.createVerticalStrut(50));

        loginButton = new JButton("LOGIN");
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        MakePrettyInterface.setFixedSize(loginButton, 350, 60);
        panel.add(loginButton);

        panel.add(Box.createVerticalStrut(15));

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 18));
        MakePrettyInterface.setFixedSize(registerButton, 350, 60);
        panel.add(registerButton);

        JLabel teamLabel = new JLabel("made by team2");
        teamLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        teamLabel.setForeground(Color.GRAY);
        teamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(teamLabel);

        panel.add(Box.createVerticalStrut(20));

        add(panel);

        wireSignals();
    }

    private void wireSignals() {
        loginButton.addActionListener(e -> {
            send(LoginSignal.LOGIN, collectCredentials());
        });
        registerButton.addActionListener(e -> {
            send(LoginSignal.REGISTER, collectCredentials());
        });
    }

    private LoginCredentials collectCredentials() {
        String username = userNameField.getText();
        char[] password = passWordField.getPassword();
        return new LoginCredentials(username, password);
    }

    private void send(LoginSignal signal, Object payload) {
        signalHandler.send(signal, payload);
    }
}

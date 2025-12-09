package GUI.Login;

import javax.swing.*;

import GUI.Main.GlobalSignal;
import Signal.Controller;
import Signal.Signal;
import Utill.MakePrettyInterface;
import voca.app.Voca;
import voca.auth.LogInManagement;
import voca.auth.Login;
import voca.core.UserFileInfo;
import voca.management.FileManagement;
import java.awt.Window;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Vector;

public class LoginController implements Controller {
    private final LoginUI loginUI;
    private Controller signalHandler;
    private static final String loginFilePath = "VocaGUI/res/Loginlist";
    private final LogInManagement logInManagement = new LogInManagement(loginFilePath);
    private static final String USER_VOCA_DIR = "VocaGUI/res/Vocas";

    public LoginController() {
        this(null);
    }

    public LoginController(Controller signalHandler) {
        this.signalHandler = signalHandler;
        this.loginUI = new LoginUI(this);
    }

    public LoginUI getView() {
        return loginUI;
    }

    public void setSignalHandler(Controller signalHandler) {
        this.signalHandler = signalHandler;
    }

    @Override
    public void send(Signal signal, Object data) {
        if (!(signal instanceof LoginSignal loginSignal)) {
            return;
        }
        switch (loginSignal) {
            case LOGIN:
                handleLogin(data);
                break;
            case REGISTER:
                handleRegister(data);
                break;
            case LOGIN_SUCCESS:
                handleLoginSuccess(data);
                break;
            case LOGIN_FAIL:
                handleLoginFail(data);
                break;
            default:
                break;
        }
    }

    private void handleLogin(Object payload) {
        LoginCredentials credentials = payload instanceof LoginCredentials ? (LoginCredentials) payload : null;
        String username = credentials != null ? credentials.getUsername() : null;
        char[] password = credentials != null ? credentials.getPassword() : new char[0];

        handleLoginJudgment(username, password);
    }

    private void handleLoginJudgment(String username, char[] password) {
        Vector<Login> loginList = logInManagement.getLoginList();

        if (isBlank(username) || password.length == 0) {
            handleLoginFail(username);
            return;
        }

        if (loginList.isEmpty()) {
            handleLoginFail(username);
            return;
        }
        try {
            String PasswordString = new String(password);

            for (Login login : loginList) {
                if (login.getUserid().equals(username)) {
                    String hashedInput = LogInManagement.PasswordUtil.hashPassword(PasswordString, login.getSalt());
                    if (login.getHashedpassword().equals(hashedInput)) {
                        UserFileInfo userInfo = new UserFileInfo(login.getUserid(), USER_VOCA_DIR);
                        handleLoginSuccess(userInfo);
                        return;
                    } else {
                        handleLoginFail(username);
                        return;
                    }
                }
            }
            handleLoginFail(username);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("인증 알고리즘 오류 발생: " + e.getMessage());
            handleLoginFail(username);

            throw new RuntimeException("인증 시스템 초기화 실패", e);
        }
    }

    private void handleLoginSuccess(Object data) {
        sendToHandler(GlobalSignal.HOME, data);
    }

    private void handleLoginFail(Object data) {
        JOptionPane.showMessageDialog(loginUI, "로그인 실패: 아이디와 비밀번호를 확인하세요.");
    }

    private void handleRegister(Object data) {
        System.out.println("DEBUG: handleRegister 메서드 호출됨");

        Object[] inputData = showRegisterDialogue();

        handleRegisterJudgement(inputData);
    }

    private void handleRegisterJudgement(Object[] data){
        JTextField usernameField = (JTextField) data[0];
        JPasswordField passwordField = (JPasswordField) data[1];
        int result = (int) data[2];

        String username = usernameField.getText();
        char[] password = passwordField.getPassword();

        LoginCredentials credentials = new LoginCredentials(username, password);

        Vector<Login> loginList = logInManagement.getLoginList();

        if(result == JOptionPane.OK_OPTION){
            if (isBlank(credentials.getUsername()) || password.length == 0) {
                JOptionPane.showMessageDialog(loginUI, "아이디와 비밀번호를 모두 입력해야 합니다.");
                Arrays.fill(password, '0');
                return;
            }

            try {
                String PasswordString = new String(password);
                for(Login login : loginList) {
                    if (login.getUserid().equals(username)){
                        JOptionPane.showMessageDialog(loginUI, "회원가입 실패: 아이디가 중복입니다.");
                        return;
                    }
                }
                String salt = LogInManagement.PasswordUtil.generateSalt();
                String hashedPassword = LogInManagement.PasswordUtil.hashPassword(PasswordString, salt);

                loginList.add(new Login(username, salt, hashedPassword));
                FileManagement.saveLogin(loginList,loginFilePath);
                JOptionPane.showMessageDialog(loginUI, "회원가입 성공");
                logInManagement.initializeUserVocaFile(username);
            }catch (NoSuchAlgorithmException e) {
                System.out.println("알고리즘을 찾을 수 없습니다.\n");
                JOptionPane.showMessageDialog(loginUI, "회원가입 실패: 아이디가 중복입니다.");
            }

        }
    }

    //한시간째 매서드 하나 만드는 중 셤공부 언제하지
    //
    private Object[] showRegisterDialogue() {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Label.background", Color.WHITE);

        JTextField newUsernameField = new JTextField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        char defaultEchoChar = newPasswordField.getEchoChar();

        JLabel RegisterText = new JLabel("Register");
        RegisterText.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        JPanel rootRegisterText = new JPanel(new GridBagLayout());
        rootRegisterText.add(RegisterText);
        MakePrettyInterface.makeWhite(rootRegisterText);

        JPanel basePanel = new JPanel(new GridBagLayout());
        MakePrettyInterface.makeWhite(basePanel);
        MakePrettyInterface.setFixedSize(basePanel, 450, 300);

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        MakePrettyInterface.makeWhite(registerPanel);
        basePanel.add(registerPanel);

        JLabel IDText = new JLabel("New ID ");
        IDText.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JPanel IdFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        MakePrettyInterface.makeWhite(IdFlow);
        MakePrettyInterface.makeShadow(newUsernameField, false);
        IdFlow.add(IDText);
        IdFlow.add(newUsernameField);

        JLabel PasswordText = new JLabel("New PW ");
        PasswordText.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JPanel PwFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        MakePrettyInterface.makeWhite(PwFlow);
        MakePrettyInterface.makeShadow(newPasswordField, false);
        PwFlow.add(PasswordText);
        PwFlow.add(newPasswordField);

        JPanel PwcheckPanel = new JPanel(new BorderLayout());
        JCheckBox showPwCheck = new JCheckBox("비밀번호 보기");
        PwcheckPanel.add(showPwCheck,BorderLayout.EAST);
        showPwCheck.setBackground(Color.WHITE);
        showPwCheck.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        showPwCheck.setFocusPainted(false);
        
        showPwCheck.addItemListener(e -> {
            if (showPwCheck.isSelected()) {
                newPasswordField.setEchoChar((char) 0);
            } else {
                newPasswordField.setEchoChar(defaultEchoChar);
            }
        });
        

        JButton btnJoin = new JButton("가입");
        MakePrettyInterface.setFixedSize(btnJoin, 50, 20);
        btnJoin.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        JButton btnCancel = new JButton("취소");
        MakePrettyInterface.setFixedSize(btnCancel, 50, 20);
        btnCancel.setFont(new Font("맑은 고딕", Font.BOLD, 12));

        btnJoin.setBackground(Color.WHITE);
        btnCancel.setBackground(Color.WHITE);
        MakePrettyInterface.makeShadow(btnJoin, false);
        MakePrettyInterface.makeShadow(btnCancel, false);
        btnJoin.setFocusPainted(false);
        btnCancel.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        MakePrettyInterface.makeWhite(buttonPanel);
        buttonPanel.add(btnJoin);
        buttonPanel.add(btnCancel);

        registerPanel.add(rootRegisterText);
        registerPanel.add(Box.createVerticalStrut(30));
        registerPanel.add(IdFlow);
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(PwFlow);
        registerPanel.add(Box.createVerticalStrut(5));
        registerPanel.add(PwcheckPanel);
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(buttonPanel);


        final int[] resultState = {JOptionPane.CANCEL_OPTION};

        btnJoin.addActionListener(e -> {
            resultState[0] = JOptionPane.OK_OPTION;
            Window w = SwingUtilities.getWindowAncestor(btnJoin);
            if (w != null) w.dispose();
        });

        btnCancel.addActionListener(e -> {
            resultState[0] = JOptionPane.CANCEL_OPTION;
            Window w = SwingUtilities.getWindowAncestor(btnCancel);
            if (w != null) w.dispose();
        });

        JOptionPane.showOptionDialog(
                loginUI,
                basePanel,
                "Register",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{},
                null
        );

        return new Object[]{newUsernameField, newPasswordField, resultState[0]};
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    private void sendToHandler(Signal signal, Object payload) {
        if (signalHandler != null) {
            signalHandler.send(signal, payload);
        }
    }

    public void resetFields() {
        loginUI.userNameField.setText("");
        loginUI.passWordField.setText("");
    }
}

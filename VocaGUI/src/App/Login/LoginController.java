package App.Login;

import javax.swing.*;

import App.Home.HomeUI;
import App.Main.GlobalSignal;
import Signal.Controller;
import Signal.Signal;
import voca.app.Voca;
import voca.auth.LogInManagement;
import voca.auth.Login;
import voca.core.UserFileInfo;
import voca.management.FileManagement;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Vector;

public class LoginController implements Controller {
    private final LoginUI loginUI;
    private Controller signalHandler;
    private static final String loginFilePath = "Voca/src/res/LoginList";
    private final LogInManagement logInManagement = new LogInManagement(loginFilePath);
    private static final String USER_VOCA_DIR = "Voca/src/res/Vocas";



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
            //System.out.println("회원가입이 된 아이디가 없습니다.\n");
            handleLoginFail(username);
            return;
        }
        try {
            String PasswordString = new String(password);

            for (Login login : loginList) {
                if (login.getUserid().equals(username)) {
                    String hashedInput = LogInManagement.PasswordUtil.hashPassword(PasswordString, login.getSalt());
                    if (login.getHashedpassword().equals(hashedInput)) {
                        //System.out.println("로그인이 완료되었습니다.\n");
                        UserFileInfo userInfo = new UserFileInfo(login.getUserid(), USER_VOCA_DIR);
                        Voca voca = new Voca(userInfo);
                        handleLoginSuccess(voca);
                        return;
                    } else {
                        //System.out.println("비밀번호가 일치 하지 않습니다.\n");
                        handleLoginFail(username);
                        return;
                    }
                }
            }
            //System.out.println("존재하지 않는 아이디 입니다..\n");
            handleLoginFail(username);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("인증 알고리즘 오류 발생: " + e.getMessage());
            handleLoginFail(username);

            throw new RuntimeException("인증 시스템 초기화 실패", e);
        }
    }

    private void handleLoginSuccess(Object data) {
        System.out.println("DEBUG: handleLoginSuccess 메서드 호출됨");
        sendToHandler(GlobalSignal.HOME, data);
    }

    private void handleLoginFail(Object data) {
        System.out.println("DEBUG: handleLoginFail 메서드 호출됨");
        JOptionPane.showMessageDialog(loginUI, "로그인 실패: 아이디와 비밀번호를 확인하세요.");

    }

    private void handleRegister(Object data) {
        System.out.println("DEBUG: handleRegister 메서드 호출됨");

        Object[] inputData = showRegisterDialogue();

        handleRegisterJudgment(inputData);
    }

    private void handleRegisterJudgment(Object[] data){
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

    private Object[] showRegisterDialogue(){
        JTextField newUsernameField = new JTextField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        newPasswordField.setEchoChar((char) 0);

        // 2. 필드를 담을 패널 생성 및 배치 (BoxLayout 사용)
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));

        registerPanel.add(new JLabel("사용할 아이디를 입력하세요:"));
        registerPanel.add(newUsernameField);
        registerPanel.add(Box.createVerticalStrut(10)); // 수직 간격 추가
        registerPanel.add(new JLabel("사용할 비밀번호를 입력하세요:"));
        registerPanel.add(newPasswordField);

        // 3. JOptionPane을 사용하여 팝업 표시
        int result = JOptionPane.showConfirmDialog(
                loginUI,
                registerPanel,
                "Register",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        return new Object[]{newUsernameField,newPasswordField,result};
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

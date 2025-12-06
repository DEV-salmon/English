package Login;

import javax.swing.JOptionPane;

import Main.GlobalSignal;
import Signal.Controller;
import Signal.Signal;

public class LoginController implements Controller {
    private final LoginUI loginUI;
    private Controller signalHandler;

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

        if (isBlank(username) || password.length == 0) {
            handleLoginFail(username);
            return;
        }

        handleLoginSuccess(username);
    }

    private void handleLoginSuccess(Object data) {
        sendToHandler(GlobalSignal.HOME, data);
        sendToHandler(LoginSignal.LOGIN_SUCCESS, data);
    }

    private void handleLoginFail(Object data) {
        JOptionPane.showMessageDialog(loginUI, "로그인 실패: 아이디와 비밀번호를 확인하세요.");
        sendToHandler(LoginSignal.LOGIN_FAIL, data);
    }

    private void handleRegister(Object data) {
        JOptionPane.showMessageDialog(loginUI, "회원가입 기능은 준비 중입니다.");
        sendToHandler(LoginSignal.REGISTER, data);
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    private void sendToHandler(Signal signal, Object payload) {
        if (signalHandler != null) {
            signalHandler.send(signal, payload);
        }
    }
}

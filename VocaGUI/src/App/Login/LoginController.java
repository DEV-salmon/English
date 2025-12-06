package Login;

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
                notifySignal(LoginSignal.REGISTER, data);
                break;
            case LOGIN_SUCCESS:
            case LOGIN_FAIL:
                notifySignal(loginSignal, data);
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
            notifySignal(LoginSignal.LOGIN_FAIL, username);
            return;
        }

        notifySignal(LoginSignal.LOGIN_SUCCESS, username);
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    private void notifySignal(LoginSignal signal, Object payload) {
        if (signalHandler != null) {
            signalHandler.send(signal, payload);
        }
    }
}

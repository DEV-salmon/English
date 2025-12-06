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
        this.loginUI = new LoginUI();
        wireActions();
    }

    public LoginUI getView() {
        return loginUI;
    }

    public void setSignalHandler(Controller signalHandler) {
        this.signalHandler = signalHandler;
    }

    @Override
    public void send(Signal signal, Object data) {
        // currently no inbound signals to handle
    }

    private void wireActions() {
        loginUI.loginButton.addActionListener(e -> handleLogin());
        loginUI.registerButton.addActionListener(e -> notifySignal(LoginSignal.REGISTER, loginUI.userNameField.getText()));
    }

    private void handleLogin() {
        String username = loginUI.userNameField.getText();
        char[] password = loginUI.passWordField.getPassword();
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

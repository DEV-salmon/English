import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.function.Consumer;

public class LoginPanel extends JPanel {
    private final LoginManager loginManager;
    private final Consumer<UserFileInfo> onLoginSuccess;
    private final JTextField userIdField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JLabel statusLabel = new JLabel("아이디와 비밀번호를 입력해주세요.");

    public LoginPanel(LoginManager loginManager, Consumer<UserFileInfo> onLoginSuccess){
        super(new BorderLayout(16, 16));
        this.loginManager = loginManager;
        this.onLoginSuccess = onLoginSuccess;
        add(buildForm(), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
    }

    private JPanel buildForm(){
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0;
        gc.insets.set(4, 4, 4, 4);

        form.add(new JLabel("아이디"), gc);
        gc.gridx = 1;
        gc.weightx = 1;
        userIdField.setPreferredSize(new Dimension(200, 30));
        form.add(userIdField, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        form.add(new JLabel("비밀번호"), gc);
        gc.gridx = 1;
        gc.weightx = 1;
        passwordField.setPreferredSize(new Dimension(200, 30));
        form.add(passwordField, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 2;
        gc.weightx = 1;
        JPanel buttonRow = new JPanel();
        JButton loginButton = new JButton("로그인");
        JButton registerButton = new JButton("회원가입");
        buttonRow.add(loginButton);
        buttonRow.add(registerButton);
        form.add(buttonRow, gc);

        gc.gridy = 3;
        statusLabel.setForeground(new Color(0x444444));
        form.add(statusLabel, gc);

        loginButton.addActionListener(e -> doLogin(false));
        registerButton.addActionListener(e -> doLogin(true));
        return form;
    }

    private void doLogin(boolean isRegister){
        String userId = userIdField.getText();
        char[] password = passwordField.getPassword();
        try{
            LoginResult result = isRegister
                ? loginManager.register(userId, password)
                : loginManager.login(userId, password);
            setStatus(result.getMessage(), result.isSuccess());
            if(result.isSuccess()){
                onLoginSuccess.accept(result.getSession());
            }
        }finally {
            Arrays.fill(password, '\0');
        }
    }

    private void setStatus(String message, boolean success){
        statusLabel.setForeground(success ? new Color(0x1D7A1D) : new Color(0xB22222));
        statusLabel.setText(message);
    }
}

package App.Login;

public class LoginCredentials {
    private final String username;
    private final char[] password;

    public LoginCredentials(String username, char[] password) {
        this.username = username;
        this.password = password == null ? new char[0] : password.clone();
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password.clone();
    }
}

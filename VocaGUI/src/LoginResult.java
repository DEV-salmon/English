public class LoginResult {
    private final boolean success;
    private final String message;
    private final UserFileInfo session;

    private LoginResult(boolean success, String message, UserFileInfo session) {
        this.success = success;
        this.message = message;
        this.session = session;
    }

    public static LoginResult success(UserFileInfo session, String message) {
        return new LoginResult(true, message, session);
    }

    public static LoginResult failure(String message) {
        return new LoginResult(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UserFileInfo getSession() {
        return session;
    }
}

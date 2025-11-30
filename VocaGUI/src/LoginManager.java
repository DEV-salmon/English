public class LoginManager {
    private final FileManager fileManager;

    public LoginManager(FileManager fileManager){
        this.fileManager = fileManager;
    }

    public LoginResult login(String userId, char[] password){
        if(userId == null || userId.isBlank()){
            return LoginResult.failure("아이디를 입력해주세요.");
        }
        UserFileInfo session = fileManager.prepareUserSession(userId);
        return LoginResult.success(session, "로그인했습니다. 단어장을 준비했어요.");
    }

    public LoginResult register(String userId, char[] password){
        if(userId == null || userId.isBlank()){
            return LoginResult.failure("아이디를 입력해주세요.");
        }
        UserFileInfo session = fileManager.prepareUserSession(userId);
        return LoginResult.success(session, "회원가입을 완료했습니다. 단어장을 준비했어요.");
    }
}

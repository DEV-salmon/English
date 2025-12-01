package vocagui.login;

import voca.app.Voca;
import voca.auth.Login;
import vocagui.core.UserFileInfo;
import voca.core.Word;
import voca.management.FileManagement;
import vocagui.FileManager;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Vector;

public class LoginManager {
    private final String loginFilePath;
    private final Vector<Login> loginList;
    private final FileManager fileManager;
    /**
     * 패스워드를 암호화하는 클래스 입니다.
     */
    public static class PasswordUtil {
        /**
         * 비밀번호를 해쉬화 하는 메소드입니다.
         * @param password 사용자가 입력한 비밀번호입니다.
         * @param salt  해쉬화에 이용될 salt변수입니다.
         * @return  해쉬화된 비밀번호가 반환됩니다.
         */
        public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes()); // 솔트 추가
            byte[] hashedPassword = md.digest(password.getBytes());
            return bytesToHex(hashedPassword);
        }

        /**
         * 무작위의 salt를 생성하는 메소드입니다..
         * @return  무작위 salt를 반환합니다.
         */
        public static String generateSalt() {
            SecureRandom sr = new SecureRandom();
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return bytesToHex(salt);
        }

        /**
         * 입력된 byte들을 16진수 문자열로 바꾸어주는 메소드입니다.
         * @param bytes 입력될 bytes변수입니다.(salt, hashedpassword)
         * @return 16진수화된 문자열을 반환합니다.
         */
        private static String bytesToHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
    }

    public LoginManager(String fileName, FileManager fileManager) {
        this.loginFilePath = fileName;
        this.loginList = FileManager.makeLogin(fileName);
        this.fileManager = fileManager;
    }

    /**
     * 회원가입 메소드입니다.
     * @param userId GUI에서 텍스트 필드를 통해 아이디를 가져옴
     * @param userPw GUI에서 텍스트 필드를 통해 비밀번호를 가져옴
     * @return 회원가입시의 결과를 반환(success or failure)
     */
    public LoginResult register(String userId, String userPw){ // boolean 대신 LoginResult 반환
        try {
            // ... (아이디 중복 확인 로직) ...
            for(Login login : loginList) {
                if (login.getUserid().equals(userId)){
                    return LoginResult.failure("중복되는 아이디입니다."); // 중복 메시지 반환
                }
            }

            String salt = PasswordUtil.generateSalt(); // (PasswordUtil 사용)
            String hashedPassword = PasswordUtil.hashPassword(userPw, salt);

            loginList.add(new Login(userId, salt, hashedPassword));
            FileManager.saveLogin(loginList, this.loginFilePath); // 저장

            initializeUserVocaFile(userId);

            UserFileInfo userInfo = this.fileManager.prepareUserSession(userId);
            return LoginResult.success(userInfo, "회원가입이 완료되었습니다.");

        }catch (NoSuchAlgorithmException e) {
            return LoginResult.failure("암호화 알고리즘 오류.");
        }
    }

    /**
     * 로그인 메소드입니다.
     * @param userId GUI에서 텍스트 필드를 통해 아이디를 가져옴
     * @param userPw GUI에서 텍스트 필드를 통해 비밀번호를 가져옴
     * @return 회원가입시의 결과를 반환(success or failure)
     */
    public LoginResult login(String userId, String userPw) { // boolean 대신 LoginResult 반환
        if(loginList.isEmpty()){
            return LoginResult.failure("회원가입된 아이디가 없습니다.");
        }
        try{
            for(Login login : loginList){
                if (login.getUserid().equals(userId)) {
                    String hashedInput = PasswordUtil.hashPassword(userPw, login.getSalt()); // (PasswordUtil 사용)
                    if(login.getHashedpassword().equals(hashedInput)){
                        UserFileInfo userInfo = this.fileManager.prepareUserSession(userId);
                        return LoginResult.success(userInfo, "로그인이 완료되었습니다.");
                    }else {
                        return LoginResult.failure("비밀번호가 일치하지 않습니다.");
                    }
                }
            }
            return LoginResult.failure("아이디가 존재하지 않습니다.");
        } catch (NoSuchAlgorithmException e) {
            return LoginResult.failure("암호화 알고리즘 오류.");
        }
    }

    private void initializeUserVocaFile(String userId){
        UserFileInfo userInfo = this.fileManager.prepareUserSession(userId);;
        String directoryPath = userInfo.getUserDirectory();
        File directory = new File(directoryPath);
        if(!directory.exists()){
            directory.mkdirs();
        }
        String filePath = userInfo.getVocaFile();
        File file = new File(filePath);
        File parent = file.getParentFile();
        if(parent != null && !parent.exists()){
            parent.mkdirs();
        }
        if(!file.exists()){
            Vector<Word> vocaBase = FileManagement.makeVoca("Voca/src/res/voca");
            FileManagement.saveVoca(new Vector<>(vocaBase), filePath);
        }
    }
}

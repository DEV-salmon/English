package voca.auth;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.Vector;

import voca.app.Voca;
import voca.core.UserFileInfo;
import voca.core.Word;
import voca.management.BaseMenu;
import voca.management.FileManagement;

/**
 * 전반적인 로그인기능을 관리하는 클래스입니다
 * 암호에 무작위 salt를 덧붙여 해시화하여 보안을 강화합니다.
 */
public class LogInManagement extends BaseMenu {
    private final Vector<Login> loginList;
    private static final Scanner scanner = new Scanner(System.in);
    private final String loginFilePath;
    private static final String USER_VOCA_DIR = "Voca/src/res/Vocas";

    public LogInManagement(String fileName) {
        this.loginFilePath = fileName;
        this.loginList = FileManagement.makeLogin(fileName);
    }

    public void run(){
        login_menu();
    }

    /**
     * 로그인 메뉴입니다.
     * 로그인 후 아이디를 통해 단어장 파일이 만들어집니다.
     * 이후 메뉴로 넘어갑니다.
     */
    void login_menu(){
        int choice = 0;

        while(choice !=3) {
            cleanConsole();
            System.out.println("1) 회원가입 2) 로그인 3) 종료");
            choice = readInt(scanner, "메뉴를 선택하세요 : ");
            System.out.println();
            switch (choice) {
                case 1-> registerUser();
                case 2-> loginUser();
                case 3 -> System.out.println("단어장 프로그램을 종료합니다.\n");
                default -> System.out.println("메뉴에 존재하지 않습니다. 다시 입력해주세요.");
            }
        }
        FileManagement.saveLogin(loginList,loginFilePath);
    }

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

    /**
     * 회원가입을 하는 메소드입니다.
     */
    private void registerUser(){
        try {
            System.out.println("사용할 닉네임를 입력해주세요");
            System.out.print("아이디 : ");
            String suserId = scanner.next();
            scanner.nextLine();

            for(Login login : loginList) {
                if (login.getUserid().equals(suserId)){
                    System.out.println("중복되는 아이디입니다.\n");
                    waitConsole(scanner);
                    return;
                }
            }

            System.out.println("사용할 비밀번호를 입력해주세요 : ");
            System.out.print("비밀번호 : ");
            String suserPw = scanner.next();
            scanner.nextLine();

            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(suserPw, salt);

            loginList.add(new Login(suserId, salt, hashedPassword));

            FileManagement.saveLogin(loginList,loginFilePath);


            System.out.println("회원가입이 완료 완료되었습니다.\n");

            initializeUserVocaFile(suserId);
            waitConsole(scanner);

        }catch (NoSuchAlgorithmException e) {
            System.out.println("알고리즘을 찾을 수 없습니다.\n");
            waitConsole(scanner);
        }

    }

    /**
     * 로그인을 하는 메소드입니다.
     */
    private void loginUser() {
        if(loginList.isEmpty()){
            System.out.println("회원가입이 된 아이디가 없습니다.\n");
            waitConsole(scanner);
            return;
        }
        try{
            System.out.println("아이디를 입력하십시오");
            System.out.print("아이디 : ");
            String pid = scanner.next();
            scanner.nextLine();
            for(Login login : loginList){
                if (login.getUserid().equals(pid)) {
                    System.out.println("비밀번호를 입력하십시오.");
                    System.out.print("비밀번호 : ");
                    String ppw = scanner.next();
                    scanner.nextLine();
                    String hashedInput = PasswordUtil.hashPassword(ppw, login.getSalt());
                    if(login.getHashedpassword().equals(hashedInput)){
                        System.out.println("로그인이 완료되었습니다.\n");
                        UserFileInfo userInfo = new UserFileInfo(login.getUserid(), USER_VOCA_DIR);
                        Voca voca = new Voca(userInfo);
                        voca.menu(login.getUserid());
                        return;
                    }else {
                        System.out.println("비밀번호가 일치 하지 않습니다.\n");
                        waitConsole(scanner);
                        return;
                    }
                }
            }
            System.out.println("아이디가 존재하지 않습니다.\n");
            waitConsole(scanner);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("알고리즘을 찾을 수 없습니다.\n");
            waitConsole(scanner);
        }
    }
    private void initializeUserVocaFile(String userId){
        UserFileInfo userInfo = new UserFileInfo(userId, USER_VOCA_DIR);
        String directoryPath = userInfo.getUserDirectory();
        File directory = new File(directoryPath);
        if(!directory.exists()){
            directory.mkdirs();
        }
        String filePath = userInfo.getVocaFilePath();
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

    public Vector<Login> getLoginList() {
        return loginList;
    }
}

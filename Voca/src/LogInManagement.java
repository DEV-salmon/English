import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.Vector;

public class LogInManagement {
    private final Vector<Login> LoginList;
    private static final Scanner scanner = new Scanner(System.in);

    public LogInManagement(String fileName) {
        this.LoginList = FileManagement.makeLogin(fileName);
    }

    public static void run(String fileName){
        LogInManagement logInManagement = new LogInManagement(fileName);
        logInManagement.login_menu();
    }

    /**
     * 로그인 메뉴입니다.
     * 로그인 후 아이디를 통해 단어장 파일이 만들어집니다.
     * 이후 메뉴로 넘어갑니다.
     */
    void login_menu(){
        int choice = 0;

        while(choice !=3) {
            System.out.println("1) 회원가입 2) 로그인 3) 종료");
            System.out.print("메뉴를 선택하세요 : ");
            choice = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            switch (choice) {
                case 1-> registerUser(scanner, LoginList);
                case 2-> loginUser(scanner, LoginList);
                case 3 ->System.out.println("단어장 프로그램을 종료합니다.\n");
            }
        }
        FileManagement.saveLogin(LoginList,"Voca/src/res/LoginList");
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
     * @param scanner   입력을 위한 scanner객체입니다.
     * @param loginList 로그인 정보를 저장할 벡터 객체입니다.
     */
    public static void registerUser(Scanner scanner,Vector<Login> loginList){
        try {
            System.out.println("사용할 닉네임를 입력해주세요");
            System.out.print("아이디 : ");
            String suserId = scanner.next();
            scanner.nextLine();

            for(Login login : loginList) {
                if (login.getUserid().equals(suserId)){
                    System.out.println("중복되는 아이디입니다.\n");
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

            FileManagement.saveLogin(loginList,"Voca/src/res/LoginList");


            System.out.println("회원가입이 완료 완료되었습니다.\n");

            Vector<Word> vocaBase = FileManagement.makeVoca("Voca/src/res/voca");
            String vocafile = "Voca/src/res/Vocas/"+ suserId +"_voca";
            FileManagement.saveVoca(vocaBase,vocafile);

        }catch (NoSuchAlgorithmException e) {
            System.out.println("알고리즘을 찾을 수 없습니다.\n");
        }

    }

    /**
     * 로그인을 하는 메소드입니다.
     * @param scanner   입력을 위한 scanner객체입니다.
     * @param loginList 로그인 정보 비교를 위한 벡터 객체입니다.
     */
    public static void loginUser(Scanner scanner, Vector<Login> loginList) {
        if(loginList.isEmpty()){
            System.out.println("회원가입이 된 아이디가 없습니다.");
            return;
        }
        try{
            System.out.println("아이디를 입력하십시오 : ");
            System.out.print("아이디 : ");
            String pid = scanner.next();
            scanner.nextLine();
            boolean isfound = false;

            for(Login login : loginList){
                if (login.getUserid().equals(pid)) {
                    System.out.println("비밀번호를 입력하십시오.");
                    System.out.print("비밀번호 : ");
                    String ppw = scanner.next();
                    scanner.nextLine();
                    String hashedInput = PasswordUtil.hashPassword(ppw, login.getSalt());
                    isfound = true;
                    if(login.getHashedpassword().equals(hashedInput)){
                        System.out.println("로그인이 완료되었습니다.\n");
                        String vocafile = "Voca/src/res/Vocas/"+login.getUserid()+"_voca";
                        Voca voca = new Voca(vocafile);
                        Voca.menu(vocafile,login.getUserid());
                        return;
                    }else {
                        System.out.println("비밀번호가 일치 하지 않습니다.\n");
                        return;
                    }
                }
            }
            if(!isfound){
                System.out.println("아이디가 존재하지 않습니다.\n");
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("알고리즘을 찾을 수 없습니다.\n");
        }
    }
}

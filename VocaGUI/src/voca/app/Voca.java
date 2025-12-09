package voca.app;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

import voca.core.UserFileInfo;
import voca.core.Word;
import voca.management.BaseMenu;
import voca.management.ExampleManagement;
import voca.management.FileManagement;
import voca.management.FileMenu;
import voca.management.QuizManagement;
import voca.management.StatManagement;
import voca.management.WordManagement;
/**
 * 영어단어장 프로그램의 핵심 클래스입니다
 * LoginManagement를 통해 UserFileInfo를 받아와 유저를 폴더별로 관리합니다
 */
public class Voca extends BaseMenu {
    private static final String DEFAULT_VOCA_PATH = "VocaGUI/res/voca";
    private final Vector<Word> voca;
    private final Scanner scanner = new Scanner(System.in);
    private final String filePath;
    private final QuizManagement quizManagement;
    private final WordManagement wordManagement;
    private final FileMenu fileMenu;
    private final StatManagement statManagement;
    private final UserFileInfo userInfo;

    /**
     * 생성자입니다
     * 보카 파일이 비어있다면, 기본 보카를 호출합니다
     * 보카 파일을 파일 경로를 받아 파싱합니다
     * Quiz, 단어, 파일, 통계관련 클래스의 객체를 생성합니다
     * @param userInfo : 사용자별 보카 관리를 위해 필요한 파라미터입니다
     */
    public Voca(UserFileInfo userInfo){
        this.userInfo = userInfo;
        this.filePath = userInfo.getVocaFilePath();
        ensureUserVocaExists();
        this.voca = FileManagement.makeVoca(filePath);
        if(this.voca.isEmpty()){
            loadDefaultIntoUserFile();
            this.voca.addAll(FileManagement.makeVoca(filePath));
        }
        this.quizManagement = new QuizManagement(this.voca, userInfo);
        this.wordManagement = new WordManagement(scanner, this.voca);
        this.fileMenu = new FileMenu(scanner, this.voca, filePath);
        this.statManagement = quizManagement.getStatManagement();
    }

    /**
     * 메뉴입니다
     * @param userId
     */
    public void menu(String userId){
        int choice = 0;
        while(choice !=7) {
            cleanConsole();
            System.out.println(userId + "님의 단어장입니다.");
            System.out.println("1) 퀴즈메뉴 2) 예문 메뉴 3) 단어장 출력 4) 단어메뉴 5) 파일메뉴 6) 통계메뉴 7) 종료");
            choice = readInt(scanner, "메뉴를 선택하세요 : ");
            System.out.println();
            switch (choice) {
                case 1 -> quizManagement.menu();
                case 2 -> ExampleManagement.ex_menu(scanner,voca);
                case 3 -> {
                    printWords(voca);
                    waitConsole(scanner);
                }
                case 4 -> wordManagement.menu();
                case 5 -> fileMenu.menu();
                case 6 -> statManagement.menu(scanner);
                case 7 -> System.out.println(userId + "님의 단어장을 종료합니다.\n");
                default -> System.out.println("메뉴에 존재하지 않습니다. 다시 입력해주세요.");
            }
        }
        FileManagement.saveVoca(voca,filePath);
    }

    /**
     * 유저의 보카 파일의 존재여부를 확인하는 매서드입니다
     */
    private void ensureUserVocaExists(){
        String userDirectory = userInfo.getUserDirectory();
        File userDir = new File(userDirectory);
        if(!userDir.exists()){
            userDir.mkdirs();
        }
        File file = new File(filePath);
        if(!file.exists()){
            loadDefaultIntoUserFile();
        }
    }

    /**
     * 유저의 보카파일이 없는 것을 대비해 기본보카를 불러오는 메서드입니다
     */
    private void loadDefaultIntoUserFile(){
        Vector<Word> defaultVoca = FileManagement.makeVoca(DEFAULT_VOCA_PATH);
        FileManagement.saveVoca(new Vector<>(defaultVoca), filePath);
    }

    public Vector<Word> getVoca() {
        return voca;
    }
}

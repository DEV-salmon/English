package voca.app;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

import voca.core.UserSession;
import voca.core.Word;
import voca.management.BaseMenu;
import voca.management.ExampleManagement;
import voca.management.FileManagement;
import voca.management.QuizManagement;

public class Voca extends BaseMenu {
    private static final String DEFAULT_VOCA_PATH = "Voca/src/res/voca";
    private final Vector<Word> voca;
    private final Scanner scanner = new Scanner(System.in);
    private final String filePath;
    private final QuizManagement quizManagement;
    private final UserSession session;

    public Voca(UserSession session){
        this.session = session;
        this.filePath = session.getVocaFilePath();
        ensureUserVocaExists();
        this.voca = FileManagement.makeVoca(filePath);
        if(this.voca.isEmpty()){
            loadDefaultIntoUserFile();
            this.voca.addAll(FileManagement.makeVoca(filePath));
        }
        this.quizManagement = new QuizManagement(this.voca, session);
    }

    public void menu(String userId){
        int choice = 0;
        while(choice !=4) {
            cleanConsole();
            System.out.println(userId + "님의 단어장입니다.");
            System.out.println("1) 퀴즈 2) 예문 관리 3) 단어장 출력 4) 종료");
            choice = readInt(scanner, "메뉴를 선택하세요 : ");
            System.out.println();
            switch (choice) {
                case 1 -> quizManagement.menu();
                case 2 -> ExampleManagement.ex_menu(scanner,voca);
                case 3 -> {
                    printWords(voca);
                    waitConsole(scanner);
                }
                case 4 ->System.out.println(userId + "님의 단어장을 종료합니다.\n");
                default -> System.out.println("메뉴에 존재하지 않습니다. 다시 입력해주세요.");
            }
        }
        FileManagement.saveVoca(voca,filePath);
    }

    private void ensureUserVocaExists(){
        String userDirectory = session.getUserDirectory();
        File userDir = new File(userDirectory);
        if(!userDir.exists()){
            userDir.mkdirs();
        }
        File file = new File(filePath);
        if(!file.exists()){
            loadDefaultIntoUserFile();
        }
    }

    private void loadDefaultIntoUserFile(){
        Vector<Word> defaultVoca = FileManagement.makeVoca(DEFAULT_VOCA_PATH);
        FileManagement.saveVoca(new Vector<>(defaultVoca), filePath);
    }
}

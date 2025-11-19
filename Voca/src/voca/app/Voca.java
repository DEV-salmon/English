package voca.app;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

import voca.core.UserSession;
import voca.core.Word;
import voca.management.ExampleManagement;
import voca.management.FileManagement;
import voca.management.QuizManagement;

public class Voca {
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
        System.out.println(userId + "님의 단어장입니다.");
        int choice = 0;

        while(choice !=4) {
            System.out.println("1) 퀴즈 2) 예문 관리 3) 단어장 출력 4) 종료");
            System.out.print("메뉴를 선택하세요 : ");
            choice = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            switch (choice) {
                case 1 -> quizManagement.menu();
                case 2 -> ExampleManagement.ex_menu(scanner,voca);
                case 3 -> all_print();
                case 4 ->System.out.println(userId + "님의 단어장을 종료합니다.\n");
            }
        }
        FileManagement.saveVoca(voca,filePath);
    }

    private void all_print() {
        for(Word str : voca){
            System.out.println(str);
        }
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

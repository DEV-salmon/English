package voca.management;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import voca.core.Word;

public class FileMenu extends BaseMenu{

    private final Scanner scanner;
    private final Vector<Word> voca;
    private final String userFilePath;

    public FileMenu(Scanner scanner, Vector<Word> voca, String userFilePath){
        this.scanner = scanner;
        this.voca = voca;
        this.userFilePath = userFilePath;
    }

    public void menu(){
        int choice = 0;
        while(choice !=4){
            cleanConsole();
            System.out.println("1) 단어장 불러오기 2) 단어장 합치기 3) 현재 단어장 저장 4) 종료");
            choice = readInt(scanner, "메뉴 번호를 선택해주세요 : ");
            System.out.println();
            switch (choice){
                case 1 -> {
                    loadFromAbsolutePath();
                    waitConsole(scanner, "불러오기를 마쳤습니다. 엔터를 눌러 계속하세요...");
                }
                case 2 -> {
                    mergeFromAbsolutePath();
                    waitConsole(scanner, "합치기를 마쳤습니다. 엔터를 눌러 계속하세요...");
                }
                case 3 -> {
                    FileManagement.saveVoca(voca, userFilePath);
                    waitConsole(scanner);
                }
                case 4 -> {
                    System.out.println("파일 메뉴를 종료합니다.");
                    waitConsole(scanner);
                }
                default -> System.out.println("메뉴에 존재하지 않습니다. 다시 입력해주세요.");
            }
        }
    }

    private void loadFromAbsolutePath(){
        String path = requestPath();
        Vector<Word> loaded = FileManagement.makeVoca(path);
        if(loaded.isEmpty()){
            System.out.println("불러온 단어장이 비어있거나 읽을 수 없습니다.");
            return;
        }
        voca.clear();
        voca.addAll(loaded);
        System.out.println("단어장을 새 파일로 교체했습니다. 총 단어 수 : " + voca.size());
    }

    private void mergeFromAbsolutePath(){
        String path = requestPath();
        Vector<Word> loaded = FileManagement.makeVoca(path);
        if(loaded.isEmpty()){
            System.out.println("불러온 단어장이 비어있거나 읽을 수 없습니다.");
            return;
        }
        Set<String> existing = new HashSet<>();
        for(Word word : voca){
            existing.add(word.getEng().toLowerCase());
        }
        int added = 0;
        for(Word word : loaded){
            if(!existing.contains(word.getEng().toLowerCase())){
                voca.add(word);
                existing.add(word.getEng().toLowerCase());
                added++;
            }
        }
        System.out.println("합치기를 완료했습니다. 추가된 단어 수 : " + added + " / 총 단어 수 : " + voca.size());
    }

    private String requestPath(){
        while(true){
            System.out.print("절대 경로를 입력해주세요 : ");
            String path = scanner.nextLine().trim();
            if(path.isEmpty()){
                System.out.println("경로를 입력해주세요.");
                continue;
            }
            File file = new File(path);
            if(!file.isAbsolute()){
                System.out.println("절대 경로를 입력해주세요.");
                continue;
            }
            if(!file.exists() || !file.isFile()){
                System.out.println("해당 경로에 파일이 없습니다. 다시 입력해주세요.");
                continue;
            }
            return path;
        }
    }
}

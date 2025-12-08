package voca.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import voca.core.Word;

public class WordManagement extends BaseMenu{

    private final Scanner scanner;
    private final Vector<Word> voca;

    public WordManagement(Scanner scanner, Vector<Word> voca){
        this.scanner = scanner;
        this.voca = voca;
    }

    /**
     * 단어 관리 메인 메뉴를 보여주고 선택된 기능을 실행합니다.
     */
    public void menu(){
        int choice = 0;
        while(choice !=5){
            cleanConsole();
            System.out.println("1) 단어 추가 2) 단어 삭제 3) 단어 수정 4) 단어 검색 5) 종료");
            choice = readInt(scanner, "메뉴를 선택하세요 : ");
            System.out.println();
            switch (choice){
                case 1 -> {
                    addWord();
                    waitConsole(scanner, "단어 추가를 마쳤습니다. 엔터를 눌러 계속하세요...");
                }
                case 2 -> {
                    removeWord();
                    waitConsole(scanner, "단어 삭제를 마쳤습니다. 엔터를 눌러 계속하세요...");
                }
                case 3 -> {
                    updateWord();
                    waitConsole(scanner, "단어 수정을 마쳤습니다. 엔터를 눌러 계속하세요...");
                }
                case 4 -> searchWord();
                case 5 -> {
                    System.out.println("단어 관리 메뉴를 종료합니다.");
                    waitConsole(scanner);
                }
                default -> System.out.println("메뉴에 존재하지 않습니다. 다시 입력해주세요.");
            }
        }
    }

    /**
     * 새로운 단어를 추가합니다. 예문은 선택 입력입니다.
     */
    private void addWord(){
        System.out.println("추가할 단어의 영어 철자를 입력해주세요.");
        String eng = requestWordInput();
        Word existing = findWord(eng);
        if(existing != null){
            System.out.println("이미 존재하는 단어입니다. 단어를 수정하려면 단어 수정 메뉴를 이용해주세요.");
            return;
        }

        System.out.println("한국어 뜻을 콤마(,)로 구분해 입력해주세요. 예) 의미1, 의미2");
        String korLine = requestNonEmptyLine();
        String[] korArray = splitKor(korLine);

        System.out.println("예문을 입력해주세요. 없으면 비워두고 엔터를 눌러주세요.");
        String example = scanner.nextLine().trim();
        if(example.isEmpty()){
            voca.add(new Word(eng, korArray));
        }
        else{
            voca.add(new Word(eng, korArray, example));
        }
        System.out.println("추가된 단어:");
        System.out.println(voca.lastElement());
    }

    /**
     * 단어를 삭제하기 전에 존재 여부와 삭제 의사를 확인합니다.
     */
    private void removeWord(){
        if(voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            return;
        }
        System.out.println("삭제할 단어의 영어 철자를 입력해주세요.");
        Word target = requestExistingWord();
        if(target == null){
            return;
        }
        System.out.print("정말 삭제하시겠습니까? (예/아니오) : ");
        String confirm = scanner.nextLine().trim();
        if(confirm.equals("예")){
            voca.remove(target);
            System.out.println("단어를 삭제했습니다.");
        }
        else{
            System.out.println("삭제를 취소했습니다.");
        }
    }

    /**
     * 선택된 단어의 철자, 뜻, 예문을 수정합니다.
     */
    private void updateWord(){
        if(voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            return;
        }
        System.out.println("수정할 단어의 영어 철자를 입력해주세요.");
        Word target = requestExistingWord();
        if(target == null){
            return;
        }
        System.out.println("현재 단어 : " + target.getEng());
        System.out.println("현재 뜻   : " + String.join(", ", target.getKor()));
        System.out.println("현재 예문 : " + (target.getEx() == null ? "" : target.getEx()));

        System.out.println("\n수정할 내용을 입력해주세요. 비워두면 기존 값을 유지합니다.");

        System.out.print("새 영어 철자 : ");
        String newEng = scanner.nextLine().trim();
        if(!newEng.isEmpty()){
            target.setEng(newEng);
        }

        System.out.print("새 한국어 뜻(콤마로 구분) : ");
        String korLine = scanner.nextLine().trim();
        if(!korLine.isEmpty()){
            target.setKor(splitKor(korLine));
        }

        System.out.print("새 예문(비우면 삭제) : ");
        String exLine = scanner.nextLine().trim();
        if(exLine.isEmpty()){
            target.setEx(null);
        }
        else{
            target.setEx(exLine);
        }
        System.out.println("수정된 단어:");
        System.out.println(target);
    }

    /**
     * 단어 검색 서브 메뉴를 보여주고 검색/발음 기능을 실행합니다.
     */
    private void searchWord(){
        if(voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            waitConsole(scanner);
            return;
        }
        int choice = 0;
        while(choice != 4){
            cleanConsole();
            System.out.println("1) 전체 단어 보기 2) 특정 철자 포함 검색 3) 발음 듣기 4) 종료");
            choice = readInt(scanner, "메뉴를 선택하세요 : ");
            switch (choice){
                case 1 -> {
                    printWords(voca);
                    waitConsole(scanner);
                }
                case 2 -> runPartialSearch();
                case 3 -> playPronunciation();
                case 4 -> {
                    System.out.println("검색을 종료합니다.");
                    waitConsole(scanner);
                }
                default -> {
                    System.out.println("메뉴에 존재하지 않습니다. 다시 입력해주세요.");
                    waitConsole(scanner);
                }
            }
        }
    }

    /**
     * 입력한 부분 문자열을 포함하는 단어를 찾고 결과를 출력합니다.
     */
    private void runPartialSearch(){
        System.out.print("검색할 영어 철자(부분 문자열) : ");
        String keyword = scanner.nextLine().trim().toLowerCase();
        if(keyword.isEmpty()){
            System.out.println("검색어가 비어 있습니다.");
            waitConsole(scanner);
            return;
        }
        List<Word> result = new ArrayList<>();
        for(Word word : voca){
            if(word.getEng().toLowerCase().contains(keyword)){
                result.add(word);
            }
        }
        if(result.isEmpty()){
            System.out.println("해당 철자를 포함하는 단어가 없습니다.");
        }
        else{
            System.out.println("검색 결과:");
            for(Word word : result){
                System.out.println(word);
            }
        }
        waitConsole(scanner);
    }

    /**
     * 단어 철자를 대소문자 무시하고 찾아 반환합니다.
     */
    private Word findWord(String eng){
        for(Word word : voca){
            if(word.getEng().equalsIgnoreCase(eng)){
                return word;
            }
        }
        return null;
    }

    /**
     * 존재하는 단어만 선택하도록 검증합니다.
     */
    private Word requestExistingWord(){
        String eng = requestWordInput();
        Word found = findWord(eng);
        if(found == null){
            System.out.println("단어장을 다시 확인해주세요. 입력한 단어가 없습니다.");
            return null;
        }
        return found;
    }

    /**
     * 공백을 허용하지 않는 단어 입력을 반복해 받습니다.
     */
    private String requestWordInput(){
        while(true){
            System.out.print("단어 : ");
            String input = scanner.nextLine().trim();
            if(input.isEmpty()){
                System.out.println("단어를 입력해주세요.");
                continue;
            }
            return input;
        }
    }

    /**
     * 공백을 허용하지 않는 한 줄 입력을 반복해 받습니다.
     */
    private String requestNonEmptyLine(){
        while(true){
            String line = scanner.nextLine().trim();
            if(line.isEmpty()){
                System.out.println("값을 입력해주세요.");
                continue;
            }
            return line;
        }
    }

    /**
     * 콤마로 구분된 한국어 뜻을 잘라 깨끗하게 배열로 만듭니다.
     */
    public String[] splitKor(String korLine){
        String[] korArray = korLine.split(",");
        List<String> cleaned = new ArrayList<>();
        for(String kor : korArray){
            String trimmed = kor.trim();
            if(!trimmed.isEmpty()){
                cleaned.add(trimmed);
            }
        }
        if(cleaned.isEmpty()){
            cleaned.add(korLine.trim());
        }
        return cleaned.toArray(new String[0]);
    }

    /**
     * 지정한 단어의 영어 발음을 출력합니다.
     */
    private void playPronunciation(){
        System.out.print("발음을 들을 단어의 영어 철자를 입력하세요: ");
        String eng = scanner.nextLine().trim();
        if(eng.isEmpty()){
            System.out.println("단어를 입력해주세요.");
            waitConsole(scanner);
            return;
        }
        Word target = findWord(eng);
        if(target == null){
            System.out.println("단어장을 다시 확인해주세요. 입력한 단어가 없습니다.");
            waitConsole(scanner);
            return;
        }
        System.out.println("단어 발음을 재생합니다: " + target.getEng());
        target.voiceEng();
        waitConsole(scanner);
    }
}

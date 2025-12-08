package voca.management;

import java.util.Scanner;
import java.util.Vector;

import voca.core.Word;

/**
 * 예문 관리를 위한 클래스입니다
 */
public class ExampleManagement extends BaseMenu{

    /**
     * 메뉴입니다.
     * @param scanner
     * @param voca
     */
    public static void ex_menu(Scanner scanner, Vector<Word> voca){
        int choice = 0;

        while(choice !=6) {
            cleanConsole();
            System.out.println("1) 예문 검색 2) 예문 추가 3) 예문 삭제 4) 예문 수정 5) 단어장 출력 6) 종료");
            choice = readInt(scanner, "메뉴를 선택하세요 : ");
            System.out.println();
            switch (choice) {
                case 1 ->{
                    searchExample(scanner, voca);
                }
                case 2-> {
                    ex_put(scanner,voca);
                    waitConsole(scanner, "예문 추가를 마쳤습니다. \n엔터를 눌러 계속하세요...");
                }
                case 3-> {
                    ex_remove(scanner,voca); 
                    waitConsole(scanner, "예문 제거를 마쳤습니다. \n엔터를 눌러 계속하세요...");
                }
                case 4 ->{
                    ex_fix(scanner,voca);
                    waitConsole(scanner,"예문 수정을 마쳤습니다. \n엔터를 눌러 계속하세요...");
                }
                case 5 ->{
                    printWords(voca);
                    waitConsole(scanner, "엔터를 누르면 계속합니다...");
                }
                case 6 ->{
                    System.out.println("예문 관리 메뉴를 종료합니다.");
                    waitConsole(scanner);
                    return;
                }
                default -> System.out.println("다시 입력해주세요");
            }
        }
    }

    /**
     * 단어장의 예문을 수정하는 메소드입니다.
     * @param scanner   많은 부분에 사용하여 간소화할 방법 고민
     * @param voca  생성된 Voca객체를 가지고 옵니다.
     */
    private static void ex_fix(Scanner scanner, Vector<Word> voca) {
        if(voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            return;
        }
        System.out.println("예문을 수정하려는 단어를 적어주십시오 \n /voca 명령어를 통해 전체 단어장을 출력해볼 수 있습니다");
        String vWord = requestWord(scanner, voca);

        boolean isfound = false;

        for (Word str : voca) {
            if (str.getEng().equals(vWord)) {
                if (str.getEx() == null) {
                    System.out.println("예문이 존재하지 않습니다.\n");
                    return;
                }
                System.out.println("찾은 단어 " + str.getEng());
                System.out.println("예문 : " + str.getEx());
                System.out.print("정말 수정하시겠습니까? (예 또는 아니오) : ");
                String remove_choice = scanner.nextLine();
                if (remove_choice.equals("예")) {
                    System.out.print("수정할 예문을 적어 주십시오 : ");
                    String vEx = scanner.nextLine();
                    str.setEx(vEx);

                    System.out.println("수정된 예문 : " + str.getEx());
                    System.out.println();
                } else if (remove_choice.equals("아니오")) {
                    System.out.println("예문 수정 기능을 종료 합니다.");
                    return;
                }
                isfound = true;
                break;
            }
        }
        if (!isfound) {
            System.out.println("찾는 단어가 단어장에 존재하지 않습니다.");
            System.out.println();
        }
    }

    /**
     * 단어장의 예문을 삭제하는 메소드입니다.
     * @param scanner   많은 부분에 사용하여 간소화할 방법 고민
     * @param voca  생성된 Voca객체를 가지고 옵니다.
     */
    private static void ex_remove(Scanner scanner, Vector<Word> voca) {
        if(voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            return;
        }
        System.out.println("예문을 삭제하려는 단어를 적어주십시오 ");
        System.out.println("/voca 명령어를 통해 전체 단어장을 출력해볼 수 있습니다");
        String vWord = requestWord(scanner, voca);

        boolean isfound = false;

        for (Word str : voca) {
            if (str.getEng().equals(vWord)) {
                if(str.getEx() == null){
                    System.out.println("예문이 존재하지 않습니다.\n");
                    return;
                }
                System.out.println("찾은 단어 " + str.getEng());
                System.out.println("예문 : " + str.getEx());
                System.out.print("정말 삭제하시겠습니까? (예 또는 아니오) : ");
                String remove_choice = scanner.nextLine();
                if(remove_choice.equals("예")){
                    str.setEx(null);
                    System.out.println(str.getEng() + "의 예문을 삭제하였습니다.");
                } else if (remove_choice.equals("아니오")) {
                    System.out.println("예문 삭제 기능을 종료 합니다.");
                    return;
                }
                isfound = true;
                break;
            }
        }
        if (!isfound) {
            System.out.println("찾는 단어가 단어장에 존재하지 않습니다.");
            System.out.println();
        }
    }

    /**
     * 단어장의 예문을 추가하는 메소드입니다.
     * @param scanner   많은 부분에 사용하여 간소화할 방법 고민
     * @param voca  생성된 Voca객체를 가지고 옵니다.
     */
    public static void ex_put(Scanner scanner, Vector<Word> voca){
        if(voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            return;
        }
        System.out.println("예문을 적으려는 단어를 적어주십시오");
        System.out.println("/voca 명령어를 통해 전체 단어장을 출력해볼 수 있습니다");
        String vWord = requestWord(scanner, voca);

        boolean isfound = false;
        for (Word str : voca) {
            if (str.getEng().equals(vWord)) {
                if(str.getEx() != null){
                    System.out.println("예문이 이미 존재합니다.\n");
                    return;
                }
                System.out.println("찾은 단어 " + str.getEng());
                System.out.print("예문을 적어 주십시오 : ");
                String vEx = scanner.nextLine();
                str.setEx(vEx);

                System.out.println(str);
                System.out.println();

                isfound = true;
                break;
            }
        }
        if (!isfound) {
            System.out.println("찾는 단어가 단어장에 존재하지 않습니다.");
            System.out.println();
        }
    }

    /**
     * 예문 검색 서브 메뉴를 보여주고 조회/발음 기능을 제공합니다.
     */
    private static void searchExample(Scanner scanner, Vector<Word> voca){
        if(voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            waitConsole(scanner, "엔터를 누르면 계속합니다...");
            return;
        }
        int choice = 0;
        while(choice != 4){
            cleanConsole();
            System.out.println("1) 모든 예문 보기 2) 예문 검색 3) 발음 듣기 4) 종료");
            choice = readInt(scanner, "메뉴를 선택하세요 : ");
            switch (choice){
                case 1 -> {
                    printWords(voca);
                    waitConsole(scanner, "엔터를 누르면 계속합니다...");
                }
                case 2 -> {
                    System.out.print("검색할 영어 철자(부분 문자열) : ");
                    String keyword = scanner.nextLine().trim().toLowerCase();
                    if(keyword.isEmpty()){
                        System.out.println("검색어가 비어 있습니다.");
                        waitConsole(scanner, "엔터를 누르면 계속합니다...");
                        break;
                    }
                    Vector<Word> result = new Vector<>();
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
                    waitConsole(scanner, "엔터를 누르면 계속합니다...");
                }
                case 3 -> {
                    playExampleTts(scanner, voca);
                }
                case 4 -> {
                    System.out.println("예문 검색을 종료합니다.");
                    waitConsole(scanner, "엔터를 누르면 계속합니다...");
                }
                default -> {
                    System.out.println("메뉴에 존재하지 않습니다. 다시 입력해주세요.");
                    waitConsole(scanner, "엔터를 누르면 계속합니다...");
                }
            }
        }
    }

    /**
     * 입력한 단어의 예문을 찾아 음성으로 출력합니다.
     */
    private static void playExampleTts(Scanner scanner, Vector<Word> voca){
        System.out.print("발음을 들을 단어의 영어 철자를 입력하세요: ");
        String eng = scanner.nextLine().trim();
        if(eng.isEmpty()){
            System.out.println("단어를 입력해주세요.");
            waitConsole(scanner, "엔터를 누르면 계속합니다...");
            return;
        }
        for(Word word : voca){
            if(word.getEng().equalsIgnoreCase(eng)){
                if(word.getEx() == null || word.getEx().isEmpty()){
                    System.out.println("해당 단어에 예문이 없습니다.");
                    waitConsole(scanner, "엔터를 누르면 계속합니다...");
                    return;
                }
                System.out.println("예문 발음을 재생합니다: " + word.getEx());
                word.voiceEx();
                waitConsole(scanner, "엔터를 누르면 계속합니다...");
                return;
            }
        }
        System.out.println("단어장을 다시 확인해주세요. 입력한 단어가 없습니다.");
        waitConsole(scanner, "엔터를 누르면 계속합니다...");
    }

    /**
     * 단어 입력을 받으며 /voca 명령으로 단어장 출력도 지원합니다.
     */
    private static String requestWord(Scanner scanner, Vector<Word> voca){
        while(true){
            System.out.print("단어 : ");
            String input = scanner.nextLine().trim();
            if(input.isEmpty()){
                System.out.println("단어를 입력해주세요.");
                continue;
            }
            if(input.equalsIgnoreCase("/voca")){
                printWords(voca);
                waitConsole(scanner, "엔터를 누르면 계속합니다...");
                continue;
            }
            return input;
        }
    }
}

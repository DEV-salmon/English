package voca.management;

import java.util.Scanner;
import java.util.Vector;

import voca.core.Word;

public class ExampleManagement extends BaseMenu{

    public static void ex_menu(Scanner scanner, Vector<Word> voca){
        int choice = 0;

        while(choice !=4) {
            cleanConsole();
            System.out.println("1) 에문 추가 2) 예문 삭제 3) 예문 수정 4) 종료");
            System.out.print("메뉴를 선택하세요 : ");
            choice = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            switch (choice) {
                case 1-> {
                    ex_put(scanner,voca);
                    waitConsole(scanner, "예문 추가를 마쳤습니다. \n엔터를 눌러 계속하세요...");
                }
                case 2-> {
                    ex_remove(scanner,voca); 
                    waitConsole(scanner, "예문 제거를 마쳤습니다. \n엔터를 눌러 계속하세요...");
                }
                case 3 ->{
                    ex_fix(scanner,voca);
                    waitConsole(scanner,"예문 수정을 마쳤습니다. \n엔터를 눌러 계속하세요...");
                }
                case 4 ->{
                    System.out.println("예문 관리 메뉴를 종료합니다.");
                    waitConsole(scanner);
                }
            }
        }
    }

    /**
     * 단어장의 에문을 수정하는 메소드입니다.
     * @param scanner   많은 부분에 사용하여 간소화할 방법 고민
     * @param voca  생성된 Voca객체를 가지고 옵니다.
     */
    private static void ex_fix(Scanner scanner, Vector<Word> voca) {
        if(voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            return;
        }
        System.out.println("예문을 수정하려는 단어를 적어주십시오");
        String vWord = requestWord(scanner, voca);

        boolean isfound = false;

        for (Word str : voca) {
            if (str.getEng().equals(vWord)) {
                if (str.getEx() == null) {
                    System.out.println("에문이 존재하지 않습니다.\n");
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
     * 단어장의 에문을 삭제하는 메소드입니다.
     * @param scanner   많은 부분에 사용하여 간소화할 방법 고민
     * @param voca  생성된 Voca객체를 가지고 옵니다.
     */
    private static void ex_remove(Scanner scanner, Vector<Word> voca) {
        if(voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            return;
        }
        System.out.println("예문을 삭제하려는 단어를 적어주십시오");
        String vWord = requestWord(scanner, voca);

        boolean isfound = false;

        for (Word str : voca) {
            if (str.getEng().equals(vWord)) {
                if(str.getEx() == null){
                    System.out.println("에문이 존재하지 않습니다.\n");
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
     * 단어장의 에문을 추가하는 메소드입니다.
     * @param scanner   많은 부분에 사용하여 간소화할 방법 고민
     * @param voca  생성된 Voca객체를 가지고 옵니다.
     */
    public static void ex_put(Scanner scanner, Vector<Word> voca){
        if(voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            return;
        }
        System.out.println("예문을 적으려는 단어를 적어주십시오");
        String vWord = requestWord(scanner, voca);

        boolean isfound = false;
        for (Word str : voca) {
            if (str.getEng().equals(vWord)) {
                if(str.getEx() != null){
                    System.out.println("에문이 이미 존재합니다.\n");
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

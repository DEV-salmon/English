package voca.management;

import java.util.Scanner;
import java.util.Vector;

import voca.core.Word;

/**
 * Management, 특히 메뉴기능이 구현되어있는 모든 클래스의 부모입니다
 * Ux개선을 위한 보조 매서드들이 구현되어있습니다
 */
public class BaseMenu {

    // UX개선을 위한 콘솔창을 비우는 매서드입니다. 아스키코드를 활용해 구현했습니다. 해당 아스키 코드를 지원하지 않은 경우도 있어, 엔터키도 활용했습니다
    protected static void cleanConsole() {
        System.out.print("\u001B[2J");
        System.out.println("\n\n\n\n\n\n\n\n\n");

    }

    /**
     * 콘솔창이 비워지기 전 잠시 기다리는 메서드입니다.
     * 기본 문구는 "엔터를 누르면 계속합니다" 입니다
     */
    protected static void waitConsole(Scanner scanner) {
        waitConsole(scanner, "엔터를 누르면 계속합니다...");
    }


    protected static void waitConsole(Scanner scanner, String message) {
        if (scanner == null) {
            return;
        }
        if (message != null && !message.isEmpty()) {
            System.out.print(message);
        }
        scanner.nextLine();
    }

    /**
     * switch 구문을 사용하여 사용자에게 숫자를 받을 때, 숫자가 아닌 문자열을 입력하는 예외를 처리하고자 사용하는 메서드입니다
     * @param str
     * @return
     */
    protected static int readInt(Scanner scanner, String str){
        while(true){
            if(str != null){
                System.out.print(str);
            }
            String line = scanner.nextLine().trim();
            if(line.isEmpty()){
                System.out.println("숫자를 입력해주세요.");
                continue;
            }
            try{
                return Integer.parseInt(line);
            } catch (Exception e){
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }

    /**
     * 벡터로 저장되어있는 단어를 출력하기 위한 메서드입니다
     * @param voca
     */
    protected static void printWords(Vector<Word> voca) {
        if(voca == null || voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            return;
        }
        for(Word word : voca){
            System.out.println(word);
        }
    }
}

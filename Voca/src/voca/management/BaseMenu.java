package voca.management;

import java.util.Scanner;
import java.util.Vector;

import voca.core.Word;

public class BaseMenu {

    protected static void cleanConsole() {
        System.out.print("\u001B[2J");
    }

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

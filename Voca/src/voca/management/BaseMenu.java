package voca.management;

import java.util.Scanner;

public class BaseMenu {

    protected static void cleanConsole() {
        System.out.print("\u001B[2J");
        System.out.flush();
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
}

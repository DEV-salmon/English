package teamProject;

import java.util.Scanner;

public class Test {
    public static void main(String[] args){
        Voca voca = new Voca();
        Scanner scanner = new Scanner(System.in);
        int input;
        while(true){
            System.out.println("\n실행할 동작을 선택하세요.\n1.단어추가 2.단어삭제 3.단어수정 4.단어검색 5.단어장출력 6.종료");
            input = scanner.nextInt();
            scanner.nextLine();
            switch (input){
                case 1 -> voca.addWord();
                case 2 -> voca.removeWord();
                case 3 -> voca.modifyWord();
                case 4 -> voca.searchWord();
                case 5 -> voca.printWord();
                case 6 -> {
                    System.out.println("종료합니다.");
                    return;
                }
            }
        }
    }
}

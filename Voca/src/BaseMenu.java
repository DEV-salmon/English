import java.util.Scanner;
import java.util.Vector;

public class BaseMenu {
    public static void searchVoc2(Scanner scanner,Vector<Word> voca) {
        System.out.println("------ 단어 검색 2 ------");
        System.out.print("검색할 부분 단어를 입력하세요 (영단어) : ");
        String sWord = scanner.nextLine();
        for(Word str : voca){
            if(str.getEng().substring(0, sWord.length()).contains(sWord)){
                System.out.println(str);
            }
        }
        System.out.println();
    }


    public static void searchVoc(Scanner scanner,Vector<Word> voca) {
        System.out.println("------ 단어 검색 ------");
        System.out.print("검색할 단어를 입력하세요 (영단어) : ");
        String sWord = scanner.nextLine();
        for(Word str : voca){
            if(str.getEng().equals(sWord)){
                System.out.println((str.getEx() != null && !str.getEx().isEmpty()) ?
                        "단어의 뜻 : "+ String.join(", ", str.getKor()) +"\n" +"예문 : " + str.getEx()
                        : "단어의 뜻 : "+ String.join(", ", str.getKor()));
            }
        }
        System.out.println();
    }
}

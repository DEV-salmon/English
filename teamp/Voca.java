package teamProject;

import java.util.Objects;
import java.util.Scanner;
import java.util.Vector;

public class Voca {
    private static Vector<Word> voca = new Vector<>();

    public static void addWord(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("추가할 영어 단어를 입력하세요.");
        String eng = scanner.nextLine();
        System.out.println("추가할 영어 단어의 뜻을 입력하세요.");
        String kor = scanner.nextLine();
        System.out.println("추가할 영어 단어를 활용한 예문을 입력하세요.");
        String ex = scanner.nextLine();
        voca.add(new Word(eng, kor, ex));
    }

    public static void removeWord(){
        if(voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("삭제할 영어 단어를 입력하세요.");
        String eng = scanner.nextLine();
        for(int i = voca.size() - 1 ; i>=0;i--){
            if(voca.get(i).getEng().equals(eng)){
                voca.remove(i);
                System.out.println("단어 "+eng+"가 삭제되었습니다.");
                return;
            }
        }
        System.out.println("해당 단어를 찾을 수 없습니다.");
    }

    public static void modifyWord(){
        if(voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("수정할 영어 단어를 입력하세요.");
        String eng = scanner.nextLine();
        for(int i = voca.size() - 1 ; i>=0;i--){
            if(voca.get(i).getEng().equals(eng)){
                System.out.println("수정할 내용을 결정하세요.(1:영어 2:뜻 3:예문)");
                int input = scanner.nextInt();;
                scanner.nextLine();
                switch (input){
                    case 1 -> {
                        System.out.println("새로운 영어 단어를 입력하세요.");
                        String new_eng = scanner.nextLine();
                        voca.get(i).setEng(new_eng);
                        System.out.println("단어 "+eng+"가 "+new_eng+"로 변경되었습니다.");
                        return;
                    }
                    case 2 -> {
                        String kor = voca.get(i).getKor();
                        System.out.println("영어 단어의 새로운 뜻을 입력하세요.");
                        String new_kor = scanner.nextLine();
                        voca.get(i).setKor(new_kor);
                        System.out.println("단어 "+eng+"의 뜻 "+kor+"가 "+new_kor+"로 변경되었습니다.");
                        return;
                    }
                    case 3 -> {
                        System.out.println("영어 단어의 새로운 예문을 입력하세요.");
                        String new_ex = scanner.nextLine();
                        voca.get(i).setEx(new_ex);
                        System.out.println("단어 "+eng+"의 예문이 새롭게 변경되었습니다.");
                        return;
                    }
                    default -> {
                        System.out.println("잘못된 입력입니다.");
                        return;
                    }
                }
            }
        }
        System.out.println("해당 단어를 찾을 수 없습니다.");
    }

    public static void searchWord(){
        if(voca.isEmpty()){
            System.out.println("단어장이 비어있습니다.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("검색할 내용을 입력하세요.");
        String input = scanner.nextLine();
        for(int i = voca.size() - 1 ; i>=0;i--){
            if(voca.get(i).getEng().equals(input)){
                System.out.println("\n"+voca.get(i).toString());
                return;
            }
            if(voca.get(i).getKor().equals(input)){
                System.out.println("\n"+voca.get(i).toString());
                return;
            }
        }
        System.out.println("해당 단어를 찾을 수 없습니다.");
    }

    public static void printWord(){
        for(Word word : voca){
            System.out.println(word);
        }
    }
}

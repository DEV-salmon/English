import java.util.Scanner;
import java.util.Vector;

public class Voca {
    private static Vector<Word> voca;
    private static final Scanner scanner = new Scanner(System.in);

    Voca(String fileName){
        this.voca=FileManagement.makeVoca(fileName);
    }

        /*
        메뉴 구현 필요 기능
        1. 단어 관리
            1. 단어 추가
            2. 단어 삭제
            3. 단어 수정
            4. 단어 검색
                1. 한글 기반
                2. 영어 기반
                3. 포함 문자 기반
        2. 파일 관리
            1. 파일 저장
            2. 파일 불러오기 --> makeVoca 매서드 활용
        3. 퀴즈 모드
            1. 한 -> 영
            2. 영 -> 한
            3. 예문 빈칸 넣기
            4. 오답 노트
        4. 통계 기능
            1. (커스텀)
            2. (커스텀)
        5. 예문 관리
            1. 예문 추가
            2. 예문 삭제
            3. 예문 수정
         */

    public static void run(String loginfileName){
        LogInManagement.run(loginfileName);
    }

    static void menu(String fileName,String userId){
        System.out.println(userId + "님의 단어장입니다.");
        int choice = 0;

        while(choice !=5) {
            System.out.println("3)예문 추가 4)단어장 출력 5) 종료");
            System.out.print("메뉴를 선택하세요 : ");
            choice = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            switch (choice) {
                case 3 -> ExampleManagement.ex_menu(scanner,voca);
                case 4 -> all_print(voca);
                case 5 ->System.out.println(userId + "님의 단어장을 종료합니다.\n");
            }
        }
        FileManagement.saveVoca(voca,fileName);
    }

    private static void all_print(Vector<Word> voca) {
        for(Word str : voca){
            System.out.println(str);
        }
    }

}

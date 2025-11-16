import java.util.Scanner;
import java.util.Vector;
import java.io.File;
import java.io.FileNotFoundException;

public class Voca {
    private Vector<Word> voca=new Vector<>();
    private Scanner scanner = new Scanner(System.in);

    Voca(String fileName){
        this.voca=makeVoca(fileName);
    }

    private Vector<Word> makeVoca(String fileName){
        Vector<Word> v = new Vector<>();
        try(Scanner sc = new Scanner(new File(fileName))){
            int number = 0;

            while(sc.hasNextLine()){
                number++;
                String eng;
                String[] kor;
                String ex="";
                String line=sc.nextLine();
                String[] engKorEx = line.split("\t");
                eng = engKorEx[0].trim();
                kor = engKorEx[1].split("/");
                for (int i = 0; i < kor.length; i++) {
                    kor[i] = kor[i].trim();
                }
                if(engKorEx.length==3){
                    ex = engKorEx[2].trim();
                    v.add(new Word(eng, kor, ex));
                }
                else if(engKorEx.length==2){
                    v.add(new Word(eng, kor));
                }
                else{
                    System.out.println(number+"의 단어의 형식이 잘못되었습니다. \n 형식 오류는 다른 단어에 영향을 끼칠 수 있으니 다시 한 번 확인해주세요.");
                }
            }
        }
        catch(FileNotFoundException e){
            System.out.println("파일을 찾을 수 없습니다.");
        }
        if(v.isEmpty()){
            System.out.println("보카가 비어있습니다 다시 한 번 확인해주세요");
        }
        return v;
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

    void menu(){
        while(true){
            int choice = scanner.nextInt();
            
        }
    }
}

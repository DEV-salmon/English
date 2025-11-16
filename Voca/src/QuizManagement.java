
import java.util.*;
public class QuizManagement {
    static int hintCount = 0;
    public static void init(Vector<Word> words){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("퀴즈 메뉴입니다");
            System.out.println("1. 뜻 -> 영어 퀴즈");
            System.out.println("2. 영어 -> 퀴즈");
            System.out.println("3. 예문 빈칸 퀴즈");
            System.out.println("4. 스펠링 퀴즈");
            System.out.println("5. 뒤로가기");
            int menuNum = scanner.nextInt();
            switch (menuNum) {
                case 1 ->{
                    korEngQuiz(words, scanner);
                }
                case 2 ->{
                    engKorQuiz(words, scanner);
                }
                case 3 ->{
                    
                }
            }
    }
    }
    public static void korEngQuiz(Vector<Word> words ,Scanner scanner){
        hintCount=0;
        int score = 0;
        System.out.print("원하시는 퀴즈 문항 수를 설정해주세요 : ");
        int quizNumber=scanner.nextInt();
        List<Integer> list =pickN(words.size(),quizNumber);
        for(int i=0;i<list.size();i++){
            if(subKorEngQuiz(i, words.get(list.get(i)), scanner)){
                score++;
            }
        }
        System.out.println("힌트 사용 횟수 : "+hintCount);
        System.out.println("점수 : "+ score);
    }
    public static void engKorQuiz(Vector<Word> words,Scanner scanner){
        hintCount=0;


    }
    private static List<Integer> pickN(int len, int number) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {   // 예: 로또처럼 1~45
            list.add(i);
        }

        Collections.shuffle(list);
        return list.subList(0, number);
    }

    private static boolean subKorEngQuiz(int number, Word word, Scanner scanner){
        System.out.println(number+"번 문제 \n");
        String[] korean = word.getKor();
        String english =word.getEng();
        int hintIndex = 0;
        for(String s : korean){
            if (korean[korean.length-1].equals(s)){
                System.out.print(s+" 의 뜻이 담긴 영어 단어를 입력해주세요 \n");
                System.out.println(" /hint를 입력하시면 힌트가 출력됩니다");
                break;
            }
            System.out.print(s+" , ");
        }
        while (true) {
            System.out.println(" -> ");
            String answer = scanner.nextLine();
            answer = answer.trim();
            answer = answer.toLowerCase();
            if(isHint(answer)){
                if(hintIndex == english.length()-1){
                    System.out.println("이미 정답을 알아내셨습니다. 답을 입력해주세요");
                }
                else{
                    System.out.println("hint입니다. " + " 현재 "+ (hintCount+1)+"번 만큼 힌트를 사용하셨습니다.");
                    System.out.println(english.charAt(hintIndex));
                    hintIndex++;
                }
            }
            else{
                return answer.equals(english);
            }
        }
    }

    private static boolean isHint(String str){
        if(str.equals("/hint")){
            hintCount++;
            return true;
        }
        return false;
    }
}

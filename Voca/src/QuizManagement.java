
import java.util.*;
public class QuizManagement {
    static int hintCount = 0;
    public static void menu(Vector<Word> words){
        System.out.println("\u001B[2J");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("================================");
            System.out.println("퀴즈 메뉴");
            System.out.println("사용 가능한 명령 예시");
            System.out.println("  /hint : 철자 힌트");
            System.out.println("  /speak : 단어 또는 예문 다시 듣기");
            System.out.println("================================");
            System.out.println("1. 뜻 -> 영어 퀴즈");
            System.out.println("2. 영어 -> 뜻 퀴즈");
            System.out.println("3. 예문 빈칸 퀴즈");
            System.out.println("4. 스펠링 퀴즈");
            System.out.println("5. 오답노트 출력");
            System.out.println("6. 뒤로가기");
            System.out.print("원하시는 항목 번호를 입력해주세요 -> ");
            int menuNum = scanner.nextInt();
            switch (menuNum) {
                case 1 ->{
                    korEngQuiz(words, scanner);
                }
                case 2 ->{
                    engKorQuiz(words, scanner);
                }
                case 3 ->{
                    exampleQuiz(words, scanner);
                }
                case 4 ->{
                    spellingQuiz(words, scanner);
                }
                case 5 ->{

                }
                case 6 ->{
                    System.out.println("\u001B[2J");
                    return;
                }
                default -> System.out.println("다시 입력해주세요");
            }
            System.out.println("\u001B[2J");
        }
    }


    public static void korEngQuiz(Vector<Word> words ,Scanner scanner){
        hintCount=0;
        int score = 0;
        System.out.println("[뜻 -> 영어 퀴즈]");
        System.out.println("정답 또는 /hint를 입력하고 엔터를 눌러주세요.");
        System.out.println("불필요한 공백은 무시됩니다.");
        System.out.print("원하시는 퀴즈 문항 수를 입력해주세요 : ");
        int quizNumber=scanner.nextInt();
        scanner.nextLine();
        List<Integer> list =pickN(words.size(),quizNumber);
        for(int i=0;i<list.size();i++){
            if(subKorEngQuiz(i, words.get(list.get(i)), scanner)){
                score++;
            }
        }
        System.out.println("힌트 사용 횟수 : "+hintCount);
        System.out.println("점수 : "+ score);
        System.out.print(" 틀린 문항은 오답 노트에 기록됩니다 엔터를 눌러주세요...");
        String next = scanner.nextLine();
    }
    public static void engKorQuiz(Vector<Word> words,Scanner scanner){
        hintCount=0;
        int score = 0;
        System.out.println("[영어 -> 뜻 퀴즈]");
        System.out.println("정답 또는 /hint를 입력하고 엔터를 눌러주세요.");
        System.out.println("불필요한 공백은 무시됩니다.");
        System.out.print("원하시는 퀴즈 문항 수를 입력해주세요 : ");
        int quizNumber=scanner.nextInt();
        scanner.nextLine();
        List<Integer> list =pickN(words.size(),quizNumber);
        for(int i=0;i<list.size();i++){
            if(subEngKorQuiz(i, words.get(list.get(i)), scanner)){
                score++;
            }
        }
        System.out.println("힌트 사용 횟수 : "+hintCount);
        System.out.println("점수 : "+ score);
        System.out.print(" 틀린 문항은 오답 노트에 기록됩니다 엔터를 눌러주세요...");
        String next = scanner.nextLine();
    }

    public static void spellingQuiz(Vector<Word> words ,Scanner scanner){
        hintCount=0;
        int score = 0;
        System.out.println("[스펠링 퀴즈]");
        System.out.println("정답 또는 /hint, /speak 를 입력해주세요.");
        System.out.println("단어가 들린 뒤에 차분히 입력하면 됩니다.");
        System.out.print("원하시는 퀴즈 문항 수를 입력해주세요 : ");
        int quizNumber=scanner.nextInt();
        scanner.nextLine();
        List<Integer> list =pickN(words.size(),quizNumber);
        for(int i=0;i<list.size();i++){
            if(subSpellingQuiz(i, words.get(list.get(i)), scanner)){
                score++;
            }
        }
        System.out.println("힌트 사용 횟수 : "+hintCount);
        System.out.println("점수 : "+ score);
        System.out.print(" 틀린 문항은 오답 노트에 기록됩니다 엔터를 눌러주세요...");
        String next = scanner.nextLine();
    }

    public static void exampleQuiz(Vector<Word> words ,Scanner scanner){
        List<Integer> exampleIndices = new ArrayList<>();
        for(int i=0;i<words.size();i++){
            String example = words.get(i).getEx();
            if(example != null && !example.trim().isEmpty()){
                exampleIndices.add(i);
            }
        }
        if(exampleIndices.isEmpty()){
            System.out.println("예문이 등록된 단어가 없습니다. 예문 관리에서 먼저 예문을 추가해주세요.");
            System.out.print("엔터를 누르면 메뉴로 돌아갑니다...");
            scanner.nextLine();
            return;
        }

        hintCount=0;
        int score = 0;
        System.out.println("[예문 빈칸 퀴즈]");
        System.out.println("정답 또는 /hint, /speak 를 입력해주세요.");
        System.out.println("예문이 없는 단어는 자동으로 제외됩니다.");
        System.out.print("원하시는 퀴즈 문항 수를 입력해주세요 : ");
        int quizNumber=scanner.nextInt();
        scanner.nextLine();
        if(quizNumber <= 0){
            System.out.println("1 이상의 문항 수를 입력해주세요. 메뉴로 돌아갑니다.");
            return;
        }
        if(quizNumber > exampleIndices.size()){
            System.out.println("등록된 예문이 "+exampleIndices.size()+"개라 해당 개수만큼만 진행합니다.");
            quizNumber = exampleIndices.size();
        }
        Collections.shuffle(exampleIndices);
        for(int i=0;i<quizNumber;i++){
            if(subExampleQuiz(i, words.get(exampleIndices.get(i)), scanner)){
                score++;
            }
        }
        System.out.println("힌트 사용 횟수 : "+hintCount);
        System.out.println("점수 : "+ score);
        System.out.print(" 틀린 문항은 오답 노트에 기록됩니다 엔터를 눌러주세요...");
        String next = scanner.nextLine();
    }
    private static List<Integer> pickN(int len, int number) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            list.add(i);
        }

        Collections.shuffle(list);
        return list.subList(0, number);
    }

    private static boolean subSpellingQuiz(int number, Word word, Scanner scanner){
        System.out.println();
        System.out.println((number+1)+"번 문제 ");
        String english = word.getEng();
        word.voiceEng();
        System.out.println("방금 들려드린 단어의 스펠링을 입력해주세요.");
        System.out.println("필요하면 /hint 또는 /speak 를 사용할 수 있습니다.");
        int hintIndex = 0;
        while (true) {
            System.out.print("정답/명령 입력 -> ");
            String answer = scanner.nextLine().trim();
            if(answer.isEmpty()){
                System.out.println("입력이 비어 있습니다. 정답 또는 /hint, /speak 를 입력해주세요.");
                continue;
            }
            if(isHint(answer)){
                if(hintIndex == english.length()){
                    System.out.println("이미 정답을 알아내셨습니다. 답을 입력해주세요");
                }
                else{
                    System.out.println();
                    System.out.println("현재 "+ (hintCount)+"번 만큼 힌트를 사용하셨습니다.");
                    System.out.println("hint : "+(hintIndex+1)+"번째 철자는 "+english.charAt(hintIndex)+" 입니다");
                    hintIndex++;
                }
                continue;
            }
            if(isSpeak(answer)){
                word.voiceEng();
                System.out.println("단어를 다시 들려드렸습니다.");
                continue;
            }

            String normalized = answer.replaceAll("\\s+","").toLowerCase();
            if(normalized.equals(english.replaceAll("\\s+","").toLowerCase())){
                return true;
            }
            return false;
        }
    }

    private static boolean subExampleQuiz(int number, Word word, Scanner scanner){
        System.out.println();
        System.out.println((number+1)+"번 문제 ");
        String example = word.getEx();
        if(example == null || example.trim().isEmpty()){
            System.out.println("예문이 없어 해당 문제를 건너뜁니다.");
            return false;
        }
        String english = word.getEng();
        String blankSentence = hideAnswerInExample(example, english);
        System.out.println("예문 : "+blankSentence);
        word.voiceEx();
        System.out.println("빈칸에 들어갈 단어를 입력해주세요.");
        System.out.println("필요하면 /hint 또는 /speak 를 사용할 수 있습니다.");
        int hintIndex = 0;
        while (true) {
            System.out.print("정답/명령 입력 -> ");
            String answer = scanner.nextLine().trim();
            if(answer.isEmpty()){
                System.out.println("입력이 비어 있습니다. 정답 또는 /hint, /speak 를 입력해주세요.");
                continue;
            }
            if(isHint(answer)){
                if(hintIndex == english.length()){
                    System.out.println("이미 정답을 알아내셨습니다. 답을 입력해주세요");
                }
                else{
                    System.out.println();
                    System.out.println("현재 "+ (hintCount)+"번 만큼 힌트를 사용하셨습니다.");
                    System.out.println("hint : "+(hintIndex+1)+"번째 철자는 "+english.charAt(hintIndex)+" 입니다");
                    hintIndex++;
                }
                continue;
            }
            if(isSpeak(answer)){
                word.voiceEx();
                System.out.println("예문을 다시 들려드렸습니다.");
                continue;
            }

            String normalized = answer.replaceAll("\\s+","").toLowerCase();
            if(normalized.equals(english.replaceAll("\\s+","").toLowerCase())){
                return true;
            }
            return false;
        }
    }






    private static boolean subEngKorQuiz(int number, Word word, Scanner scanner){
        System.out.println();
        System.out.println((number+1)+"번 문제 ");
        String english = word.getEng();
        System.out.println(english+" 의 뜻을 입력해주세요  ");

        String[] korean = word.getKor();
        List<String> candidateList = new ArrayList<>();
        for(String kor : korean){
            if(kor == null){
                continue;
            }
            String[] split = kor.split(",");
            for(String candidate : split){
                String trimmed = candidate.trim();
                if(!trimmed.isEmpty()){
                    candidateList.add(trimmed);
                }
            }
        }
        if(candidateList.isEmpty()){
            candidateList.add(english);
        }

        String answerHint = candidateList.get(0);
        int hintIndex = 0;
        while (true) {
            System.out.print("정답/명령 입력 -> ");
            String answer = scanner.nextLine().trim();
            if(answer.isEmpty()){
                System.out.println("입력이 비어 있습니다. 정답 또는 /hint 를 입력해주세요.");
                continue;
            }
            if(isHint(answer)){
                if(hintIndex == answerHint.length()){
                    System.out.println("이미 정답을 알아내셨습니다. 답을 입력해주세요");
                }
                else{
                    System.out.println();
                    System.out.println("현재 "+ (hintCount)+"번 만큼 힌트를 사용하셨습니다.");
                    System.out.println("hint : "+(hintIndex+1)+"번째 철자는 "+answerHint.charAt(hintIndex)+" 입니다");
                    hintIndex++;
                }
            }
            else{
                String normalized = answer.replaceAll("\\s+","");
                for(String candidate : candidateList){
                    if(normalized.equalsIgnoreCase(candidate.replaceAll("\\s+",""))){
                        return true;
                    }
                }
                return false;
            }
        }
    }

    private static boolean subKorEngQuiz(int number, Word word, Scanner scanner){
        System.out.println();
        System.out.println((number+1)+"번 문제 ");
        String[] korean = word.getKor();
        List<String> meaningList = new ArrayList<>();
        for(String kor : korean){
            if(kor == null){
                continue;
            }
            String[] split = kor.split(",");
            for(String candidate : split){
                String trimmed = candidate.trim();
                if(!trimmed.isEmpty()){
                    meaningList.add(trimmed);
                }
            }
        }
        for(int i=0;i<korean.length;i++){
            if(i == korean.length-1){
                System.out.print(korean[i]+" 의 뜻이 담긴 영어 단어를 입력해주세요  ");
            }
            else{
                System.out.print(korean[i]+" , ");
            }
        }

        String english = word.getEng();
        if(meaningList.isEmpty()){
            meaningList.add(english);
        }
        int hintIndex = 0;
        while (true) {
            System.out.print("정답/명령 입력 -> ");
            String answer = scanner.nextLine().trim();
            if(answer.isEmpty()){
                System.out.println("입력이 비어 있습니다. 정답 또는 /hint 를 입력해주세요.");
                continue;
            }
            if(isHint(answer)){
                if(hintIndex == english.length()){
                    System.out.println("이미 정답을 알아내셨습니다. 답을 입력해주세요");
                }
                else{
                    System.out.println();
                    System.out.println("현재 "+ (hintCount)+"번 만큼 힌트를 사용하셨습니다.");
                    System.out.println("hint : "+(hintIndex+1)+"번째 철자는 "+english.charAt(hintIndex)+" 입니다");
                    hintIndex++;
                }
                continue;
            }

            String normalized = answer.replaceAll("\\s+","").toLowerCase();
            if(normalized.equals(english.replaceAll("\\s+","").toLowerCase())){
                return true;
            }
            for(String meaning : meaningList){
                if(normalized.equals(meaning.replaceAll("\\s+","").toLowerCase())){
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean isHint(String str){
        if(str.equals("/hint")){
            hintCount++;
            return true;
        }
        return false;
    }

    private static boolean isSpeak(String str){
        return str.equals("/speak");
    }

    private static String hideAnswerInExample(String example, String english){
        if(example == null || english == null || english.isEmpty()){
            return example != null ? example : "";
        }
        String blank = buildBlank(english.length());
        String lowerExample = example.toLowerCase();
        String lowerEnglish = english.toLowerCase();
        String result = "";
        boolean replaced = false;
        int i = 0;
        int targetLength = english.length();
        while(i <= example.length() - targetLength){
            if(lowerExample.startsWith(lowerEnglish, i)){
                result += blank;
                i += targetLength;
                replaced = true;
            }
            else{
                result += example.charAt(i);
                i++;
            }
        }
        if(i < example.length()){
            result += example.substring(i);
        }
        if(!replaced){
            return example;
        }
        return result;
    }

    private static String buildBlank(int length){
        if(length <= 0){
            return "_____";
        }
        String blank = "";
        for(int i=0;i<length;i++){
            blank += "_";
        }
        return blank;
    }
}

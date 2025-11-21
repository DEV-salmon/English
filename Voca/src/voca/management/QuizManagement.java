package voca.management;

import java.util.*;

import voca.core.*;
import voca.core.Word;

import static java.lang.Integer.parseInt;

public class QuizManagement extends BaseMenu {
    private static final String QUIZ_TYPE_KOR_ENG = "KOR_ENG";
    private static final String QUIZ_TYPE_ENG_KOR = "ENG_KOR";
    private static final String QUIZ_TYPE_EXAMPLE = "EXAMPLE";
    private static final String QUIZ_TYPE_SPELLING = "SPELLING";
    private static final int MODE_SUBJECTIVE = 1;
    private static final int MODE_OBJECTIVE = 2;
    private static final int OBJindex = 5;
    private final Vector<Word> words;
    private final IncorrectManagement incorrectManagement;
    private final StatManagement statManagement;
    private final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);
    private static int hintCount = 0;

    /**
     * 생성자
     * @param words: 유저의 보카 파일을 받는다
     * @param userInfo: 로그인한 유저의 보카 파일 경로를 전달하기 위해 필요하다
     */
    public QuizManagement(Vector<Word> words, UserFileInfo userInfo){
        this.words = words;
        this.incorrectManagement = new IncorrectManagement(userInfo);
        this.statManagement = new StatManagement(userInfo);
    }

    public StatManagement getStatManagement(){
        return statManagement;
    }

    /**
     * 퀴즈 메인 메뉴를 보여주고, 사용자가 고른 번호에 따라 세부 퀴즈나 오답 메뉴로 보냅니다.
     * 반복문에서 계속 묻다가 6번을 누르면 빠져나옵니다.
     * 모드 선택은 여기서 처리하고, 실제 퀴즈 로직은 각 함수로 넘깁니다.
     */
    public void menu(){
        incorrectManagement.initializeIncorrectNotes();
        cleanConsole();
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
            System.out.println("5. 오답 메뉴");
            System.out.println("6. 뒤로가기");
            int menuNum = readInt(scanner, "원하시는 항목 번호를 입력해주세요 -> ");
            switch (menuNum) {
                case 1 ->{
                    int mode = modeSelect();
                    if(mode == MODE_OBJECTIVE){
                        OBJkorEngQuiz();
                    }
                    else{
                        SUBkorEngQuiz();
                    }
                }
                case 2 ->{
                    int mode = modeSelect();
                    if(mode == MODE_OBJECTIVE){
                        OBJengKorQuiz();
                    }
                    else{
                        SUBengKorQuiz();
                    }
                }
                case 3 ->{
                    int mode = modeSelect();
                        exampleQuiz(mode);
                }
                case 4 ->{
                    int mode = modeSelect();
                    if(mode == MODE_OBJECTIVE){
                        OBJspellingQuiz();
                    }
                    else{
                        SUBspellingQuiz();
                    }
                }
                case 5 ->{
                    incorrectManagement.menu(scanner, this);
                }
                case 6 ->{
                    cleanConsole();
                    return;
                }
                default -> System.out.println("메뉴에 존재하지 않습니다. 다시 입력해주세요.");
            }
            cleanConsole();
        }
    }

    
    /**
     * 뜻 -> 영어 주관식 퀴즈를 실행합니다.
     * 문제 수를 받고, 랜덤한 단어를 골라 한 문제씩 `subSUBKorEngQuiz`로 묻습니다.
     * 맞추면 통계 정답을 올리고, 틀리면 오답 노트에 기록합니다.
     */
    public void SUBkorEngQuiz(){
        hintCount=0;
        int score = 0;
        System.out.println("[뜻 -> 영어 퀴즈]");
        System.out.println("정답 또는 /hint를 입력하고 엔터를 눌러주세요.");
        System.out.println("불필요한 공백은 무시됩니다.");
        int quizNumber = requestQuizCount(words.size());
        if(quizNumber <= 0){
            return;
        }
        List<Integer> list =pickN(words.size(),quizNumber);
        for(int i=0;i<list.size();i++){
            Word word = words.get(list.get(i));
            boolean correct = subSUBKorEngQuiz(i, word);
            if(correct){
                score++;
                statManagement.addKorEngCorrect();
            }
            else{
                incorrectManagement.recordIncorrect(word, QUIZ_TYPE_KOR_ENG);
                statManagement.addKorEngWrong();
            }
        }
        System.out.println("힌트 사용 횟수 : "+hintCount);
        System.out.println("점수 : "+ score);
        statManagement.saveStatToFileWithWait(scanner, " 틀린 문항은 오답 노트에 기록됩니다 엔터를 누르면 통계를 저장합니다...");
    }

    /**
     * 영어 -> 뜻 주관식 퀴즈를 실행합니다.
     * 문제 수만큼 단어를 뽑아 `subSUBEngKorQuiz`로 답을 입력받습니다.
     * 각 문제 결과에 따라 통계/오답 기록을 업데이트합니다.
     */
    public void SUBengKorQuiz(){
        hintCount=0;
        int score = 0;
        System.out.println("[영어 -> 뜻 퀴즈]");
        System.out.println("정답 또는 /hint를 입력하고 엔터를 눌러주세요.");
        System.out.println("불필요한 공백은 무시됩니다.");
        int quizNumber = requestQuizCount(words.size());
        if(quizNumber <= 0){
            return;
        }
        List<Integer> list =pickN(words.size(),quizNumber);
        for(int i=0;i<list.size();i++){
            Word word = words.get(list.get(i));
            boolean correct = subSUBEngKorQuiz(i, word);
            if(correct){
                score++;
                statManagement.addEngKorCorrect();
            }
            else{
                incorrectManagement.recordIncorrect(word, QUIZ_TYPE_ENG_KOR);
                statManagement.addEngKorWrong();
            }
        }
        System.out.println("힌트 사용 횟수 : "+hintCount);
        System.out.println("점수 : "+ score);
        statManagement.saveStatToFileWithWait(scanner, " 틀린 문항은 오답 노트에 기록됩니다 엔터를 누르면 통계를 저장합니다...");
    }

    /**
     * 뜻 -> 영어 객관식 퀴즈를 실행합니다.
     * 보기 번호를 고르고, 결과에 따라 통계/오답을 업데이트합니다.
     */
    public void OBJkorEngQuiz(){
        hintCount=0;
        int score = 0;
        System.out.println("[뜻 -> 영어 객관식 퀴즈]");
        int quizNumber = requestQuizCount(words.size());
        if(quizNumber <= 0){
            return;
        }
        List<Integer> list =pickN(words.size(),quizNumber);
        for(int i=0;i<list.size();i++){
            Word word = words.get(list.get(i));
            boolean correct = subOBJKorEngQuiz(i, word);
            if(correct){
                score++;
                statManagement.addKorEngCorrect();
            }
            else{
                incorrectManagement.recordIncorrect(word, QUIZ_TYPE_KOR_ENG);
                statManagement.addKorEngWrong();
            }
        }
        System.out.println("점수 : "+ score);
        statManagement.saveStatToFileWithWait(scanner, " 틀린 문항은 오답 노트에 기록됩니다 엔터를 누르면 통계를 저장합니다...");
    }

    /**
     * 영어 -> 뜻 객관식 퀴즈를 실행합니다.
     * /speak 를 이용해 다시 들을 수 있다는 안내를 넣었고, 보기 중에서 한글 뜻을 고르게 합니다.
     */
    public void OBJengKorQuiz(){
        hintCount=0;
        int score = 0;
        System.out.println("[영어 -> 뜻 객관식 퀴즈]");
        int quizNumber = requestQuizCount(words.size());
        if(quizNumber <= 0){
            return;
        }
        List<Integer> list =pickN(words.size(),quizNumber);
        for(int i=0;i<list.size();i++){
            Word word = words.get(list.get(i));
            boolean correct = subOBJEngKorQuiz(i, word);
            if(correct){
                score++;
                statManagement.addEngKorCorrect();
            }
            else{
                incorrectManagement.recordIncorrect(word, QUIZ_TYPE_ENG_KOR);
                statManagement.addEngKorWrong();
            }
        }
        System.out.println("점수 : "+ score);
        statManagement.saveStatToFileWithWait(scanner, " 틀린 문항은 오답 노트에 기록됩니다 엔터를 누르면 통계를 저장합니다...");
    }

    /**
     * 스펠링 주관식 퀴즈를 실행합니다.
     * 발음을 들려준 뒤 철자를 직접 입력받고, 힌트/다시듣기 명령을 처리합니다.
     */
    public void SUBspellingQuiz(){
        hintCount=0;
        int score = 0;
        System.out.println("[스펠링 퀴즈]");
        System.out.println("정답 또는 /hint, /speak 를 입력해주세요.");
        System.out.println("단어가 들린 뒤에 차분히 입력하면 됩니다.");
        int quizNumber = requestQuizCount(words.size());
        if(quizNumber <= 0){
            return;
        }
        List<Integer> list =pickN(words.size(),quizNumber);
        for(int i=0;i<list.size();i++){
            Word word = words.get(list.get(i));
            boolean correct = subSUBSpellingQuiz(i, word);
            if(correct){
                score++;
                statManagement.addSpellingCorrect();
            }
            else{
                incorrectManagement.recordIncorrect(word, QUIZ_TYPE_SPELLING);
                statManagement.addSpellingWrong();
            }
        }
        System.out.println("힌트 사용 횟수 : "+hintCount);
        System.out.println("점수 : "+ score);
        statManagement.saveStatToFileWithWait(scanner, " 틀린 문항은 오답 노트에 기록됩니다 엔터를 누르면 통계를 저장합니다...");
    }

    /**
     * 스펠링 객관식 퀴즈를 실행합니다.
     * 발음을 들려주고 보기에서 철자를 고르게 합니다.
     */
    public void OBJspellingQuiz(){
        hintCount=0;
        int score = 0;
        System.out.println("[스펠링 객관식 퀴즈]");
        int quizNumber = requestQuizCount(words.size());
        if(quizNumber <= 0){
            return;
        }
        List<Integer> list =pickN(words.size(),quizNumber);
        for(int i=0;i<list.size();i++){
            Word word = words.get(list.get(i));
            boolean correct = subOBJSpellingQuiz(i, word);
            if(correct){
                score++;
                statManagement.addSpellingCorrect();
            }
            else{
                incorrectManagement.recordIncorrect(word, QUIZ_TYPE_SPELLING);
                statManagement.addSpellingWrong();
            }
        }
        System.out.println("점수 : "+ score);
        statManagement.saveStatToFileWithWait(scanner, " 틀린 문항은 오답 노트에 기록됩니다 엔터를 누르면 통계를 저장합니다...");
    }

    /**
     * 예문 빈칸 퀴즈를 실행합니다.
     * 예문이 있는 단어만 걸러서 대상 목록을 만들고, 모드(주관/객관)에 따라 다른 출제 함수를 씁니다.
     * 문제 수를 초과 입력하면 가능한 최대치로 맞춰주고, 결과는 통계/오답에 반영됩니다.
     */
    public void exampleQuiz(int mode){
        List<Integer> exampleIndices = new ArrayList<>();
        for(int i=0;i<words.size();i++){
            String example = words.get(i).getEx();
            if(example != null && !example.trim().isEmpty()){
                exampleIndices.add(i);
            }
        }
        if(exampleIndices.isEmpty()){
            System.out.println("예문이 등록된 단어가 없습니다. 예문 관리에서 먼저 예문을 추가해주세요.");
            waitConsole(scanner, "엔터를 누르면 메뉴로 돌아갑니다...");
            return;
        }

        hintCount=0;
        int score = 0;
        System.out.println(mode == 1 ? "[예문 빈칸 주관식 퀴즈]" :"[예문 빈칸 객관식 퀴즈]");
        System.out.println("정답 또는 /hint, /speak 를 입력해주세요.");
        System.out.println("예문이 없는 단어는 자동으로 제외됩니다.");
        int quizNumber = readInt(scanner, "원하시는 퀴즈 문항 수를 입력해주세요 : ");
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
            Word word = words.get(exampleIndices.get(i));
            boolean correct = mode == 1? subSUBExampleQuiz(i, word) : subOBJExampleQuiz(i, word);
            if(correct){
                score++;
                statManagement.addExampleCorrect();
            }
            else{
                incorrectManagement.recordIncorrect(word, QUIZ_TYPE_EXAMPLE+(mode == 1?" - SUBJECTIVE":" - OBJECTIVE"));
                statManagement.addExampleWrong();
            }
        }
        System.out.println("힌트 사용 횟수 : "+hintCount);
        System.out.println("점수 : "+ score);
        statManagement.saveStatToFileWithWait(scanner, " 틀린 문항은 오답 노트에 기록됩니다 엔터를 누르면 통계를 저장합니다...");
    }

    /**
     * 오답 노트에 담긴 단어를 다시 푸는 퀴즈를 실행합니다.
     * 유형 정보에 따라 적절한 질의 함수를 호출하고, 다시 맞히면 오답 기록을 지웁니다.
     */
    public void runIncorrectQuiz(Vector<IncorrectWord> incorrectWords){
        if(incorrectWords == null || incorrectWords.isEmpty()){
            System.out.println("기록된 오답이 없습니다.");
            waitConsole(scanner, "엔터를 누르면 이전 메뉴로 돌아갑니다...");
            return;
        }
        hintCount=0;
        int score = 0;
        System.out.println("[오답 퀴즈]");
        System.out.println("기록된 유형별로 다시 푸는 퀴즈입니다.");
        for(int i=0;i<incorrectWords.size();i++){
            IncorrectWord word = incorrectWords.get(i);
            String quizType = word.getQuizType();
            if(quizType == null || quizType.isEmpty()){
                quizType = QUIZ_TYPE_ENG_KOR;
            }
            boolean correct = askIncorrectQuestion(quizType, i, word);
            if(correct){
                score++;
                incorrectManagement.markCorrect(word);
            }
            else{
                incorrectManagement.recordIncorrect(word, quizType);
            }
        }
        System.out.println("힌트 사용 횟수 : "+hintCount);
        System.out.println("점수 : "+ score);
        System.out.print("엔터를 누르면 오답 메뉴로 돌아갑니다...");
        scanner.nextLine();
    }

    /**
     * 오답 유형 문자열을 보고 어떤 퀴즈로 물어볼지 결정합니다.
     * 알 수 없는 유형이면 기본적으로 영어->뜻 주관식으로 진행합니다.
     */
    private boolean askIncorrectQuestion(String quizType, int number, Word word){
        String type = normalizeQuizType(quizType);
        switch (type){
            case QUIZ_TYPE_KOR_ENG -> {
                return subSUBKorEngQuiz(number, word);
            }
            case QUIZ_TYPE_EXAMPLE -> {
                if(word.getEx() == null || word.getEx().trim().isEmpty()){
                    System.out.println("예문 정보가 없어 해당 문제를 건너뜁니다.");
                    return true;
                }
                return subSUBExampleQuiz(number, word);
            }
            case QUIZ_TYPE_SPELLING -> {
                return subSUBSpellingQuiz(number, word);
            }
            case QUIZ_TYPE_ENG_KOR -> {
                return subSUBEngKorQuiz(number, word);
            }
            default -> {
                System.out.println("알 수 없는 퀴즈 유형입니다. 영어->뜻 퀴즈로 진행합니다.");
                return subSUBEngKorQuiz(number, word);
            }
        }
    }

    /**
     * 오답 유형 문자열 앞부분을 표준 키워드로 통일합니다.
     * 일치하는 접두사가 없으면 그대로 반환합니다.
     */
    private String normalizeQuizType(String quizType){
        if(quizType == null || quizType.isEmpty()){
            return QUIZ_TYPE_ENG_KOR;
        }
        if(quizType.startsWith(QUIZ_TYPE_KOR_ENG)){
            return QUIZ_TYPE_KOR_ENG;
        }
        if(quizType.startsWith(QUIZ_TYPE_EXAMPLE)){
            return QUIZ_TYPE_EXAMPLE;
        }
        if(quizType.startsWith(QUIZ_TYPE_SPELLING)){
            return QUIZ_TYPE_SPELLING;
        }
        if(quizType.startsWith(QUIZ_TYPE_ENG_KOR)){
            return QUIZ_TYPE_ENG_KOR;
        }
        return quizType;
    }
    
    /**
     * 안전하게 퀴즈 문항 수를 입력받습니다.
     * 0 이하 입력 시 다시 묻고, 최대치보다 크면 가능한 최대 개수를 반환합니다.
     */
    private int requestQuizCount(int available){
        if(available <= 0){
            System.out.println("등록된 단어가 없어 퀴즈를 진행할 수 없습니다.");
            waitConsole(scanner, "엔터를 누르면 이전 메뉴로 돌아갑니다...");
            return 0;
        }
        // 퀴즈 문항 수를 안전하게 입력받습니다.
        while(true){
            int quizNumber = readInt(scanner, "원하시는 퀴즈 문항 수를 입력해주세요 : ");
            if(quizNumber <= 0){
                System.out.println("1 이상의 문항 수를 입력해주세요.");
                continue;
            }
            if(quizNumber > available){
                System.out.println("등록된 단어가 "+available+"개라 해당 개수만큼만 진행합니다.");
                return available;
            }
            return quizNumber;
        }
    }

    /**
     * 객관식/주관식 모드를 입력받습니다.
     * 1 또는 2를 입력할 때까지 반복하고, 숫자가 아니면 계속 안내합니다.
     */
    private int modeSelect(){
        while(true){
            System.out.println("퀴즈 모드를 선택하세요.");
            System.out.println("1. 주관식");
            System.out.println("2. 객관식");
            int mode = readInt(scanner, "모드를 선택해주세요 : ");
            if(mode == MODE_SUBJECTIVE || mode == MODE_OBJECTIVE){
                return mode;
            }
            System.out.println("1 또는 2를 입력해주세요.");
        }
    }

    /**
     * 0부터 len-1까지 번호 중 원하는 개수만큼 랜덤으로 뽑아 리스트로 반환합니다.
     * 음수나 len보다 큰 값이 들어오면 범위를 맞춰줍니다.
     */
    private static List<Integer> pickN(int len, int number) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            list.add(i);
        }

        Collections.shuffle(list);
        if(number < 0){
            number = 0;
        }
        if(number > len){
            number = len;
        }
        return new ArrayList<>(list.subList(0, number));
    }

    /**
     * 스펠링 주관식 한 문제를 묻습니다.
     * @param number: 문제 번호(0부터 시작)
     * @param word: 현재 단어 객체
     * @return 정답이면 true, 틀리면 false
     */
    private boolean subSUBSpellingQuiz(int number, Word word){
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

            String normalized = normalize(answer);
            if(normalized.equals(normalize(english))){
                return true;
            }
            return false;
        }
    }

    /**
     * 예문 빈칸 주관식 한 문제를 묻습니다.
     * 예문에서 정답 단어를 언더바로 가리고, /hint와 /speak 명령을 처리합니다.
     */
    private boolean subSUBExampleQuiz(int number, Word word){
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

            String normalized = normalize(answer);
            if(normalized.equals(normalize(english))){
                return true;
            }
            return false;
        }
    }
    /**
     * 예문 빈칸 객관식 한 문제를 묻습니다.
     * 정답 단어와 다른 영어 단어 4개를 섞어 보기로 보여주고 번호를 입력받습니다.
     */
    private boolean subOBJExampleQuiz(int number, Word word){
        System.out.println();
        System.out.println((number+1)+"번 문제 ");
        String example = word.getEx();
        if(example == null || example.trim().isEmpty()){
            System.out.println("예문이 없어 해당 문제를 건너뜁니다.");
            return false;
        }
        String english = word.getEng();
        String blankSentence = hideAnswerInExample(example, english);
        System.out.println("예문 : "+blankSentence+"\n");
        word.voiceEx();

        List<String> engExList = new ArrayList<>();
        Set<String> unqList = new HashSet<>();
        while (engExList.size() < 4) {  // 원하는 개수
            String addEnglish = words.get(random.nextInt(words.size())).getEng();
            if (addEnglish != null && !addEnglish.equals(english) && unqList.add(addEnglish)) {
                engExList.add(addEnglish);
            }
        }
        engExList.add(english);
        Collections.shuffle(engExList);
        System.out.println();
        for(int i =0;i<engExList.size();i++){
            System.out.println((i+1)+"번 : "+engExList.get(i));
        }

        System.out.println("\n빈칸에 들어갈 단어에 해당하는 번호를 입력해주세요.");
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



            String normalized = normalize(engExList.get(parseInt(answer)-1));
            if(normalized.equals(normalize(english))){
                return true;
            }
            return false;
        }
    }

    /**
     * 영어 -> 뜻 객관식 한 문제를 묻습니다.
     * /speak 로 발음을 다시 들려줄 수 있고, 번호 입력을 검증합니다.
     */
    private boolean subOBJEngKorQuiz(int number, Word word){
        System.out.println();
        System.out.println((number+1)+"번 문제 ");
        String english = word.getEng();
        System.out.println(english+" 의 뜻에 해당하는 번호를 선택해주세요.");
        List<String> options = buildKoreanOptions(word);
        printOptions(options);
        System.out.println("\n필요하면 /speak 를 사용해 발음을 다시 들을 수 있습니다.");
        while(true){
            System.out.print("정답/명령 입력 -> ");
            String input = scanner.nextLine().trim();
            if(input.isEmpty()){
                System.out.println("입력이 비어 있습니다. 번호를 입력하거나 /speak 를 사용해주세요.");
                continue;
            }
            if(isSpeak(input)){
                word.voiceEng();
                System.out.println("단어를 다시 들려드렸습니다.");
                continue;
            }
            int selected = parseChoice(input, options.size());
            if(selected == -1){
                continue;
            }
            String selectedValue = options.get(selected);
            if(normalize(selectedValue).equals(normalize(getPrimaryMeaning(word)))){
                return true;
            }
            return false;
        }
    }

    /**
     * 뜻 -> 영어 객관식 한 문제를 묻습니다.
     * 한국어 뜻을 보여주고 영어 보기에서 고르게 합니다.
     */
    private boolean subOBJKorEngQuiz(int number, Word word){
        System.out.println();
        System.out.println((number+1)+"번 문제 ");
        String[] korean = word.getKor();
        for(int i=0;i<korean.length;i++){
            if(i == korean.length-1){
                System.out.print(korean[i]+" 의 뜻이 담긴 영어 단어를 선택해주세요  ");
            }
            else{
                System.out.print(korean[i]+" , ");
            }
        }
        List<String> options = buildEnglishOptions(word);
        printOptions(options);
        while(true){
            System.out.print("정답 번호를 입력해주세요 -> ");
            String input = scanner.nextLine().trim();
            if(input.isEmpty()){
                System.out.println("입력이 비어 있습니다. 번호를 입력해주세요.");
                continue;
            }
            int selected = parseChoice(input, options.size());
            if(selected == -1){
                continue;
            }
            String selectedValue = options.get(selected);
            if(normalize(selectedValue).equals(normalize(word.getEng()))){
                return true;
            }
            return false;
        }
    }

    /**
     * 스펠링 객관식 한 문제를 묻습니다.
     * 발음을 들려준 뒤 영어 보기에서 철자를 고르게 합니다.
     */
    private boolean subOBJSpellingQuiz(int number, Word word){
        System.out.println();
        System.out.println((number+1)+"번 문제 ");
        word.voiceEng();
        System.out.println("방금 들려드린 단어의 스펠링을 고르세요.");
        List<String> options = buildEnglishOptions(word);
        printOptions(options);
        System.out.println("\n필요하면 /speak 를 사용해 단어를 다시 들을 수 있습니다.");
        while(true){
            System.out.print("정답/명령 입력 -> ");
            String input = scanner.nextLine().trim();
            if(input.isEmpty()){
                System.out.println("입력이 비어 있습니다. 번호를 입력하거나 /speak 를 사용해주세요.");
                continue;
            }
            if(isSpeak(input)){
                word.voiceEng();
                System.out.println("단어를 다시 들려드렸습니다.");
                continue;
            }
            int selected = parseChoice(input, options.size());
            if(selected == -1){
                continue;
            }
            String selectedValue = options.get(selected);
            if(normalize(selectedValue).equals(normalize(word.getEng()))){
                return true;
            }
            return false;
        }
    }

    /**
     * 영어 -> 뜻 주관식 한 문제를 묻습니다.
     * 한글 뜻 배열을 콤마로 분리해 여러 정답을 인정하고, /hint 명령을 지원합니다.
     */
    private boolean subSUBEngKorQuiz(int number, Word word){
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
                String normalized = normalize(answer);
                for(String candidate : candidateList){
                    if(normalized.equals(normalize(candidate))){
                        return true;
                    }
                }
                return false;
            }
        }
    }

    /**
     * 뜻 -> 영어 주관식 한 문제를 묻습니다.
     * 힌트로 영어 철자를 한 글자씩 보여주며, 한글 뜻도 정답으로 인정합니다.
     */
    private boolean subSUBKorEngQuiz(int number, Word word){
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

            String normalized = normalize(answer);
            if(normalized.equals(normalize(english))){
                return true;
            }
            for(String meaning : meaningList){
                if(normalized.equals(normalize(meaning))){
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 객관식 번호 문자열을 숫자로 바꾼 뒤 범위를 확인합니다.
     * @return 유효하면 0부터 시작하는 인덱스, 아니면 -1
     */
    private int parseChoice(String input, int optionSize){
        try{
            int value = Integer.parseInt(input);
            if(value < 1 || value > optionSize){
                System.out.println("1부터 "+optionSize+" 사이의 번호를 입력해주세요.");
                return -1;
            }
            return value-1;
        }
        catch (NumberFormatException e){
            System.out.println("숫자만 입력해주세요.");
            return -1;
        }
    }

    /**
     * 보기 문자열 리스트를 1번부터 순서대로 출력합니다.
     */
    private void printOptions(List<String> options){
        for(int i=0;i<options.size();i++){
            System.out.println((i+1)+"번 : "+options.get(i));
        }
    }

    /**
     * 영어 단어 보기를 만듭니다.
     * 정답을 넣고, 다른 단어에서 중복되지 않는 오답을 뽑아 OBJindex 개수까지 채웁니다.
     */
    private List<String> buildEnglishOptions(Word answer){
        Set<String> options = new LinkedHashSet<>();
        String english = answer.getEng();
        if(english != null && !english.trim().isEmpty()){
            options.add(english);
        }
        List<Word> shuffled = new ArrayList<>(words);
        Collections.shuffle(shuffled);
        for(Word candidate : shuffled){
            if(options.size() >= OBJindex){
                break;
            }
            if(candidate == null){
                continue;
            }
            String candidateEng = candidate.getEng();
            if(candidateEng == null || candidateEng.trim().isEmpty()){
                continue;
            }
            if(isSameNormalized(candidateEng, english)){
                continue;
            }
            boolean duplicated = false;
            for(String opt : options){
                if(isSameNormalized(opt, candidateEng)){
                    duplicated = true;
                    break;
                }
            }
            if(!duplicated){
                options.add(candidateEng);
            }
        }
        List<String> optionList = new ArrayList<>(options);
        Collections.shuffle(optionList);
        return optionList;
    }

    /**
     * 한글 뜻 보기를 만듭니다.
     * 첫 번째 의미를 정답으로 넣고, 다른 단어의 첫 의미로 오답을 채웁니다.
     */
    private List<String> buildKoreanOptions(Word answer){
        Set<String> options = new LinkedHashSet<>();
        String primaryMeaning = getPrimaryMeaning(answer);
        if(!primaryMeaning.isEmpty()){
            options.add(primaryMeaning);
        }
        List<Word> shuffled = new ArrayList<>(words);
        Collections.shuffle(shuffled);
        for(Word candidate : shuffled){
            if(options.size() >= OBJindex){
                break;
            }
            if(candidate == null){
                continue;
            }
            String meaning = getPrimaryMeaning(candidate);
            if(meaning.isEmpty() || isSameNormalized(meaning, primaryMeaning)){
                continue;
            }
            boolean duplicated = false;
            for(String opt : options){
                if(isSameNormalized(opt, meaning)){
                    duplicated = true;
                    break;
                }
            }
            if(!duplicated){
                options.add(meaning);
            }
        }
        List<String> optionList = new ArrayList<>(options);
        Collections.shuffle(optionList);
        return optionList;
    }

    /**
     * 단어의 첫 번째 한글 뜻을 돌려줍니다.
     * 한글 뜻이 없다면 영어 단어를 대신 돌려줍니다.
     */
    private String getPrimaryMeaning(Word word){
        List<String> meanings = extractMeanings(word);
        if(!meanings.isEmpty()){
            return meanings.get(0);
        }
        String english = word.getEng();
        if(english == null){
            return "";
        }
        return english;
    }

    /**
     * Word 객체에서 한글 뜻을 모두 뽑아 리스트로 만듭니다.
     * 콤마로 여러 뜻을 구분하고, 공백/빈칸은 제외합니다.
     */
    private List<String> extractMeanings(Word word){
        List<String> meaningList = new ArrayList<>();
        String[] koreans = word.getKor();
        for(String kor : koreans){
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
        return meaningList;
    }

    /**
     * normalize 후 문자열이 같은지 비교합니다.
     */
    private boolean isSameNormalized(String first, String second){
        return normalize(first).equals(normalize(second));
    }

    /**
     * 입력이 /hint인지 확인하고, 맞으면 힌트 사용 횟수를 올립니다.
     */
    private static boolean isHint(String str){
        if(str.equals("/hint")){
            hintCount++;
            return true;
        }
        return false;
    }

    /**
     * 입력이 /speak 명령인지 확인합니다.
     */
    private static boolean isSpeak(String str){
        return str.equals("/speak");
    }

    /**
     * 공백과 대소문자를 무시하기 위해 문자열을 소문자+공백 제거 형태로 변환합니다.
     */
    private static String normalize(String input){
        if(input == null){
            return "";
        }
        String normalized = "";
        for(int i=0;i<input.length();i++){
            char ch = input.charAt(i);
            if(!Character.isWhitespace(ch)){
                normalized += Character.toLowerCase(ch);
            }
        }
        return normalized;
    }

    /**
     * 예문에서 정답 단어를 언더바로 가리고 반환합니다.
     * 정답이 안 보이면 원문을 그대로 돌려줍니다.
     */
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

    /**
     * 정답 길이에 맞춰 언더바로 된 빈칸 문자열을 만듭니다.
     */
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

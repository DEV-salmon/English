package voca.management;

import java.io.*;
import java.util.*;

import voca.core.*;
import voca.core.Word;

public class IncorrectManagement extends BaseMenu {
    private final String incorrectFilePath;
    private final Vector<IncorrectWord> notes = new Vector<>();
    private final Set<String> noteKeys = new HashSet<>();

    /**
     * 오답 관리자를 초기화합니다.
     * @param userInfo 파일 경로 정보를 담은 사용자 정보
     */
    public IncorrectManagement(UserFileInfo userInfo){
        this.incorrectFilePath = userInfo.getIncorrectFilePath();
        ensureFile();
    }

    /**
     * 오답 메뉴를 보여주고 선택된 기능을 실행합니다.
     */
    public void menu(Scanner scanner, QuizManagement quizManagement){
        initializeIncorrectNotes();
        cleanConsole();
        while(true){
            System.out.println("===== 오답 메뉴 =====");
            System.out.println("1. 오답 퀴즈");
            System.out.println("2. 오답 초기화");
            System.out.println("3. 오답 노트 출력");
            System.out.println("4. 되돌아가기");
            System.out.print("-> ");
            int choice = readInt(scanner, "");
            switch (choice){
                case 1 -> quizManagement.runIncorrectQuiz(getIncorrectWordsSnapshot());
                case 2 -> clearIncorrect(scanner);
                case 3 -> printIncorrectNotes(scanner);
                case 4 -> {
                    System.out.println("이전 메뉴로 돌아갑니다.");
                    return;
                }
                default -> System.out.println("다시 입력해주세요");
            }
            cleanConsole();
        }
    }

    /**
     * 오답을 파일에 추가합니다. (중복은 noteKeys로 방지)
     */
    public void recordIncorrect(Word word, String quizType){
        if(word == null){
            return;
        }
        String type = quizType != null ? quizType : "";
        initializeIncorrectNotes();
        ensureFile();
        String eng = safeString(word.getEng());
        String kor = safeString(String.join(", ", word.getKor()));
        String ex = safeString(word.getEx());
        String key = buildKey(eng, kor, ex, type);
        if(noteKeys.contains(key)){
            return;
        }
        try(PrintWriter writer = new PrintWriter(new FileWriter(incorrectFilePath, true))){
            writer.println(formatLine(eng, kor, ex, type));
            noteKeys.add(key);
            notes.add(buildIncorrectWord(eng, kor, ex, type));
        }
        catch (IOException e){
            System.out.println("오답 노트를 기록하는 동안 오류가 발생했습니다 : "+e.getMessage());
        }
    }

    /**
     * 맞춘 단어를 오답 목록에서 제거하고 파일을 갱신합니다.
     */
    public void markCorrect(IncorrectWord word){
        if(word == null){
            return;
        }
        initializeIncorrectNotes();
        String key = buildKey(word, word.getQuizType());
        if(!noteKeys.remove(key)){
            return;
        }
        notes.removeIf(existing -> buildKey(existing, existing.getQuizType()).equals(key));
        rewriteIncorrectFile();
    }

    /**
     * 오답 노트 파일을 읽어 현재 목록을 준비합니다.
     */
    public void initializeIncorrectNotes(){
        ensureFile();
        Vector<IncorrectWord> parsed = parseIncorrectWords();
        rebuildNotes(parsed);
        rewriteIncorrectFile();
    }

    /**
     * 오답 노트를 비웁니다. (목록/키 모두 초기화)
     */
    private void clearIncorrect(Scanner scanner){
        ensureFile();
        try(PrintWriter writer = new PrintWriter(new FileWriter(incorrectFilePath))){
            writer.print("");
            System.out.println("오답 노트를 초기화했습니다.");
            waitConsole(scanner, "엔터를 누르면 이전 메뉴로 돌아갑니다...");
            notes.clear();
            noteKeys.clear();
        }
        catch (IOException e){
            System.out.println("오답 노트를 초기화하는 동안 오류가 발생했습니다 : "+e.getMessage());
        }
    }

    /**
     * 오답 노트 파일을 파싱하여 메모리 목록을 생성합니다.
     */
    private Vector<IncorrectWord> parseIncorrectWords(){
        Vector<IncorrectWord> incorrectWords = new Vector<>();
        try(Scanner sc = new Scanner(new File(incorrectFilePath))){
            int number = 0;
            while(sc.hasNextLine()){
                number++;
                String line = sc.nextLine();
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] engKorExType = line.split("\t",-1);
                if(engKorExType.length < 2){
                    System.out.println(number+"번째 오답 기록의 형식이 잘못되었습니다.");
                    continue;
                }
                String eng = engKorExType[0].trim();
                String korRaw = engKorExType[1];
                String ex = "";
                if(engKorExType.length >= 3){
                    ex = engKorExType[2].trim();
                }
                String quizType = "";
                if(engKorExType.length >= 4){
                    quizType = engKorExType[3].trim();
                }
                incorrectWords.add(buildIncorrectWord(eng, korRaw, ex, quizType));
            }
        }
        catch (FileNotFoundException e){
            System.out.println("오답 노트 파일을 불러오는 동안 오류가 발생했습니다 : "+e.getMessage());
        }
        return incorrectWords;
    }

    /**
     * 파일에서 읽어온 오답들을 내부 목록에 재적재합니다. (중복은 noteKeys로 필터)
     */
    private void rebuildNotes(Vector<IncorrectWord> incorrectWords){
        notes.clear();
        noteKeys.clear();
        for(IncorrectWord word : incorrectWords){
            String eng = safeString(word.getEng());
            String kor = safeString(String.join(", ", word.getKor()));
            String ex = safeString(word.getEx());
            String quizType = safeString(word.getQuizType());
            String key = buildKey(eng, kor, ex, quizType);
            if(noteKeys.add(key)){
                notes.add(buildIncorrectWord(eng, kor, ex, quizType));
            }
        }
    }

    /**
     * 현재 목록을 파일로 다시 씁니다.
     */
    private void rewriteIncorrectFile(){
        ensureFile();
        try(PrintWriter writer = new PrintWriter(new FileWriter(incorrectFilePath))){
            for(IncorrectWord incorrectWord : notes){
                String eng = safeString(incorrectWord.getEng());
                String kor = safeString(String.join(", ", incorrectWord.getKor()));
                String ex = safeString(incorrectWord.getEx());
                String quizType = safeString(incorrectWord.getQuizType());
                writer.println(formatLine(eng, kor, ex, quizType));
            }
        }
        catch (IOException e){
            System.out.println("오답 노트 파일을 저장하는 동안 오류가 발생했습니다 : "+e.getMessage());
        }
    }

    /**
     * 현 시점의 오답 목록 스냅샷을 반환합니다.
     */
    public Vector<IncorrectWord> getIncorrectWordsSnapshot(){
        initializeIncorrectNotes();
        return new Vector<>(notes);
    }

    /**
     * GUI용 오답 노트를 모두 삭제합니다.
     */
    public void clearAll(){
        ensureFile();
        try(PrintWriter writer = new PrintWriter(new FileWriter(incorrectFilePath))){
            writer.print("");
            notes.clear();
            noteKeys.clear();
        }
        catch (IOException e){
            System.out.println("오답 노트를 초기화하는 동안 오류가 발생했습니다 : "+e.getMessage());
        }
    }

    /**
     * 기록된 오답 노트를 콘솔에 출력합니다.
     */
    private void printIncorrectNotes(Scanner scanner){
        initializeIncorrectNotes();
        if(notes.isEmpty()){
            System.out.println("기록된 오답이 없습니다.");
            waitConsole(scanner, "엔터를 누르면 이전 메뉴로 돌아갑니다...");
            return;
        }

        System.out.println("===== 기록된 오답 =====");
        int index = 1;
        for(IncorrectWord incorrectWord : notes){
            String eng = safeString(incorrectWord.getEng());
            String kor = safeString(String.join(", ", incorrectWord.getKor()));
            String ex = safeString(incorrectWord.getEx());
            String quizType = safeString(incorrectWord.getQuizType());
            System.out.println(index+". "+eng);
            System.out.println("   뜻 : "+kor);
            if(!ex.isEmpty()){
                System.out.println("   예문 : "+ex);
            }
            if(!quizType.isEmpty()){
                System.out.println("   유형 : "+quizType);
            }
            index++;
        }
        System.out.println("=====================");
        waitConsole(scanner, "엔터를 누르면 이전 메뉴로 돌아갑니다...");
    }

    /**
     * 주어진 정보를 기반으로 IncorrectWord 객체를 생성합니다.
     */
    private IncorrectWord buildIncorrectWord(String eng, String kor, String ex, String quizType){
        String[] korArray = parseKor(kor);
        if(ex != null && !ex.isEmpty()){
            return new IncorrectWord(eng, korArray, ex, quizType);
        }
        return new IncorrectWord(eng, korArray, quizType);
    }

    /**
     * 쉼표로 구분된 한국어 뜻을 배열로 변환합니다.
     */
    private String[] parseKor(String korRaw){
        if(korRaw == null || korRaw.trim().isEmpty()){
            return new String[]{""};
        }
        String[] kor = korRaw.split(",");
        for(int i=0;i<kor.length;i++){
            kor[i] = kor[i].trim();
        }
        return kor;
    }

    /**
     * 단어/예문/유형을 조합해 고유 키를 만듭니다.
     */
    private String buildKey(Word word, String quizTypeOverride){
        String eng = safeString(word.getEng());
        String kor = safeString(String.join(", ", word.getKor()));
        String ex = safeString(word.getEx());
        String quizType = safeString(quizTypeOverride);
        return buildKey(eng, kor, ex, quizType);
    }

    private String buildKey(String eng, String kor, String ex, String quizType){
        String normalizedEng = safeString(eng).toLowerCase();
        String normalizedKor = safeString(kor).toLowerCase();
        String normalizedEx = safeString(ex).toLowerCase();
        String normalizedType = safeString(quizType).toLowerCase();
        return normalizedEng + "|" + normalizedKor + "|" + normalizedEx + "|" + normalizedType;
    }

    /**
     * 파일에 저장할 오답 노트 한 줄을 탭으로 구분해 만듭니다.
     */
    private String formatLine(String eng, String kor, String ex, String quizType){
        return safeString(eng) + "\t" + safeString(kor) + "\t" + safeString(ex) + "\t" + safeString(quizType);
    }

    /**
     * 오답 노트 파일을 준비합니다. 없으면 생성하고, 상위 폴더도 만듭니다.
     */
    private void ensureFile(){
        try{
            File file = new File(incorrectFilePath);
            File parent = file.getParentFile();
            if(parent != null && !parent.exists()){
                parent.mkdirs();
            }
            if(!file.exists()){
                file.createNewFile();
            }
        }
        catch (IOException e){
            System.out.println("오답 노트 파일을 준비하는 동안 오류가 발생했습니다 : "+e.getMessage());
        }
    }

    /**
     * 널/공백 안전한 문자열을 반환합니다.
     */
    private static String safeString(String value){
        if(value == null){
            return "";
        }
        return value;
    }

    public String getIncorrectFilePath() {
        return incorrectFilePath;
    }
}

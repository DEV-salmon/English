package voca.management;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import voca.core.IncorrectWord;
import voca.core.UserSession;
import voca.core.Word;

public class IncorrectManagement {
    private final String incorrectFilePath;
    private final Vector<IncorrectWord> cachedIncorrectWords = new Vector<>();
    private final Set<String> incorrectWordKeys = new HashSet<>();
    private boolean cacheInitialized = false;

    public IncorrectManagement(UserSession session){
        this.incorrectFilePath = session.getIncorrectFilePath();
        ensureFile();
    }

    public void menu(Scanner scanner, QuizManagement quizManagement){
        initializeIncorrectNotes();
        cleanConsole();
        while(true){
            System.out.println("===== 오답 메뉴 =====");
            System.out.println("1. 오답 퀴즈");
            System.out.println("2. 오답 초기화");
            System.out.println("3. 되돌아가기");
            System.out.print("-> ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice){
                case 1 -> quizManagement.runIncorrectQuiz(getIncorrectWordsSnapshot());
                case 2 -> clearIncorrect(scanner);
                case 3 -> {
                    System.out.println("이전 메뉴로 돌아갑니다.");
                    return;
                }
                default -> System.out.println("다시 입력해주세요");
            }
            cleanConsole();
        }
    }

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
        if(incorrectWordKeys.contains(key)){
            return;
        }
        try(PrintWriter writer = new PrintWriter(new FileWriter(incorrectFilePath, true))){
            writer.println(formatLine(eng, kor, ex, type));
            incorrectWordKeys.add(key);
            cachedIncorrectWords.add(buildIncorrectWord(eng, kor, ex, type));
        }
        catch (IOException e){
            System.out.println("오답 노트를 기록하는 동안 오류가 발생했습니다 : "+e.getMessage());
        }
    }

    public void markCorrect(IncorrectWord word){
        if(word == null){
            return;
        }
        initializeIncorrectNotes();
        String key = buildKey(word, word.getQuizType());
        if(!incorrectWordKeys.remove(key)){
            return;
        }
        cachedIncorrectWords.removeIf(existing -> buildKey(existing, existing.getQuizType()).equals(key));
        rewriteIncorrectFile();
    }

    public void initializeIncorrectNotes(){
        ensureFile();
        if(cacheInitialized){
            return;
        }
        Vector<IncorrectWord> parsed = parseIncorrectWords();
        rebuildCache(parsed);
        rewriteIncorrectFile();
        cacheInitialized = true;
    }

    private void clearIncorrect(Scanner scanner){
        ensureFile();
        try(PrintWriter writer = new PrintWriter(new FileWriter(incorrectFilePath))){
            writer.print("");
            System.out.println("오답 노트를 초기화했습니다.");
            System.out.print("엔터를 누르면 이전 메뉴로 돌아갑니다...");
            scanner.nextLine();
            cachedIncorrectWords.clear();
            incorrectWordKeys.clear();
            cacheInitialized = true;
        }
        catch (IOException e){
            System.out.println("오답 노트를 초기화하는 동안 오류가 발생했습니다 : "+e.getMessage());
        }
    }

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

    private void rebuildCache(Vector<IncorrectWord> incorrectWords){
        cachedIncorrectWords.clear();
        incorrectWordKeys.clear();
        for(IncorrectWord word : incorrectWords){
            String eng = safeString(word.getEng());
            String kor = safeString(String.join(", ", word.getKor()));
            String ex = safeString(word.getEx());
            String quizType = safeString(word.getQuizType());
            String key = buildKey(eng, kor, ex, quizType);
            if(incorrectWordKeys.add(key)){
                cachedIncorrectWords.add(buildIncorrectWord(eng, kor, ex, quizType));
            }
        }
    }

    private void rewriteIncorrectFile(){
        ensureFile();
        try(PrintWriter writer = new PrintWriter(new FileWriter(incorrectFilePath))){
            for(IncorrectWord incorrectWord : cachedIncorrectWords){
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

    private Vector<IncorrectWord> getIncorrectWordsSnapshot(){
        initializeIncorrectNotes();
        return new Vector<>(cachedIncorrectWords);
    }

    private IncorrectWord buildIncorrectWord(String eng, String kor, String ex, String quizType){
        String[] korArray = parseKor(kor);
        if(ex != null && !ex.isEmpty()){
            return new IncorrectWord(eng, korArray, ex, quizType);
        }
        return new IncorrectWord(eng, korArray, quizType);
    }

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

    private String buildKey(Word word, String quizTypeOverride){
        String eng = safeString(word.getEng());
        String kor = safeString(String.join(", ", word.getKor()));
        String ex = safeString(word.getEx());
        String quizType = safeString(quizTypeOverride);
        return buildKey(eng, kor, ex, quizType);
    }

    private String buildKey(String eng, String kor, String ex, String quizType){
        return safeString(eng).toLowerCase()+"|"+safeString(kor).toLowerCase()+"|"+
                safeString(ex).toLowerCase()+"|"+safeString(quizType).toLowerCase();
    }

    private String formatLine(String eng, String kor, String ex, String quizType){
        return safeString(eng) + "\t" + safeString(kor) + "\t" + safeString(ex) + "\t" + safeString(quizType);
    }

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

    private static String safeString(String value){
        if(value == null){
            return "";
        }
        return value;
    }
    private static void cleanConsole(){
        System.out.println("\u001B[2J");
    }
}

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

public class IncorrectManagement {
    private static final String INCORRECT_FILE_PATH = "Voca/src/res/Incorrect.txt";

    public static void menu(Scanner scanner){
        ensureFile();
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
                case 1 ->{
                    Vector<IncorrectWord> incorrectWords = loadIncorrectWords();
                    QuizManagement.runIncorrectQuiz(incorrectWords, scanner);
                }
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

    public static void recordIncorrect(Word word, String quizType){
        if(word == null){
            return;
        }
        String type = quizType != null ? quizType : "";
        ensureFile();
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(INCORRECT_FILE_PATH, true)))){
            String eng = safeString(word.getEng());
            String kor = safeString(String.join(", ", word.getKor()));
            String ex = safeString(word.getEx());
            writer.println(eng + "\t" + kor + "\t" + ex + "\t" + type);
        }
        catch (IOException e){
            System.out.println("오답 노트를 기록하는 동안 오류가 발생했습니다 : "+e.getMessage());
        }
    }

    public static Vector<IncorrectWord> loadIncorrectWords(){
        ensureFile();
        Vector<IncorrectWord> incorrectWords = new Vector<>();
        try(Scanner sc = new Scanner(new File(INCORRECT_FILE_PATH))){
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
                String[] kor = engKorExType[1].split(",");
                for(int i=0;i<kor.length;i++){
                    kor[i] = kor[i].trim();
                }
                String ex = "";
                if(engKorExType.length >= 3){
                    ex = engKorExType[2].trim();
                }
                String quizType = "";
                if(engKorExType.length >= 4){
                    quizType = engKorExType[3].trim();
                }
                IncorrectWord incorrectWord;
                if(!ex.isEmpty()){
                    incorrectWord = new IncorrectWord(eng, kor, ex, quizType);
                }
                else{
                    incorrectWord = new IncorrectWord(eng, kor, quizType);
                }
                incorrectWords.add(incorrectWord);
            }
        }
        catch (FileNotFoundException e){
            System.out.println("오답 노트 파일을 불러오는 동안 오류가 발생했습니다 : "+e.getMessage());
        }
        return incorrectWords;
    }

    private static void clearIncorrect(Scanner scanner){
        ensureFile();
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(INCORRECT_FILE_PATH)))){
            writer.print("");
            System.out.println("오답 노트를 초기화했습니다.");
            System.out.print("엔터를 누르면 이전 메뉴로 돌아갑니다...");
            scanner.nextLine();
        }
        catch (IOException e){
            System.out.println("오답 노트를 초기화하는 동안 오류가 발생했습니다 : "+e.getMessage());
        }
    }

    private static void ensureFile(){
        try{
            File file = new File(INCORRECT_FILE_PATH);
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

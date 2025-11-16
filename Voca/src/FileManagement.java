import java.io.*; // 파일 입출력 관련 클래스 import
import java.util.Scanner;
import java.util.Vector;

public class FileManagement {

    public static Vector<Word> makeVoca(String fileName){
        Vector<Word> v = new Vector<>();
        try(Scanner sc = new Scanner(new File(fileName))){
            int number = 0;

            while(sc.hasNextLine()){
                number++;
                String eng;
                String[] kor;
                String ex;
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

    public static void saveVoca(Vector<Word> voca,String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Word word : voca) {
                String engString = word.getEng();
                String korString = String.join("/ ", word.getKor());
                String exString = (word.getEx() != null) ? word.getEx() : "";

                String line = engString + "\t" + korString + "\t" + exString;
                writer.println(line);
            }
            System.out.println("파일을 저장했습니다.");
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }
}

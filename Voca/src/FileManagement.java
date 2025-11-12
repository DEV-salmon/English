import java.io.*; // 파일 입출력 관련 클래스 import
import java.util.Vector;

public class FileManagement {


    public static void save_Voca(Vector<Word> voca,String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Word word : voca) {
                String engString = String.join("/ ", word.getKor());
                String korString = String.join("/ ", word.getKor());
                String exString = (word.getEx() != null) ? word.getEx() : "";

                String line = word.getEng() + "\t" + korString + "\t" + exString;
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }
}

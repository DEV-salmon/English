import java.io.*; // 파일 입출력 관련 클래스 import
import java.util.Scanner;
import java.util.Vector;

public class FileManagement {

    /**
     * 단어장 벡터 객체를 만드는 메소드입니다.
     * @param fileName 단어장을 가져올 파일 이름입니다.
     * @return 만든 단어장 벡터 객체를 반환합니다.
     */
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
                kor = engKorEx[1].split(",");
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
                    System.out.println();
                }
            }
        }
        catch(FileNotFoundException e){
            System.out.println("파일을 찾을 수 없습니다.");
            System.out.println();
        }
        if(v.isEmpty()){
            System.out.println("보카가 비어있습니다 다시 한 번 확인해주세요");
            System.out.println();
        }
        return v;
    }

    /**
     * 로그인 정보 벡터 객체를 만드는 메소드입니다.
     * @param fileName 로그인 정보를 가져올 파일 이름입니다.
     * @return 만든 로그인 정보 벡터 객체를 반환합니다.
     */
    public static Vector<Login> makeLogin(String fileName){
        Vector<Login> l = new Vector<>();
        try(Scanner sc = new Scanner(new File(fileName))){

            while(sc.hasNextLine()){
                String userid;
                String salt;
                String hspw;
                String line=sc.nextLine();
                String[] idsalthspw = line.split("\t");
                userid = idsalthspw[0].trim();
                salt = idsalthspw[1].trim();
                hspw = idsalthspw[2].trim();
                l.add(new Login(userid, salt, hspw));
            }
        }
        catch(FileNotFoundException e){
            System.out.println("파일을 찾을 수 없습니다.");
            System.out.println();
        }
        if(l.isEmpty()){
            System.out.println("로그인 정보가 비어있습니다 다시 한 번 확인해주세요");
            System.out.println();
        }
        return l;
    }

    /**
     * 단어장 벡터 객체를 파일에 저장하는 메소드입니다.
     * @param voca  단어장에 저장된 벡터 객체입니다.
     * @param filename 단어장을 저장할 파일 이름입니다.
     */
    public static void saveVoca(Vector<Word> voca,String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            if(voca.isEmpty()){
                Vector<Word> vocaBase = makeVoca("Voca/src/res/voca");
                voca.addAll(vocaBase);
            }else{
                for (Word word : voca) {
                    String engString = word.getEng();
                    String korString = String.join(", ", word.getKor());
                    String exString = (word.getEx() != null) ? word.getEx() : "";

                    String line = engString + "\t" + korString + "\t" + exString;
                    writer.println(line);
                }
                System.out.println("파일을 저장했습니다.\n");
            }
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류 발생: " + e.getMessage()+"\n");
        }
    }

    /**
     * 로그인 정보 벡터 객체를 저장하는 메소드입니다.
     * @param loginList 로그인 정보가 저장되어있는 벡터 객체입니다.
     * @param filename  로그인 정보를 저장할 파일 이름입니다.
     */
    public static void saveLogin(Vector<Login> loginList,String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Login login1 : loginList) {
                String useridString = login1.getUserid();
                String saltString = login1.getSalt();
                String hspwString = login1.getHashedpassword();

                String line = useridString + "\t" + saltString  + "\t" + hspwString;
                writer.println(line);
            }
            System.out.println("파일을 저장했습니다.\n");
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류 발생: " + e.getMessage()+"\n");
        }
    }
}

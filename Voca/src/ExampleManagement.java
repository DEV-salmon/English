import java.util.Scanner;
import java.util.Vector;

public class ExampleManagement {

    /**
     * 단어장의 에문을 추가하는 메소드입니다.
     * 사용자로부터 예문을 적고싶은 단어를 for문을 통해 찾습니다.
     * 찾는 단어가 존재한다면 예문을 적습니다. set을 이용하여 적은 예문을 해당 Word객체에 저장합니다.
     * 찾는 단어가 존재하지 않는다면 존재하지 않는다는 안내와 함께 종료 됩니다.
     * @param scanner
     * @param voca
     */
    public static void ex_put(Scanner scanner, Vector<Word> voca){
        System.out.println("예문을 적으려는 단어를 적어주십시오");
        System.out.print("단어 : ");
        String vWord = scanner.next();
        scanner.nextLine();

        boolean isfound = false;

        for (Word str : voca) {
            if (str.getEng().equals(vWord)) {
                System.out.println("찾은 단어 " + str);
                System.out.print("예문을 적어 주십시오 : ");
                String vEx = scanner.nextLine();
                str.setEx(vEx);

                System.out.println("예문 저장됨: " + str);
                System.out.println();

                isfound = true;
                break;
            }
        }
        if (!isfound) {
            System.out.println("찾는 단어가 단어장에 존재하지 않습니다.");
            System.out.println();
        }
    }
}

package Test;

import java.util.Vector;

import voca.core.Word;

public class ExampleVector {
    public Vector<Word> voca = new Vector<>();

    // 예제 단어 데이터를 채우는 생성자
    public ExampleVector() {
        voca.add(new Word("Apple", new String[]{"사과"}));
        voca.add(new Word("Banana", new String[]{"바나나"}));
        voca.add(new Word("PineApple", new String[]{"파인애플"}));
        voca.add(new Word("example", new String[]{"예시 뜻1", "예시 뜻2"}));
    }
}

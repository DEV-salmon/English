package voca.core;
/**
 * 사용자의 학습효과를 극대화하기 위해 어떤 문제에서 틀렸는지도 필드로 담는 클래스입니다
 */ 
public class IncorrectWord extends Word {
    private String quizType;

    /**
     * 생성자입니다
     * word를 상속하기 때문에 word에 있는 두개의 필드를 받고, quizType이라는 새로운 필드를 받습니다
     * @param eng : 영어
     * @param kor : 한국어뜻
     * @param quizType : 퀴즈타입은 총 네가지로 해당 퀴즈타입은 QuizManagement에 구체적인 정의가 되어있습니다.
     */
    public IncorrectWord(String eng, String[] kor, String quizType){
        super(eng, kor);
        this.quizType = quizType;
    }

    public IncorrectWord(String eng, String[] kor, String ex, String quizType){
        super(eng, kor, ex);
        this.quizType = quizType;
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }
}

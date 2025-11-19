public class IncorrectWord extends Word {
    private String quizType;

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

public class Word {
    private String eng;
    private String[] kor;
    private String ex;

    public Word(String eng, String[] kor){
        this.eng = eng;
        this.kor = kor;
    }
    public Word(String eng, String[] kor, String ex){
        this(eng, kor);
        this.ex = ex;
    }
    public String getEx() {
        return ex;
    }
    public String getEng() {
        return eng;
    }

    public String[] getKor() {
        return kor;
    }
    public void setEx(String ex) {
        this.ex = ex;
    }
    public void setEng(String eng) {
        this.eng = eng;
    }
    public void setKor(String[] kor) {
        this.kor = kor;
    }

}

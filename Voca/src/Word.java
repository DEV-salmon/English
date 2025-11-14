package teamProject;

public class Word {
    private String eng;
    private String kor;
    private String ex;

    public Word(String eng, String kor, String ex) {
        this.eng = eng;
        this.kor = kor;
        this.ex = ex;
    }

    public String getEng() {
        return eng;
    }

    public String getKor() {
        return kor;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public void setKor(String kor) {
        this.kor = kor;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

    @Override
    public String toString() {
        return this.eng + " : " + kor + "   ex. " + ex;
    }
}

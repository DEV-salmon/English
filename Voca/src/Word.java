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
        return kor = new String[]{String.join(", ", kor)};
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


    @Override
    public String toString() {

        String eng_kor_ex_String = eng + " : " + String.join(", ", kor) + "\n" + "예문 : "+ex;
        String eng_kor_String = eng + " : " + String.join(", ", kor)+ " ";


        return ((ex != null && !ex.isEmpty()) ? eng_kor_ex_String : eng_kor_String) + "\n"+"-".repeat(20);
    }
}

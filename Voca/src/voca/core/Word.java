//package voca.core;
//
//import voca.tts.TTS;
//
///**
// * 단어를 정의한 클래스입니다
// */
//public class Word {
//    private String eng;
//    private String[] kor;
//    private String ex="";
//
//    /**
//     * 생성자입니다
//     * 영어, 한국어뜻을 받습니다. 이때, 예문은 선택적으로 받습니다.
//     * @param eng
//     * @param kor
//     */
//    public Word(String eng, String[] kor){
//        this.eng = eng;
//        this.kor = kor;
//    }
//    public Word(String eng, String[] kor, String ex){
//        this(eng, kor);
//        this.ex = ex;
//    }
//    public String getEx() {
//        if (ex.equals("")){
//            return null;
//        }
//        return ex;
//    }
//    public String getEng() {
//        return eng;
//    }
//    public String[] getKor() {
//        if(kor == null){
//            return new String[0];
//        }
//        return kor.clone();
//    }
//
//    public void setEx(String ex) {
//        this.ex = ex;
//    }
//    public void setEng(String eng) {
//        this.eng = eng;
//    }
//    public void setKor(String[] kor) {
//        this.kor = kor;
//    }
//
//    // 영어 단어 음성 출력
//    public void voiceEng(){
//        TTSGUI TTSGUI = new TTSGUI();
//        TTSGUI.tts(eng);
//    }
//
//    // 영어 예문 음성 출력
//    public void voiceEx(){
//        TTSGUI TTSGUI = new TTSGUI();
//        TTSGUI.tts((ex));
//    }
//
//    @Override
//    public String toString() {
//
//        String eng_kor_ex_String = eng + " : " + String.join(", ", kor) + "\n" + "예문 : "+ex;
//        String eng_kor_String = eng + " : " + String.join(", ", kor)+ " ";
//
//
//        return ((ex != null && !ex.isEmpty()) ? eng_kor_ex_String : eng_kor_String) + "\n"+"-".repeat(20);
//    }
//
//
//    /**
//     * @return 단어의 예문 구현여부를 반환합니다.
//     */
//    public boolean haveEx(){
//        if (getEx()==null){
//            return false;
//        }
//        return true;
//    }
//}

package voca.core;

import voca.tts.TTS;

public class Word {
    private String eng;
    private String[] kor;
    private String ex = "";

    // 영어 단어와 뜻을 설정하는 생성자
    public Word(String eng, String[] kor){
        this.eng = eng;
        this.kor = kor;
    }

    // 예문까지 함께 설정하는 생성자
    public Word(String eng, String[] kor, String ex){
        this(eng, kor);
        this.ex = ex;
    }

    // 예문을 반환
    public String getEx() {
        if (ex.equals("")){
            return null;
        }
        return ex;
    }

    // 영어 단어를 반환
    public String getEng() {
        return eng;
    }

    // 한국어 뜻 배열을 반환
    public String[] getKor() {
        if(kor == null){
            return new String[0];
        }
        return kor.clone();
    }

    // 예문을 설정
    public void setEx(String ex) {
        this.ex = ex;
    }

    // 영어 단어를 설정
    public void setEng(String eng) {
        this.eng = eng;
    }

    // 한국어 뜻 배열을 설정
    public void setKor(String[] kor) {
        this.kor = kor;
    }

    // 영어 단어를 음성으로 출력
    public void voiceEng(){
        TTS tts = new TTS();
        tts.tts(eng);
    }
    
    // 예문을 음성으로 출력
    public void voiceEx(){
        TTS tts = new TTS();
        tts.tts((ex));
    }

    // 단어와 뜻을 문자열로 변환
    @Override
    public String toString() {
        String eng_kor_String = eng + " : " + String.join(", ", kor)+ "";
        return eng_kor_String;
    }

    // 예문 존재 여부를 반환
    public boolean haveEx(){
        if (getEx()==null){
            return false;
        }
        return true;
    }
}

package voca.tts;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TTSGUI {
    private Voice voice;

    // FreeTTS 엔진을 초기화하는 생성자
    public TTSGUI() {
        String voiceName = "kevin16"; 

        try {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

            VoiceManager vm = VoiceManager.getInstance();

            this.voice = vm.getVoice(voiceName);

            if (this.voice == null) {
                System.err.println("목소리를 찾을 수 없습니다: " + voiceName);
                System.err.println("사용 가능한 목소리 목록:");
                for (Voice v : vm.getVoices()) {
                    System.err.println("  - " + v.getName());
                }
                return;
            }
            this.voice.allocate();

        } catch (Exception e) {
            System.err.println("TTS 초기화 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // 전달된 텍스트를 음성으로 출력
    public void tts(String text) {
        if (this.voice == null) {
            System.err.println("TTS가 제대로 초기화되지 않았습니다. (목소리를 찾을 수 없음)");
            return;
        }

        try {
            this.voice.speak(text);
        } catch (Exception e) {
            System.out.println("말하는 도중 오류 발생");
        }
    }

    // 할당된 음성 리소스를 해제
    public void shutdown() {
        if (this.voice != null) {
            try {
                this.voice.deallocate();
            } catch (Exception e) {
                System.out.println("리소스 해제중 오류 발생");
            }
        }
    }
}

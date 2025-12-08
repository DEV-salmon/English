package GUI.File;

import GUI.Main.GlobalSignal;
import Signal.Controller;
import Signal.Signal;
import voca.core.UserFileInfo;
import voca.core.Word;
import voca.management.FileManagement;

import javax.swing.*;
import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class FileController implements Controller {
    private final FileUI fileUI;
    private final Vector<Word> vocabulary; // 현재 단어장 목록
    private boolean menuVisible;
    private final Controller globalHandler;
    private UserFileInfo userFileInfo;

    public FileController(Vector<Word> vocabulary) {
        this(vocabulary, null);
    }

    public FileController(Vector<Word> vocabulary, Controller controller) {
        this.vocabulary = vocabulary;
        this.globalHandler = controller;
        this.fileUI = new FileUI((this::send));
        this.fileUI.setSideMenuVisible(menuVisible);
    }

    public FileUI getView() {
        return fileUI;
    }

    @Override
    public void send(Signal signal, Object data) {
        if (signal instanceof GlobalSignal globalSignal && globalHandler != null) {
            globalHandler.send(globalSignal, data);
            return;
        }
        if (!(signal instanceof FileSignal fileSignal)) {
            return;
        }

        switch (fileSignal) {
            case FILE_MERGE:
                handleFileMerge(data);
                break;
            case FILE_LOAD:
                handleFileLoad(data);
                break;
            case FILE_SAVE:
                handleFileSave(data);
                break;
            default:
                break;
        }
    }

    public void toggleMenu() {
        menuVisible = !menuVisible;
        fileUI.setSideMenuVisible(menuVisible);
    }

    // ====================================================================
    // 💡 CORE HANDLERS
    // ====================================================================

    private void handleFileMerge(Object data) {
        boolean mergeCompleted = false;
        String currentPath = "";

        while (!mergeCompleted) {
            Object[] InputData = fileUI.showFileMergeDialogue(currentPath);
            JTextField newFileField = (JTextField) InputData[0];
            JTextField newPolicyField = (JTextField) InputData[1];
            int result = (int)InputData[2];

            if(result == JOptionPane.NO_OPTION){
                File str = fileUI.showFileChooser();
                if (str != null) {
                    currentPath = str.getAbsolutePath();
                    newFileField.setText(str.getAbsolutePath());
                }
                continue;
            }

            // 2. 병합 실행 (OK_OPTION)
            if(result == JOptionPane.OK_OPTION){
                currentPath = newFileField.getText().trim(); // 최종 경로 업데이트
                String policy = newPolicyField.getText();
                executeMergeLogic(currentPath, policy);
                mergeCompleted = true;
            }

            // 3. 취소 (CANCEL_OPTION)
            if(result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION){
                // CANCEL이 눌렸으므로 루프를 종료합니다.
                mergeCompleted = true;
            }
        }
    }

    private void executeMergeLogic(String path, String policyStr) {
        String policy = policyStr;

        if(!validateVocaFormat(path)){
            fileUI.showMessage("파일 내용이 정확하지 않음");
            return;
        }

        // 이전에 구현했던 병합 로직을 그대로 여기에 배치합니다.
        String backupPath = createBackup();
        Vector<Word> loaded = FileManagement.makeVoca(path);

        if(loaded.isEmpty()){
            fileUI.showMessage("벡터가 비워있습니다.");
            return;
        }

        String intPolicy;
        try {
            intPolicy = policy;
        } catch (NumberFormatException e) {
            fileUI.showMessage("정책 번호는 숫자로 입력해야 합니다.");
            return;
        }

        int[] resulted = applyMergePolicy(loaded, intPolicy);

        System.out.println("합치기를 완료했습니다. 추가:" + resulted[0] + " 갱신:" + resulted[1] + " 건너뜀:" + resulted[2] + " / 총 단어 수 : " + vocabulary.size());
        FileManagement.saveVoca(vocabulary,userFileInfo.getVocaFilePath());
        globalHandler.send(GlobalSignal.UPDATE_VOCA,vocabulary);

        if(!backupPath.isEmpty()){
            System.out.println("문제가 생기면 백업 파일로 복원하세요 : " + backupPath);
        }
        // UI 업데이트 시그널 전송 (globalHandler.send(GlobalSignal.UPDATE_VOCA, vocabulary);)도 추가해야 합니다.
    }


    private void handleFileLoad(Object data) {

    }

    private void handleFileSave(Object data) {

    }

    // ====================================================================
    // 💡 FILE MANAGEMENT UTILITIES (이식된 FileMenu 로직)
    // ====================================================================

    /**
     * 단어장 파일 절대 경로를 검증
     */
    public String requestPath(String path){
        if(path.isEmpty()){
            fileUI.showMessage("파일경로가 없음");
            return null;
        }
        File file = new File(path);
        if(!file.isAbsolute()){
            fileUI.showMessage("절대 경로 X");
            return null;
        }
        if(!file.exists() || !file.isFile()){
            fileUI.showMessage("해당 경로에 파일이 없음");
            return null;
        }
        return path;
    }


    /**
     * 현재 단어장 파일을 백업합니다.
     */
    public String createBackup(){
        try{
            File source = new File(userFileInfo.getVocaFilePath());
            if(!source.exists()){
                return "";
            }
            String backupPath = userFileInfo.getVocaFilePath() + ".bak." + System.currentTimeMillis();
            copyFile(source, new File(backupPath));
            System.out.println("백업 생성: " + backupPath);
            return backupPath;
        } catch (IOException e){
            System.out.println("백업을 만드는 동안 오류가 발생했습니다: " + e.getMessage());
            return "";
        }
    }

    /**
     * 간단한 파일 복사 메서드입니다. (하드코딩된 버퍼 사용)
     */
    public void copyFile(File source, File target) throws IOException{
        try(FileInputStream fis = new FileInputStream(source);
            FileOutputStream fos = new FileOutputStream(target)){
            byte[] buffer = new byte[8192];
            int read;
            while((read = fis.read(buffer)) != -1){
                fos.write(buffer, 0, read);
            }
        }
    }

    /**
     * 병합 정책을 선택합니다.
     */
//    private int chooseMergePolicy(){
//        while(true){
//            System.out.println("중복 단어 처리 방법을 선택하세요.");
//            System.out.println("1) 중복은 건너뛰기 2) 새 파일 값으로 덮어쓰기 3) 뜻/예문을 합치기");
//            int choice = readInt(scanner, "번호를 입력하세요 : ");
//            if(choice >=1 && choice <=3){
//                return choice;
//            }
//            System.out.println("다시 입력해주세요.");
//        }
//    }

    /**
     * 병합 정책을 적용하고 결과를 반환합니다.
     */
    private int[] applyMergePolicy(Vector<Word> loaded, String policy){
        int added = 0;
        int updated = 0;
        int skipped = 0;
        Set<String> existing = new HashSet<>();
        for(Word word : vocabulary){
            existing.add(word.getEng().toLowerCase());
        }
        for(Word word : loaded){
            String key = word.getEng().toLowerCase();
            int idx = findWordIndex(key);
            if(idx == -1){
                vocabulary.add(word);
                existing.add(key);
                added++;
                continue;
            }
            if(policy == "1"){
                skipped++;
            }
            else if(policy == "2"){
                vocabulary.set(idx, word);
                updated++;
            }
            else if(policy == "3"){
                Word current = vocabulary.get(idx);
                mergeWordContent(current, word);
                updated++;
            }
        }
        return new int[]{added, updated, skipped};
    }

    /**
     * 단어장을 병합할 때 기존과 새 데이터를 합칩니다.
     */
    private void mergeWordContent(Word base, Word incoming){
        Set<String> korSet = new HashSet<>();
        for(String kor : base.getKor()){
            korSet.add(kor.trim());
        }
        for(String kor : incoming.getKor()){
            korSet.add(kor.trim());
        }
        base.setKor(korSet.toArray(new String[0]));
        if((base.getEx() == null || base.getEx().isEmpty()) && incoming.getEx() != null && !incoming.getEx().isEmpty()){
            base.setEx(incoming.getEx());
        }
    }

    /**
     * 주어진 영어 철자에 해당하는 단어 인덱스를 찾습니다.
     */
    private int findWordIndex(String engKey){
        for(int i=0;i<vocabulary.size();i++){
            if(vocabulary.get(i).getEng().equalsIgnoreCase(engKey)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 단어장 파일 형식을 간단히 검사합니다. (예문은 없어도 됩니다)
     */
    public boolean validateVocaFormat(String path){
        try(Scanner sc = new Scanner(new File(path))){
            int lineNo = 0;
            boolean hasError = false;
            while(sc.hasNextLine()){
                lineNo++;
                String line = sc.nextLine();
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] parts = line.split("\\t+| {2,}", -1);
                if(parts.length < 2){
                    parts = line.split("\t",-1);
                }
                if(parts.length < 2 || parts.length > 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()){
                    System.out.println(lineNo + "번째 줄 형식이 잘못되었습니다. (영어<TAB>한글<TAB>예문)");
                    hasError = true;
                }
            }
            if(hasError){
                fileUI.showMessage("형식 오류");
                return false;
            }
            return true;
        } catch (Exception e){
            fileUI.showMessage("파일을 찾을 수 없다.");
            return false;
        }
    }

    public void updateUserInfo(UserFileInfo newUserFileInfo) {
        this.userFileInfo = newUserFileInfo;
    }

    public UserFileInfo getUserFileInfo() {
        return userFileInfo = userFileInfo;
    }
}


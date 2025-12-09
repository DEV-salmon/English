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
                handleFileMerge();
                break;
            case FILE_LOAD:
                handleFileLoad();
                break;
            case FILE_SAVE:
                fileUI.showMessage("파일은 자동으로 저장됩니다.");
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

    private void handleFileMerge() {
        boolean mergeCompleted = false;
        String currentPath = "";

        while (!mergeCompleted) {
            FileUI.FileMergeDialogResult result = fileUI.showFileMergeDialogue(currentPath);
            if(result.getResult() != JOptionPane.OK_OPTION){
                break;
            }

            currentPath = result.getPath().trim(); // 최종 경로 업데이트
            if (currentPath.isEmpty()) {
                fileUI.showMessage("파일 경로를 선택하세요.");
                continue;
            }

            executeMergeLogic(currentPath);
            mergeCompleted = true;
        }
    }

    private void executeMergeLogic(String path) {
        if(!validateVocaFormat(path)){
            fileUI.showMessage("파일 내용이 정확하지 않음");
            return;
        }

        String backupPath = createBackup();
        Vector<Word> loaded = FileManagement.makeVoca(path);

        if(loaded.isEmpty()){
            fileUI.showMessage("벡터가 비워있습니다.");
            return;
        }

        int[] resulted = applyMergePolicy(loaded);

        System.out.println("합치기를 완료했습니다. 추가:" + resulted[0] + " 갱신:" + resulted[1] + " 건너뜀:" + resulted[2] + " / 총 단어 수 : " + vocabulary.size());
        saveIfPossible();
        if(globalHandler != null){
            globalHandler.send(GlobalSignal.UPDATE_VOCA,vocabulary);
        }

        if(!backupPath.isEmpty()){
            System.out.println("문제가 생기면 백업 파일로 복원하세요 : " + backupPath);
        }
    }


    private void handleFileLoad() {
        File selectedFile = fileUI.showFileChooser();
        if (selectedFile == null) {
            return;
        }
        String path = selectedFile.getAbsolutePath();

        if(!validateVocaFormat(path)){
            return;
        }
        String backupPath = createBackup();
        Vector<Word> loaded = FileManagement.makeVoca(path);
        if(loaded.isEmpty()){
            fileUI.showMessage("불러올 단어가 없습니다.");
            return;
        }

        vocabulary.clear();
        vocabulary.addAll(loaded);
        saveIfPossible();
        if(globalHandler != null){
            globalHandler.send(GlobalSignal.UPDATE_VOCA, vocabulary);
        }
        if(!backupPath.isEmpty()){
            System.out.println("문제가 생기면 백업 파일로 복원하세요 : " + backupPath);
        }
        fileUI.showMessage("파일을 불러왔습니다.");
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
        if (userFileInfo == null) {
            return "";
        }
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
    private int[] applyMergePolicy(Vector<Word> loaded){
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
            skipped++;
        }
        return new int[]{added, updated, skipped};
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

    private void saveIfPossible() {
        if (userFileInfo == null) {
            System.err.println("경고: 저장 경로가 없어 변경사항을 파일에 반영하지 못했습니다.");
            fileUI.showMessage("저장 경로가 없어 파일로 저장하지 못했습니다.");
            return;
        }
        FileManagement.saveVoca(vocabulary, userFileInfo.getVocaFilePath());
    }

    public void updateUserInfo(UserFileInfo newUserFileInfo) {
        this.userFileInfo = newUserFileInfo;
    }

    public UserFileInfo getUserFileInfo() {
        return userFileInfo;
    }
}

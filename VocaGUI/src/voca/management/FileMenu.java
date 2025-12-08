package voca.management;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import voca.core.UserFileInfo;
import voca.core.Word;

public class FileMenu extends BaseMenu{

    private final Scanner scanner;
    private final Vector<Word> voca;
    private final String userFilePath;

    public FileMenu(UserFileInfo userFileInfo) {
        this.scanner = null;
        this.voca = new Vector<>();
        this.userFilePath = userFileInfo.getVocaFilePath();
    }

    /**
     * 파일 메뉴를 초기화합니다.
     */
    public FileMenu(Scanner scanner, Vector<Word> voca, String userFilePath){
        this.scanner = scanner;
        this.voca = voca;
        this.userFilePath = userFilePath;
    }

    /**
     * 파일 관련 메뉴를 표시하고 선택된 작업을 수행합니다.
     */
    public void menu(){
        int choice = 0;
        while(choice !=4){
            cleanConsole();
            System.out.println("1) 단어장 불러오기 2) 단어장 합치기 3) 현재 단어장 저장 4) 종료");
            choice = readInt(scanner, "메뉴 번호를 선택해주세요 : ");
            System.out.println();
            switch (choice){
                case 1 -> {
                    loadFromAbsolutePath();
                    waitConsole(scanner, "불러오기를 마쳤습니다. 엔터를 눌러 계속하세요...");
                }
                case 2 -> {
                    mergeFromAbsolutePath();
                    waitConsole(scanner, "합치기를 마쳤습니다. 엔터를 눌러 계속하세요...");
                }
                case 3 -> {
                    FileManagement.saveVoca(voca, userFilePath);
                    waitConsole(scanner);
                }
                case 4 -> {
                    System.out.println("파일 메뉴를 종료합니다.");
                    waitConsole(scanner);
                }
                default -> System.out.println("메뉴에 존재하지 않습니다. 다시 입력해주세요.");
            }
        }
    }

    /**
     * 입력받은 절대 경로의 단어장 파일을 현재 단어장으로 교체합니다.
     */
    private void loadFromAbsolutePath(){
        String path = requestPath();
        if(!validateVocaFormat(path)){
            return;
        }
        String backupPath = createBackup();
        Vector<Word> loaded = FileManagement.makeVoca(path);
        if(loaded.isEmpty()){
            System.out.println("불러온 단어장이 비어있거나 읽을 수 없습니다.");
            return;
        }
        voca.clear();
        voca.addAll(loaded);
        System.out.println("단어장을 새 파일로 교체했습니다. 총 단어 수 : " + voca.size());
        if(!backupPath.isEmpty()){
            System.out.println("문제가 생기면 백업 파일로 복원하세요 : " + backupPath);
        }
    }

    /**
     * 입력받은 절대 경로의 단어장 파일을 중복 없이 합칩니다.
     */
    private void mergeFromAbsolutePath(){
        String path = requestPath();
        if(!validateVocaFormat(path)){
            return;
        }
        int policy = chooseMergePolicy();
        String backupPath = createBackup();
        Vector<Word> loaded = FileManagement.makeVoca(path);
        if(loaded.isEmpty()){
            System.out.println("불러온 단어장이 비어있거나 읽을 수 없습니다.");
            return;
        }
        int[] result = applyMergePolicy(loaded, policy);
        System.out.println("합치기를 완료했습니다. 추가:" + result[0] + " 갱신:" + result[1] + " 건너뜀:" + result[2] + " / 총 단어 수 : " + voca.size());
        if(!backupPath.isEmpty()){
            System.out.println("문제가 생기면 백업 파일로 복원하세요 : " + backupPath);
        }
    }

    /**
     * 단어장 파일 절대 경로를 검증하며 입력받습니다.
     */
    public String requestPath(){
        while(true){
            System.out.print("절대 경로를 입력해주세요 : ");
            String path = scanner.nextLine().trim();
            if(path.isEmpty()){
                System.out.println("경로를 입력해주세요.");
                continue;
            }
            File file = new File(path);
            if(!file.isAbsolute()){
                System.out.println("절대 경로를 입력해주세요.");
                continue;
            }
            if(!file.exists() || !file.isFile()){
                System.out.println("해당 경로에 파일이 없습니다. 다시 입력해주세요.");
                continue;
            }
            return path;
        }
    }

    /**
     * 현재 단어장 파일을 백업합니다.
     */
    public String createBackup(){
        try{
            File source = new File(userFilePath);
            if(!source.exists()){
                return "";
            }
            String backupPath = userFilePath + ".bak." + System.currentTimeMillis();
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
    private int chooseMergePolicy(){
        while(true){
            System.out.println("중복 단어 처리 방법을 선택하세요.");
            System.out.println("1) 중복은 건너뛰기 2) 새 파일 값으로 덮어쓰기 3) 뜻/예문을 합치기");
            int choice = readInt(scanner, "번호를 입력하세요 : ");
            if(choice >=1 && choice <=3){
                return choice;
            }
            System.out.println("다시 입력해주세요.");
        }
    }

    /**
     * 병합 정책을 적용하고 결과를 반환합니다.
     */
    private int[] applyMergePolicy(Vector<Word> loaded, int policy){
        int added = 0;
        int updated = 0;
        int skipped = 0;
        Set<String> existing = new HashSet<>();
        for(Word word : voca){
            existing.add(word.getEng().toLowerCase());
        }
        for(Word word : loaded){
            String key = word.getEng().toLowerCase();
            int idx = findWordIndex(key);
            if(idx == -1){
                voca.add(word);
                existing.add(key);
                added++;
                continue;
            }
            if(policy == 1){
                skipped++;
            }
            else if(policy == 2){
                voca.set(idx, word);
                updated++;
            }
            else if(policy == 3){
                Word current = voca.get(idx);
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
        for(int i=0;i<voca.size();i++){
            if(voca.get(i).getEng().equalsIgnoreCase(engKey)){
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
                System.out.println("위와 같이 형식 오류가 있습니다. 백업 파일로 되돌릴 수 있습니다.");
                System.out.println("진행을 취소했습니다. 기존 파일 백업 후 다시 확인해주세요.");
                return false;
            }
            return true;
        } catch (Exception e){
            System.out.println("파일을 찾을 수 없습니다.");
            return false;
        }
    }
}

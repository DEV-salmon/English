import java.nio.file.Path;

public class UserFileInfo {
    private final String userId;
    private final Path userDirectory;
    private final Path vocaFile;
    private final Path incorrectFile;
    private final Path statFile;

    // 사용자 파일 경로 정보를 초기화
    public UserFileInfo(String userId, Path userDirectory, Path vocaFile, Path incorrectFile, Path statFile) {
        this.userId = userId;
        this.userDirectory = userDirectory;
        this.vocaFile = vocaFile;
        this.incorrectFile = incorrectFile;
        this.statFile = statFile;
    }

    // 사용자 ID를 반환
    public String getUserId() {
        return userId;
    }

    // 사용자 디렉터리 경로를 반환
    public Path getUserDirectory() {
        return userDirectory;
    }

    // 단어장 파일 경로를 반환
    public Path getVocaFile() {
        return vocaFile;
    }

    // 오답 노트 파일 경로를 반환
    public Path getIncorrectFile() {
        return incorrectFile;
    }

    // 통계 파일 경로를 반환
    public Path getStatFile() {
        return statFile;
    }
}

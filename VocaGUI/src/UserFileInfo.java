import java.nio.file.Path;

public class UserFileInfo {
    private final String userId;
    private final Path userDirectory;
    private final Path vocaFile;
    private final Path incorrectFile;
    private final Path statFile;

    public UserFileInfo(String userId, Path userDirectory, Path vocaFile, Path incorrectFile, Path statFile) {
        this.userId = userId;
        this.userDirectory = userDirectory;
        this.vocaFile = vocaFile;
        this.incorrectFile = incorrectFile;
        this.statFile = statFile;
    }

    public String getUserId() {
        return userId;
    }

    public Path getUserDirectory() {
        return userDirectory;
    }

    public Path getVocaFile() {
        return vocaFile;
    }

    public Path getIncorrectFile() {
        return incorrectFile;
    }

    public Path getStatFile() {
        return statFile;
    }
}

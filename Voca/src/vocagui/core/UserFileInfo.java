package vocagui.core;

public class UserFileInfo {
    private final String userId;
    private final String userDirectory;
    private final String vocaFile;
    private final String incorrectFile;
    private final String statFile;

    public UserFileInfo(String userId, String userDirectory, String vocaFile, String incorrectFile, String statFile) {
        this.userId = userId;
        this.userDirectory = userDirectory;
        this.vocaFile = vocaFile;
        this.incorrectFile = incorrectFile;
        this.statFile = statFile;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserDirectory() {
        return userDirectory;
    }

    public String getVocaFile() {
        return vocaFile;
    }

    public String getIncorrectFile() {
        return incorrectFile;
    }

    public String getStatFile() {
        return statFile;
    }
}

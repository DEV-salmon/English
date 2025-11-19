public class UserSession {
    private final String userId;
    private final String baseDirectory;
    private final String userDirectory;

    public UserSession(String userId, String baseDirectory) {
        this.userId = userId;
        this.baseDirectory = baseDirectory;
        this.userDirectory = baseDirectory + "/" + userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserDirectory() {
        return userDirectory;
    }

    public String getVocaFilePath() {
        return userDirectory + "/voca.txt";
    }

    public String getIncorrectFilePath() {
        return userDirectory + "/Incorrect.txt";
    }

    public String getStatFilePath(){
        return userDirectory+"/Stat.txt";
    }

    public String resolveInUserDirectory(String relativePath) {
        if(relativePath == null || relativePath.isEmpty()){
            return userDirectory;
        }
        if(relativePath.startsWith("/")){
            relativePath = relativePath.substring(1);
        }
        return userDirectory + "/" + relativePath;
    }
}

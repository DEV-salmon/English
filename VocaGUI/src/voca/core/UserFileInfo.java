package voca.core;

/**
 * 유저의 폴더 경로를 알기 위해 필요한 보조 클래스입니다
 * 
 */
public class UserFileInfo {
    private final String userId;
    private final String baseDirectory;
    private final String userDirectory;


    /**
     * 생성자입니다
     * @param userId : 유저의 아이디입니다
     * @param baseDirectory : 기본적인 경로입니다
     */
    public UserFileInfo(String userId, String baseDirectory) {
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

    public String getStatFilePath() {
        return userDirectory + "/stat.txt";
    }

    /**
     * 항상 절대경로를 사용하여 코딩할 수는 없기 때문에 userDirectory/추가경로 형식을 반환하는 매서드입니다
     * @param relativePath : userDirectory 뒤에 붙이고 싶은 파일 경로입니다
     * @return : userDirectory/추가경로 를 문자열로 반환합니다
     */
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

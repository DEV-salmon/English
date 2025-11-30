import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileManager {
    private final Path userBaseDir = Paths.get("src", "res", "Vocas");

    public UserFileInfo prepareUserSession(String userId){
        String trimmedId = userId.trim();
        Path userDir = userBaseDir.resolve(trimmedId);
        Path vocaFile = userDir.resolve("voca.txt");
        Path incorrectFile = userDir.resolve("Incorrect.txt");
        Path statFile = userDir.resolve("stat.txt");

        createDirectoryIfNeeded(userBaseDir);
        createDirectoryIfNeeded(userDir);
        ensureFileExists(vocaFile, defaultVocaSeed(), true);
        ensureFileExists(incorrectFile, "", false);
        ensureFileExists(statFile, "", false);

        return new UserFileInfo(trimmedId, userDir, vocaFile, incorrectFile, statFile);
    }

    private String defaultVocaSeed(){
        return """
# 기본 단어 샘플입니다. 필요한 단어를 추가하거나 수정하세요.
apple|사과|I eat an apple every morning.
banana|바나나|Bananas are yellow and sweet.
""";
    }

    private void ensureFileExists(Path filePath, String defaultContent, boolean withContent){
        try{
            if(Files.notExists(filePath)){
                createDirectoryIfNeeded(filePath.getParent());
                if(withContent){
                    Files.writeString(
                        filePath,
                        defaultContent,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE
                    );
                }else{
                    Files.createFile(filePath);
                }
            }
        }catch (IOException e){
            throw new IllegalStateException("파일을 준비하는 중 오류가 발생했습니다: " + filePath, e);
        }
    }

    private void createDirectoryIfNeeded(Path dir){
        if(dir == null){
            return;
        }
        try{
            if(Files.notExists(dir)){
                Files.createDirectories(dir);
            }
        }catch (IOException e){
            throw new IllegalStateException("폴더를 만들 수 없습니다: " + dir, e);
        }
    }
}

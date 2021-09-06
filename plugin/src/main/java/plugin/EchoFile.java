package plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Optional;
import org.gradle.api.Project;

public class EchoFile {

    private final Path echoedFile;

    public EchoFile(Project project, Path echoedFile) {
        this.echoedFile = createAbsolutePathToEchoedFile(project, echoedFile);
    }

    public void performAction() throws FileNotFoundException {
        File targetFile = locateTargetFile(echoedFile);
        String fileContents = new FileReader().readFile(targetFile.getAbsoluteFile().toPath());
        echoContents(fileContents);
    }

    private Path createAbsolutePathToEchoedFile(Project project, Path echoedFile) {
        return new File(project.getProjectDir(), echoedFile.toString()).getAbsoluteFile().toPath();
    }

    private File locateTargetFile(Path path) {
        return Optional.ofNullable(path)
            .map(pathString -> path.toFile())
            .orElseThrow();
    }

    private void echoContents(String fileContents) {
        System.out.println(fileContents);
    }
}

package plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Stream;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class FunctionalTest {

    public static final Random RANDOM = new Random();
    public static final Path SETTINGS_GRADLE_SAMPLE = Path.of("settings.gradle");
    public static final String SETTINGS_GRADLE = "settings.gradle";
    public static final String TEMP_FILE_PREFIX = "tempFile";
    public static final File CURRENT_FOLDER = new File("").getAbsoluteFile();
    public static final File ROOT_FOLDER = CURRENT_FOLDER.getAbsoluteFile().getParentFile();
    public static final String HARDCODED_FILENAME_TO_BE_ECHOED = "echo.me";
    public static final String HARDCODED_TESTRING_PROJECT_NAME = "testingProject";
    private static final String TASK_NAME = "echofiletask";
    private BuildResult result;
    private String expectedContent;

    @BeforeEach
    public void runProject() throws IOException {
        cleanTempFolders();
        File projectFolder = cloneTestingProjectInTemporaryFolder();
        injectNewSettingsFileForRunningWithGradleRunner(projectFolder);
        injectFileToBeEchoed(projectFolder);
        result = runPluginTask(projectFolder);
    }

    private BuildResult runPluginTask(File projectFolder) {
        return GradleRunner.create()
            .withProjectDir(projectFolder)
            .withPluginClasspath()
            .withArguments(TASK_NAME)
            .build();
    }

    @AfterEach
    public void cleanup() {
        cleanTempFolders();
    }

    public BuildResult getResult() {
        return result;
    }

    protected String getExpectedContent() {
        return expectedContent;
    }

    private static void copyFolder(Path src, Path dest) throws IOException {
        try (Stream<Path> stream = Files.walk(src)) {
            stream.forEach(source -> copySimpleFile(source, dest.resolve(src.relativize(source))));
        }
    }

    private static void copySimpleFile(Path source, Path dest) {
        try {
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void injectFileToBeEchoed(File projectFolder) throws IOException {
        expectedContent = "helloworld" + randomSuffix();
        insertResource(projectFolder, HARDCODED_FILENAME_TO_BE_ECHOED, expectedContent);
    }

    private void injectNewSettingsFileForRunningWithGradleRunner(File projectFolder) throws IOException {
        insertResource(projectFolder, SETTINGS_GRADLE, IoUtils.stringFromResources(SETTINGS_GRADLE_SAMPLE));
    }

    private File cloneTestingProjectInTemporaryFolder() throws IOException {
        File projectFolder = new File(tempFilename());
        Files.createDirectories(Path.of(projectFolder.getAbsolutePath()));
        copyFolder(new File(ROOT_FOLDER, HARDCODED_TESTRING_PROJECT_NAME).getAbsoluteFile().toPath(),
                   projectFolder.getAbsoluteFile().toPath());
        return projectFolder;
    }

    private void cleanTempFolders() {
        List<File> tempFolders = listTempFolders();
        tempFolders.forEach(this::deleteFile);
    }

    private void deleteFile(File file) {
        Stack<File> filesToBeDeleted = new Stack<>();
        filesToBeDeleted.add(file);
        while (!filesToBeDeleted.isEmpty()) {
            File currentFile = filesToBeDeleted.peek();
            if (isNonEmptyFolder(currentFile)) {
                File[] files = currentFile.listFiles();
                filesToBeDeleted.addAll(Arrays.asList(files));
            } else {
                currentFile.delete();
                filesToBeDeleted.pop();
            }
        }
    }

    private boolean isNonEmptyFolder(File currentFile) {
        return currentFile.isDirectory() && folderHasFiles(currentFile);
    }

    private boolean folderHasFiles(File currentFile) {
        return Optional.of(currentFile)
            .map(File::listFiles)
            .filter(this::isEmpty)
            .isPresent();
    }

    private boolean isEmpty(File[] array) {
        return array.length > 0;
    }

    private List<File> listTempFolders() {
        return Optional.ofNullable(CURRENT_FOLDER.listFiles((dir, name) -> name.startsWith(TEMP_FILE_PREFIX)))
            .map(Arrays::asList)
            .orElse(Collections.emptyList());
    }

    private void insertResource(File projectFolder, String destFilename, String buildGradleFile) throws IOException {
        File file = new File(projectFolder, destFilename);
        try (Writer writer = new FileWriter(file)) {
            writer.write(buildGradleFile);
            writer.flush();
        }
    }

    private String tempFilename() {
        return TEMP_FILE_PREFIX + randomSuffix();
    }

    private int randomSuffix() {
        return RANDOM.nextInt(10_000);
    }
}

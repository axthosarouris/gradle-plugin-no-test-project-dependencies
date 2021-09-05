package plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public abstract class EchoFileTask extends DefaultTask {

    @Inject
    public EchoFileTask() {
    }

    @Input
    @org.gradle.api.tasks.Optional
    public abstract Property<String> getProjectName();

    @Input
    public abstract Property<String> getFilePath();

    @TaskAction
    public void taskAction() throws FileNotFoundException {
        Project project = locateProject();
        File projectFolder = extractProjectFolder(project);
        File targetFile = locateTargetFile(projectFolder);
        String fileContents = new FileReader().readFile(targetFile.getAbsoluteFile().toPath());
        echoContents(fileContents);
    }

    private File locateTargetFile(File projectFolder) {
        return Optional.ofNullable(getFilePath().getOrNull())
            .map(pathString -> new File(projectFolder, pathString))
            .orElseThrow();
    }

    private void echoContents(String fileContents) {
        System.out.println(fileContents);
    }

    private File extractProjectFolder(Project project) {
        return project.getProjectDir().getAbsoluteFile();
    }

    private boolean isRootProject() {
        return
            this.getProjectName().equals(getProject().getRootProject().getName())
            || !this.getProjectName().isPresent()
            || this.getProjectName().equals("root");
    }

    private Project locateProject() {
        if (isRootProject()) {
            return this.getProject().getRootProject();
        }
        return locateProjectByName();
    }

    private Project locateProjectByName() {
        return this.getProject()
            .getAllprojects()
            .stream()
            .filter(project -> project.getName().equals(extractProjectName()))
            .findAny()
            .orElseThrow(() -> new RuntimeException("Could not find project with name:" + getProjectName().get()));
    }

    ;

    private String extractProjectName() {
        return getProjectName().getOrElse(this.getProject().getRootProject().getName());
    }
}

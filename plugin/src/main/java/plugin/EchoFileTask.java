package plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
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
    public abstract Property<String> getFilePath();

    @TaskAction
    public void taskAction() throws FileNotFoundException {
        Project project = this.getProject();
        new EchoFile(project, Path.of(getFilePath().get())).performAction();
    }




}

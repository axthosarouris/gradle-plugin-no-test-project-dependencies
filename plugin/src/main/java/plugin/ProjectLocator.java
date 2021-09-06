package plugin;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import org.gradle.api.Project;

public class ProjectLocator {

    private final String projectName;
    private final Project currentProject;

    public ProjectLocator(String projectName, Project currentProject) {

        this.projectName = projectName;
        this.currentProject = currentProject;
    }

    public Project locateProject() {
        if (targetProjectIsRootProject()) {
            return currentProject.getRootProject();
        }
        return locateProjectByName();
    }

    private boolean targetProjectIsRootProject() {
        return isNull(projectName) || currentProject.getName().equals(projectName);
    }

    private Project locateProjectByName() {
        return this.currentProject
            .getAllprojects()
            .stream()
            .filter(project -> project.getName().equals(extractProjectName()))
            .findAny()
            .orElseThrow(() -> new RuntimeException("Could not find project with name:" + projectName));
    }

    private String extractProjectName() {
        return nonNull(projectName) ? projectName : currentProject.getName();
    }
}

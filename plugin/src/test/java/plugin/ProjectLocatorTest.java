package plugin;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

class ProjectLocatorTest {

    @Test
    public void locateProjectReturnsSubprojectWhenSubprojectIsSpecifiedAsDesiredProject() {
        String parent = "parent";
        String subprojectName = "child";

        Project parentProject = ProjectBuilder.builder().withName(parent).build();
        Project subProject = ProjectBuilder.builder()
            .withName(subprojectName)
            .withParent(parentProject)
            .build();
        ProjectLocator projectLocator = new ProjectLocator(subprojectName, parentProject);
        assertThat(projectLocator.locateProject()).isEqualTo(subProject);
    }
}
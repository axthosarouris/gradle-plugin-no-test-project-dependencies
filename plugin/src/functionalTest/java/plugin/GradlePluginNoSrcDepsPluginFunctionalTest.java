package plugin;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

/**
 * A simple functional test for the plugin.
 */
public class GradlePluginNoSrcDepsPluginFunctionalTest extends FunctionalTest {

    @Test
    public void echoFileTask() {
        String output = getResult().getOutput();
        assertThat(output).contains(getExpectedContent());
    }

    @Test
    void canRunTask() {
        assertThat(getResult()).isNotNull();
        assertThat(getResult().getOutput()).isNotBlank();
    }
}

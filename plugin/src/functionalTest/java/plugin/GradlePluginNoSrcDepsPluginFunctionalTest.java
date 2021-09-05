package plugin;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * A simple functional test for the plugin.
 */
public class GradlePluginNoSrcDepsPluginFunctionalTest extends FunctionalTest {

    @Test
    void canRunTask() {
        // Verify the result
        assertThat(getResult()).isNotNull();
        assertThat(getResult().getOutput()).isNotBlank();
    }

    @Test
    public void echoFileTask(){
        String output= getResult().getOutput();
        assertThat(output).contains(getExpectedContent());
    }

}

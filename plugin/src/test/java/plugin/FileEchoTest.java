package plugin;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class FileEchoTest {

    @Test
    public void echoFileReturnsStringContainingFileContents() throws IOException {
        String contents = "some random contents";
        Path file = Files.createTempFile("file", "temp");
        writeString(file, contents);
        FileReader fileEcho = new FileReader();
        String actualString = fileEcho.readFile(file);
        assertThat(actualString).isEqualTo(contents);
    }

    private void writeString(Path file, String contents) throws IOException {
        FileWriter writer = new FileWriter(file.toAbsolutePath().toFile());
        writer.write(contents);
        writer.flush();
        writer.close();
    }
}
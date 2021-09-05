package plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class FileReader {

    public static final String LINE_SEPARATOR = System.lineSeparator();

    public String readFile(Path path) throws FileNotFoundException {
        File file = path.toAbsolutePath().toFile();
        return new BufferedReader(new java.io.FileReader(file)).lines()
            .collect(Collectors.joining(LINE_SEPARATOR));
    }
}

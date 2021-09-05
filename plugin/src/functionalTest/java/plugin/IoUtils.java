package plugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.stream.Collectors;

public final class IoUtils {

    public static final String DOUBLE_BACKSLASH_REGEX = "\\\\\\\\";
    public static final String LINE_SEPARATOR = System.lineSeparator();

    private IoUtils(){
    }

    public static InputStream inputStreamFromResources(Path path) {
        String resourcePath = replaceWindowsSeparatorWithUnixSeparator(path);
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
    }

    public static String stringFromResources(Path path) {
        InputStream resource = inputStreamFromResources(path);
        return toString(resource);
    }

    private static String toString(InputStream resource) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
        return reader.lines().collect(Collectors.joining(LINE_SEPARATOR));
    }

    private static String replaceWindowsSeparatorWithUnixSeparator(Path path) {
        return path.toString().replaceAll(DOUBLE_BACKSLASH_REGEX, "/");
    }
}

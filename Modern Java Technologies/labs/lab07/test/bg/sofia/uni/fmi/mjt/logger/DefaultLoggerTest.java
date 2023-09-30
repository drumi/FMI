package bg.sofia.uni.fmi.mjt.logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DefaultLoggerTest {

    private final static String DIRECTORY = "tmp3";

    @BeforeAll
    static void setUp() throws IOException {
        Files.createDirectory(Path.of(DIRECTORY));
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.delete(Path.of(DIRECTORY));
    }

    @AfterEach
    void cleanUp() throws IOException {
        DirectoryStream<Path> files = Files.newDirectoryStream(Path.of(DIRECTORY), "*.txt");

        for (var file : files) {
            Files.deleteIfExists(file);
        }
    }


    @Test
    void testLog() {
        var logger = new DefaultLogger(
            new LoggerOptions(Integer.class, DIRECTORY)
        );

        logger.log(Level.WARN, LocalDateTime.of(2022, 1, 1, 0, 0), "Happy New Year!");

        assertTrue(Files.exists(Path.of(DIRECTORY,"logs-0.txt")));
    }

    @Test
    void testLog_whenNull_thenThrow() {
        var logger = new DefaultLogger(
            new LoggerOptions(Integer.class, DIRECTORY)
        );

        assertThrows(IllegalArgumentException.class, () -> logger.log(null, LocalDateTime.of(2022, 1, 1, 0, 0), "Happy New Year!"));
        assertThrows(IllegalArgumentException.class, () -> logger.log(Level.WARN, null, "Happy New Year!"));
        assertThrows(IllegalArgumentException.class, () -> logger.log(Level.WARN, LocalDateTime.of(2022, 1, 1, 0, 0), null));
    }

    @Test
    void getCurrentFilePath_works() {
        var logger = new DefaultLogger(
            new LoggerOptions(Integer.class, DIRECTORY)
        );

        assertEquals(Path.of(DIRECTORY,"logs-0.txt"), logger.getCurrentFilePath());
    }

    @Test
    void optionsGetterWorks() {
        var options =  new LoggerOptions(Integer.class, DIRECTORY);

        options.setShouldThrowErrors(true);

        var logger = new DefaultLogger(options);

        assertEquals(options, logger.getOptions());
    }

    @Test
    void testLog_throwsException() {
        var options =  new LoggerOptions(Integer.class, DIRECTORY);

        options.setShouldThrowErrors(true);
        var logger = new DefaultLogger(options);

        logger.log(Level.WARN, LocalDateTime.of(2022, 1, 1, 0, 0), "Happy New Year!");

        Path path =  Path.of(DIRECTORY,"logs-0.txt");
        path.toFile().setWritable(false);

        assertThrows(LogException.class, () -> logger.log(Level.WARN, LocalDateTime.of(2022, 1, 1, 0, 0), "Happy New Year!"));
        path.toFile().setWritable(true);
    }

    @Test
    void testLog_throwsExceptionOnFileCollision() throws IOException {
        var options =  new LoggerOptions(Integer.class, DIRECTORY);

        options.setShouldThrowErrors(true);
        var logger = new DefaultLogger(options);
        options.setMaxFileSizeBytes(12);

        Files.createFile( Path.of(DIRECTORY,"logs-1.txt"));

        assertThrows(LogException.class, () -> logger.log(Level.WARN, LocalDateTime.of(2022, 1, 1, 0, 0), "Happy New Year!"));

        Files.deleteIfExists( Path.of(DIRECTORY,"logs-1.txt"));
    }

    @Test
    void testLog_createsNewFile() {
        var options =  new LoggerOptions(Integer.class, DIRECTORY);

        options.setShouldThrowErrors(true);
        options.setMaxFileSizeBytes(4);
        var logger = new DefaultLogger(options);

        logger.log(Level.WARN, LocalDateTime.of(2022, 1, 1, 0, 0), "Happy New Year!");
        logger.log(Level.WARN, LocalDateTime.of(2022, 1, 1, 0, 0), "Hap");

        assertTrue(Files.exists(Path.of(DIRECTORY,"logs-1.txt")));
    }
}
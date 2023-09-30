package bg.sofia.uni.fmi.mjt.logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultLogParserTest {

    private final static String TEST_FILE_NAME = "tmp2";

    @BeforeAll
    static void setUp() throws IOException{
        Files.write(Path.of(TEST_FILE_NAME),
            """
            [INFO]|2021-11-27T01:11:03.935601200|com.cryptobank.investment|Bought asset with ID 167305
            [WARN]|2021-11-27T01:11:04.099162900|com.cryptobank.investment|Disk space is getting low
            [ERROR]|2021-11-27T01:11:04.431274900|com.cryptobank.investment|Failed to process request {1405}
            """.getBytes()
        );
    }

    @Test
    void getLogs() {
        var parser = new DefaultLogParser(Path.of(TEST_FILE_NAME));

        var logs = parser.getLogs(Level.ERROR);

        assertEquals(1, logs.size());
    }

    @Test
    void testGetLogs() {
        var parser = new DefaultLogParser(Path.of(TEST_FILE_NAME));

        var logs = parser.getLogs(LocalDateTime.parse("2021-11-27T01:11:03.935601200"), LocalDateTime.parse("2023-11-27T01:11:03.935601200"));

        assertEquals(3, logs.size());
    }

    @Test
    void getLogsTail() {
        var parser = new DefaultLogParser(Path.of(TEST_FILE_NAME));

        var logs = parser.getLogsTail(11);

        assertEquals(3, logs.size());
    }

    @Test
    void getLogsTail_2() {
        var parser = new DefaultLogParser(Path.of(TEST_FILE_NAME));

        var logs = parser.getLogsTail(1);

        assertEquals(1, logs.size());
    }

    @Test
    void getLogsTail_throwsOnNegative() {
        var parser = new DefaultLogParser(Path.of(TEST_FILE_NAME));

        assertThrows(IllegalArgumentException.class, () -> parser.getLogsTail(-1));
    }

    @Test
    void getLogs_throwsOnNull() {
        var parser = new DefaultLogParser(Path.of(TEST_FILE_NAME));

        assertThrows(IllegalArgumentException.class, () -> parser.getLogs(null, null));
    }

    @Test
    void getLogs_whenLevelIsCalledWithNull_thenThrow() {
        var parser = new DefaultLogParser(Path.of(TEST_FILE_NAME));

        assertThrows(IllegalArgumentException.class, () -> parser.getLogs(null));
    }

    @AfterAll
    static void tearDown() throws IOException{
        Files.delete(Path.of(TEST_FILE_NAME));
    }

    private List<Log> getTestLogs() {
        return List.of(
            new Log(Level.INFO, LocalDateTime.parse("2021-11-27T01:11:03.935601200"), "com.cryptobank.investment", "Bought asset with ID 167305"),
            new Log(Level.WARN, LocalDateTime.parse("2021-11-27T01:11:04.099162900"), "com.cryptobank.investment", "Disk space is getting low"),
            new Log(Level.ERROR, LocalDateTime.parse("2021-11-27T01:11:04.431274900"), "com.cryptobank.investment", "Failed to process request {1405}")
        );
    }
}
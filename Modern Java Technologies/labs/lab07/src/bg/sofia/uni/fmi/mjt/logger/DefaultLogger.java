package bg.sofia.uni.fmi.mjt.logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class DefaultLogger implements Logger {

    private static final String LOG_PREFIX = "logs-";
    private static final String LOG_EXTENSION = ".txt";
    private static final String DELIMITER = "|";

    private LoggerOptions options;

    private int currentNumberPrefix = 0;
    private int bytesWrittenToCurrentFile = 0;

    public DefaultLogger(LoggerOptions options) {
        this.options = options;
        createNextFile();
    }

    private void createNextFile() {
        try {
            Files.createFile(getCurPath());
        } catch (IOException e) {
            if (options.shouldThrowErrors()) {
                throw new LogException(e);
            }
        }
    }

    @Override
    public void log(Level level, LocalDateTime timestamp, String message) {
        if (level == null || timestamp == null || message == null || message.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (level.getLevel() < options.getMinLogLevel().getLevel()) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("[")
              .append(level)
              .append("]")
              .append(DELIMITER)
              .append(timestamp)
              .append(DELIMITER)
              .append(options.getClazz().getPackageName())
              .append(DELIMITER)
              .append(message)
              .append(System.lineSeparator());

        String line = sb.toString();

        try {
            Files.writeString(getCurPath(), line, StandardOpenOption.APPEND);
        } catch (IOException e) {
            if (options.shouldThrowErrors()) {
                throw new LogException(e);
            }
        }

        bytesWrittenToCurrentFile += line.getBytes(StandardCharsets.UTF_8).length;

        if (bytesWrittenToCurrentFile > options.getMaxFileSizeBytes()) {
            bytesWrittenToCurrentFile = 0;
            currentNumberPrefix++;
            createNextFile();
        }
    }

    @Override
    public LoggerOptions getOptions() {
        return options;
    }

    @Override
    public Path getCurrentFilePath() {
        return getCurPath();
    }

    private Path getCurPath() {
        return Path.of(options.getDirectory(), LOG_PREFIX + currentNumberPrefix + LOG_EXTENSION);
    }

}

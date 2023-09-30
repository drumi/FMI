package bg.sofia.uni.fmi.mjt.logger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultLogParser implements LogParser {

    private static final String DELIMITER_REGEX = "\\|";

    private final static int LOG_INDEX = 0;
    private final static int DATE_INDEX = 1;
    private final static int PACKAGE_INDEX = 2;
    private final static int MESSAGE_INDEX = 3;

    private final Path logsFilePath;
    private List<Log> logs;

    public DefaultLogParser(Path logsFilePath) {
        this.logsFilePath = logsFilePath;

        // logs = readLogs();
    }

    @Override
    public List<Log> getLogs(Level level) {
        logs = readLogs();
        if (level == null) {
            throw new IllegalArgumentException();
        }

        List<Log> filteredLogs = new ArrayList<>();

        for (var log : logs) {
            if (log.level() == level) {
                filteredLogs.add(log);
            }
        }

        return filteredLogs;
    }

    private List<Log> readLogs() {
        List<String> lines;

        try {
            lines = Files.readAllLines(logsFilePath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        List<Log> list = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split(DELIMITER_REGEX);

            list.add(
                new Log(Level.valueOf(tokens[LOG_INDEX].replaceAll("[\\[\\]]", "")),
                    LocalDateTime.parse(tokens[DATE_INDEX]),
                    tokens[PACKAGE_INDEX],
                    tokens[MESSAGE_INDEX]
                )
            );
        }

        return list;
    }

    @Override
    public List<Log> getLogs(LocalDateTime from, LocalDateTime to) {
        logs = readLogs();
        if (from == null || to == null) {
            throw new IllegalArgumentException();
        }

        List<Log> filteredLogs = new ArrayList<>();

        for (var log : logs) {
            if (!log.timestamp().isBefore(from) && !log.timestamp().isAfter(to)) {
                filteredLogs.add(log);
            }
        }

        return filteredLogs;
    }

    @Override
    public List<Log> getLogsTail(int n) {
        logs = readLogs();
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        if (n == 0) {
            return Collections.emptyList();
        }

        if (n >= logs.size()) {
            return logs;
        }

        return logs.subList(logs.size() - n, logs.size());
    }
}

package bg.sofia.uni.fmi.mjt.boardgames.io;

import bg.sofia.uni.fmi.mjt.boardgames.io.exceptions.BadCSVFormatException;
import bg.sofia.uni.fmi.mjt.boardgames.utils.FileUtils;
import bg.sofia.uni.fmi.mjt.boardgames.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class BasicCSVReader implements CSVReader {

    private static final String CSV_DELIMITER_REGEX = ";";

    private final BufferedReader reader;
    private final List<String> columnNames;

    public BasicCSVReader(Reader reader) {
        Utils.requireNonNull(reader, "Reader cannot be null");

        this.reader = new BufferedReader(reader);
        columnNames = extractColumnNames();
    }

    private List<String> extractColumnNames() {
        String line = FileUtils.stripBOM(readLine());
        return extractColumnValues(line);
    }

    private String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<String> extractColumnValues(String line) {
        String[] tokens = line.split(CSV_DELIMITER_REGEX);
        return Arrays.asList(tokens);
    }

    private List<String> extractAndValidateColumnValues(String line) {
        List<String> tmp = extractColumnValues(line);
        ensureCorrectSize(tmp);
        return tmp;
    }

    private <T> void ensureCorrectSize(List<T> tokens) {
        if (tokens.size() != columnNames.size()) {
            throw new BadCSVFormatException("The csv file is with incorrect formatting");
        }
    }

    @Override
    public Map<String, String> read() {
        String line = readLine();
        return asMap(columnNames, extractAndValidateColumnValues(line));
    }

    @Override
    public List<Map<String, String>> readAll() {
        return reader.lines()
                     .map(line -> asMap(columnNames, extractAndValidateColumnValues(line)))
                     .toList();
    }

    @Override
    public List<String> getColumnNames() {
        return Collections.unmodifiableList(columnNames);
    }

    private static <T> Map<T, T> asMap(List<T> keys, List<T> values) {
        assert keys.size() == values.size();

        Map<T, T> map = new HashMap<>();

        var keyIter = keys.iterator();
        var valueIter = values.iterator();

        while (keyIter.hasNext()) {
            map.put(keyIter.next(), valueIter.next());
        }

        return map;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

}

package bg.sofia.uni.fmi.mjt.boardgames.recommender;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import bg.sofia.uni.fmi.mjt.boardgames.io.BasicBoardGameCSVReader;
import bg.sofia.uni.fmi.mjt.boardgames.io.exceptions.BadCSVFormatException;
import bg.sofia.uni.fmi.mjt.boardgames.io.exceptions.DataValidationException;
import bg.sofia.uni.fmi.mjt.boardgames.recommender.exceptions.RecommenderException;
import bg.sofia.uni.fmi.mjt.boardgames.statistics.BasicBoardGameDistanceCalculator;
import bg.sofia.uni.fmi.mjt.boardgames.statistics.BoardGameDistanceCalculator;
import bg.sofia.uni.fmi.mjt.boardgames.utils.FileUtils;
import bg.sofia.uni.fmi.mjt.boardgames.utils.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class BoardGamesRecommender implements Recommender {

    private static final String WORD_DELIMITER_REGEX = "[\\p{IsPunctuation}\\p{IsWhite_Space}]+";

    private static final String MAP_DELIMITER_OUTPUT = ": ";
    private static final String WORD_DELIMITER_OUTPUT = ", ";

    private final static Charset FILE_ENCODING = StandardCharsets.UTF_8;

    private Collection<BoardGame> games;
    private Set<String> stopwords;

    private final Map<Integer, BoardGame> idToGame = new HashMap<>();
    private final Map<String, Set<Integer>> keywordToSetOfGamesId = new HashMap<>();

    private final BoardGameDistanceCalculator distanceCalculator = new BasicBoardGameDistanceCalculator();

    /**
     * Constructs an instance using the provided file names.
     *
     * @param datasetZipFile  ZIP file containing the board games dataset file
     * @param datasetFileName the name of the dataset file (inside the ZIP archive)
     * @param stopwordsFile   the stopwords file
     */
    public BoardGamesRecommender(Path datasetZipFile, String datasetFileName, Path stopwordsFile) {
        Utils.requireNonNull(datasetZipFile, "Path cannot be null");
        Utils.requireNonNull(datasetFileName, "File name cannot be null");
        Utils.requireNonEmpty(datasetFileName, "File name cannot be empty");
        Utils.requireNonNull(stopwordsFile, "File name cannot be null");

        Reader gamesReader = createReaderFromZipPathWithExceptionWrapping(datasetZipFile, datasetFileName);
        Reader stopwordsReader = createReaderFromPathWithExceptionWrapping(stopwordsFile);

        init(gamesReader, stopwordsReader);
    }

    /**
     * Constructs an instance using the provided Reader streams.
     *
     * @param dataset   Reader from which the dataset can be read
     * @param stopwords Reader from which the stopwords list can be read
     */
    public BoardGamesRecommender(Reader dataset, Reader stopwords) {
        Utils.requireNonNull(dataset, "dataset reader cannot be null");
        Utils.requireNonNull(stopwords, "stopwords reader cannot be null");

        initWithExceptionWrapping(dataset, stopwords);
    }

    private InputStreamReader createReaderFromZipPath(Path datasetZipFile, String datasetFileName)
        throws IOException {
        ZipFile zipFile = new ZipFile(datasetZipFile.toFile());
        ZipEntry entry = zipFile.getEntry(datasetFileName);

        if (entry == null) {
            zipFile.close();
            throw new IOException("The csv file is missing from the zip archive");
        }

        InputStream stream = zipFile.getInputStream(entry);

        return new InputStreamReader(stream, FILE_ENCODING) {
            @Override
            public void close() throws IOException {
                zipFile.close();
            }
        };
    }

    private InputStreamReader createReaderFromZipPathWithExceptionWrapping(Path datasetZipFile,
                                                                           String datasetFileName) {
        try {
            return createReaderFromZipPath(datasetZipFile, datasetFileName);
        } catch (FileNotFoundException e) {
            throw new RecommenderException("Path to dataset zip file is incorrect", e);
        } catch (ZipException e) {
            throw new RecommenderException("The provided zip file is malformed", e);
        } catch (IOException e) {
            throw new RecommenderException(e);
        }
    }

    private Reader createReaderFromPathWithExceptionWrapping(Path path) {
        try {
            return new BufferedReader(new FileReader(path.toFile(), FILE_ENCODING));
        } catch (FileNotFoundException e) {
            throw new RecommenderException("Path to stopwords file is incorrect", e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void init(Reader dataset, Reader stopwords) {
        readGames(dataset);
        readStopwords(stopwords);
        populateWordIndexes();
    }

    private void initWithExceptionWrapping(Reader dataset, Reader stopwords) {
        try {
            init(dataset, stopwords);
        } catch (DataValidationException | BadCSVFormatException e) {
            throw new RecommenderException("The provided readers are not in the correct format", e);
        } catch (UncheckedIOException e) {
            throw new RecommenderException("The provided readers do not behave correctly", e);
        }
    }

    private void readGames(Reader reader) {
        try (var csvreader = new BasicBoardGameCSVReader(reader)) {
            games = csvreader.readAll();
            putGamesIntoMap(games);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void putGamesIntoMap(Collection<BoardGame> games) {
        games.forEach(game -> idToGame.put(game.id(), game));
    }

    private void populateWordIndexes() {
        games.forEach(game -> {
            String[] words = game.description().split(WORD_DELIMITER_REGEX);

            Arrays.stream(words)
                  .map(String::toLowerCase)
                  .filter(word -> !stopwords.contains(word))
                  .forEach(word -> {
                      var set = keywordToSetOfGamesId.getOrDefault(word, createSet());
                      set.add(game.id());
                      keywordToSetOfGamesId.put(word, set);
                  });
        });
    }

    private static <T> Set<T> createSet() {
        return new HashSet<>();
    }

    private void readStopwords(Reader reader) {
        try (var bufReader = new BufferedReader(reader)) {
            String firstLine = FileUtils.stripBOM(bufReader.readLine());

            stopwords = bufReader.lines()
                                 .collect(Collectors.toSet());

            stopwords.add(firstLine);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Collection<BoardGame> getGames() {
        return Collections.unmodifiableCollection(games);
    }

    @Override
    public List<BoardGame> getSimilarTo(BoardGame game, int n) {
        Utils.requireNonNull(game, "BoardGame cannot be null");
        Utils.requireNonNegative(n, "int cannot be negative");

        return games.stream()
                    .filter(g -> g.categories().stream()
                                               .anyMatch(game.categories()::contains))
                    .filter(g -> !g.equals(game))
                    .sorted(Comparator.comparingDouble(o -> distanceCalculator.calculate(o, game)))
                    .limit(n)
                    .toList();
    }

    @Override
    public List<BoardGame> getByDescription(String... keywords) {
        Utils.requireNonNull(keywords, "varargs cannot be null");
        Utils.requireAllNonNull(keywords);

        Map<BoardGame, Integer> multiset = calculateMultisetByKeywords(keywords);

        return new ArrayList<>(multiset.keySet()) {{
                sort(Comparator.comparingInt(multiset::get).reversed());
            }};
    }

    private Map<BoardGame, Integer> calculateMultisetByKeywords(String... keywords) {
        Map<BoardGame, Integer> multiset = new HashMap<>();

        Arrays.stream(keywords)
              .distinct()
              .forEach(keyword ->
                  keywordToSetOfGamesId.getOrDefault(keyword.toLowerCase(), createSet())
                                       .forEach(gameId -> multiset.merge(idToGame.get(gameId), 1, Integer::sum)));

        return multiset;
    }

    @Override
    public void storeGamesIndex(Writer writer) {
        Utils.requireNonNull(writer, "Writer cannot be null");

        String index = serializeGameIndex();

        writeWithExceptionWrapping(writer, index);
    }

    private String serializeGameIndex() {
        var builder = new StringBuilder();

        keywordToSetOfGamesId.keySet().forEach(word -> {
            builder.append(word)
                   .append(MAP_DELIMITER_OUTPUT);

            var line = String.join(
                WORD_DELIMITER_OUTPUT,
                keywordToSetOfGamesId.get(word).stream()
                                     .map(Object::toString)
                                     .toList()
            );

            builder.append(line)
                   .append(System.lineSeparator());
        });

        return builder.toString();
    }

    private void writeWithExceptionWrapping(Writer writer, String s) {
        try {
            writer.write(s);
        } catch (IOException e) {
            throw new RecommenderException("The provided writer does not behave correctly", e);
        }
    }
}

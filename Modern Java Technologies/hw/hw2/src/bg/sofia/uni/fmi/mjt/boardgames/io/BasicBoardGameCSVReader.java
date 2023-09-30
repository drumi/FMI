package bg.sofia.uni.fmi.mjt.boardgames.io;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import bg.sofia.uni.fmi.mjt.boardgames.io.exceptions.DataValidationException;
import bg.sofia.uni.fmi.mjt.boardgames.utils.Utils;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class BasicBoardGameCSVReader implements BoardGameReader {

    private static final String LIST_DELIMITER_REGEX = ",";

    private enum BoardGameColumnNames {
        ID("id"),
        NAME("details.name"),
        DESCRIPTION("details.description"),
        MAX_PLAYERS("details.maxplayers"),
        MIN_AGE("details.minage"),
        MIN_PLAYERS("details.minplayers"),
        PLAYING_TIME_MINS("details.playingtime"),
        CATEGORIES("attributes.boardgamecategory"),
        MECHANICS("attributes.boardgamemechanic");

        private final String columnName;

        BoardGameColumnNames(String s) {
            columnName = s;
        }

        @Override
        public String toString() {
            return columnName;
        }
    }

    private final CSVReader reader;

    public BasicBoardGameCSVReader(Reader reader) {
        Utils.requireNonNull(reader, "reader cannot be null");

        this.reader = new BasicCSVReader(reader);
        validate();
    }

    private void validate() {
        List<String> readerColumnNames = reader.getColumnNames();
        List<String> requiredColumnNames = Arrays.stream(BoardGameColumnNames.values())
                                                 .map(BoardGameColumnNames::toString)
                                                 .toList();

        if (!readerColumnNames.containsAll(requiredColumnNames)) {
            throw new DataValidationException("Required columns are missing from the csv file");
        }
    }

    @Override
    public BoardGame read() {
        return createBoardGame(reader.read());
    }

    @Override
    public List<BoardGame> readAll() {
        return reader.readAll().stream()
                               .map(this::createBoardGame)
                               .toList();
    }

    private BoardGame createBoardGame(Map<String, String> columnToValue) {
        int id              = Integer.parseInt(columnToValue.get(BoardGameColumnNames.ID.toString()));
        int maxPlayers      = Integer.parseInt(columnToValue.get(BoardGameColumnNames.MAX_PLAYERS.toString()));
        int minAge          = Integer.parseInt(columnToValue.get(BoardGameColumnNames.MIN_AGE.toString()));
        int minPlayers      = Integer.parseInt(columnToValue.get(BoardGameColumnNames.MIN_PLAYERS.toString()));
        int playingTimeMins = Integer.parseInt(columnToValue.get(BoardGameColumnNames.PLAYING_TIME_MINS.toString()));

        String name        = columnToValue.get(BoardGameColumnNames.NAME.toString());
        String description = columnToValue.get(BoardGameColumnNames.DESCRIPTION.toString());

        List<String> categories = createListFromString(columnToValue.get(BoardGameColumnNames.CATEGORIES.toString()));
        List<String> mechanics  = createListFromString(columnToValue.get(BoardGameColumnNames.MECHANICS.toString()));

        return BoardGame.with()
                        .id(id)
                        .name(name)
                        .description(description)
                        .maxPlayers(maxPlayers)
                        .minAge(minAge)
                        .minPlayers(minPlayers)
                        .playingTimeMins(playingTimeMins)
                        .categories(categories)
                        .mechanics(mechanics)
                        .build();
    }

    private List<String> createListFromString(String line) {
        return Arrays.asList(line.split(LIST_DELIMITER_REGEX));
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

}

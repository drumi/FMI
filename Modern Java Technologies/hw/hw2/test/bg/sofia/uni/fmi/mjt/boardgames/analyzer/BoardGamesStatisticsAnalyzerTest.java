package bg.sofia.uni.fmi.mjt.boardgames.analyzer;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BoardGamesStatisticsAnalyzerTest {

    @Test
    void getNMostPopularCategories_whenRequested_thenItWorksWithSingleCategory() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(createListOfGames());
        assertEquals("Shooter", analyzer.getNMostPopularCategories(1).get(0));
    }

    @Test
    void getNMostPopularCategories_whenNumberIsNegative_thenThrow() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(createListOfGames());
        assertThrows(
            IllegalArgumentException.class,
            () -> analyzer.getNMostPopularCategories(-1)
        );
    }

    @Test
    void getNMostPopularCategories_whenRequestedWithMultipleCategories() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(createListOfGames());
        assertIterableEquals(
            List.of("Shooter", "Multiplayer"),
            analyzer.getNMostPopularCategories(2)
        );
    }

    @Test
    void getNMostPopularCategories_whenRequestedWithNoGames_thenItReturnsEmptyList() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(Collections.emptyList());
        assertIterableEquals(
            Collections.emptyList(),
            analyzer.getNMostPopularCategories(99)
        );
    }

    @Test
    void getNMostPopularCategories_returnsUnmodifiableList() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(Collections.emptyList());
        assertThrows(Exception.class, () -> analyzer.getNMostPopularCategories(99).add("element"));
    }

    @Test
    void getAverageMinAge_whenRequestedNormally_thenGivesCorrectResult() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(createListOfGames());
        assertEquals(14, analyzer.getAverageMinAge());
    }

    @Test
    void getAverageMinAge_whenRequestedIsCached_thenGivesCorrectResult() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(createListOfGames());
        // put in cache
        assertEquals(14, analyzer.getAverageMinAge());
        // test cached result
        assertEquals(14, analyzer.getAverageMinAge());
    }

    @Test
    void getAveragePlayingTimeByCategory_whenTheCategoryIsAbsent_thenReturnsZero() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(createListOfGames());
        assertEquals(0.0, analyzer.getAveragePlayingTimeByCategory("TriplePlayer"), 0.001);
    }

    @Test
    void getAveragePlayingTimeByCategory_whenCategoryIsNull_thenThrow() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(createListOfGames());
        assertThrows(IllegalArgumentException.class, () -> analyzer.getAveragePlayingTimeByCategory(null));
    }

    @Test
    void getAveragePlayingTimeByCategory_whenCategoryIsEmpty_thenThrow() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(createListOfGames());
        assertThrows(IllegalArgumentException.class, () -> analyzer.getAveragePlayingTimeByCategory(""));
    }

    @Test
    void getAveragePlayingTimeByCategory_whenRequested_thenGivesCorrectResult() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(createListOfGames());
        assertEquals(60.0, analyzer.getAveragePlayingTimeByCategory("Multiplayer"), 0.001);
        assertEquals(125.0, analyzer.getAveragePlayingTimeByCategory("Single Player"), 0.001);
    }

    @Test
    void getAveragePlayingTimeByCategory_whenRequestedIsCached_thenGivesCorrectResult() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(createListOfGames());
        // put in cache
        assertEquals(60.0, analyzer.getAveragePlayingTimeByCategory("Multiplayer"), 0.001);
        assertEquals(125.0, analyzer.getAveragePlayingTimeByCategory("Single Player"), 0.001);
        //test cached result
        assertEquals(60.0, analyzer.getAveragePlayingTimeByCategory("Multiplayer"), 0.001);
        assertEquals(125.0, analyzer.getAveragePlayingTimeByCategory("Single Player"), 0.001);
    }

    @Test
    void getAveragePlayingTimeByCategory_whenNoGames_thenReturnsZero() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(Collections.emptyList());
        assertEquals(0.0, analyzer.getAveragePlayingTimeByCategory("Test"), 0.001);
    }

    @Test
    void getAveragePlayingTimeByCategory_whenRequested_thenReturnsCorrectMap() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(createListOfGames());
        assertEquals(
            Map.ofEntries(
                Map.entry("Multiplayer", 60.0),
                Map.entry("Single Player", 125.0),
                Map.entry("Clicker", 100.0),
                Map.entry("RPG", 110.0),
                Map.entry("Shooter", 80.0)
            ),
            analyzer.getAveragePlayingTimeByCategory()
        );
    }

    @Test
    void getAveragePlayingTimeByCategory_whenRequestedIsCached_thenReturnsCorrectMap() {
        BoardGamesStatisticsAnalyzer analyzer = new BoardGamesStatisticsAnalyzer(createListOfGames());
        // put in cache
        assertEquals(
            Map.ofEntries(
                Map.entry("Multiplayer", 60.0),
                Map.entry("Single Player", 125.0),
                Map.entry("Clicker", 100.0),
                Map.entry("RPG", 110.0),
                Map.entry("Shooter", 80.0)
            ),
            analyzer.getAveragePlayingTimeByCategory()
        );

        // test cached result
        assertEquals(
            Map.ofEntries(
                Map.entry("Multiplayer", 60.0),
                Map.entry("Single Player", 125.0),
                Map.entry("Clicker", 100.0),
                Map.entry("RPG", 110.0),
                Map.entry("Shooter", 80.0)
            ),
            analyzer.getAveragePlayingTimeByCategory()
        );
    }

    private List<BoardGame> createListOfGames() {
        return List.of(
            BoardGame.with()
                     .id(1)
                     .name("CS")
                     .description("Good shooter game")
                     .maxPlayers(32)
                     .minAge(20)
                     .minPlayers(4)
                     .playingTimeMins(30)
                     .categories(List.of("Shooter", "Multiplayer"))
                     .mechanics(List.of("Moving"))
                     .build(),

            BoardGame.with()
                     .id(2)
                     .name("CS 2")
                     .description("Better shooter game")
                     .maxPlayers(32)
                     .minAge(18)
                     .minPlayers(4)
                     .playingTimeMins(70)
                     .categories(List.of("Shooter", "Single Player", "Multiplayer"))
                     .mechanics(List.of("Motion Engine"))
                     .build(),

            BoardGame.with()
                     .id(3)
                     .name("Heroes 3")
                     .description("Good strategy game")
                     .maxPlayers(8)
                     .minAge(6)
                     .minPlayers(4)
                     .playingTimeMins(100)
                     .categories(List.of("Clicker", "Multiplayer"))
                     .mechanics(List.of("2D"))
                     .build(),

            BoardGame.with()
                     .id(4)
                     .name("Unmapped 2")
                     .description("Great exploration game")
                     .maxPlayers(1)
                     .minAge(22)
                     .minPlayers(4)
                     .playingTimeMins(180)
                     .categories(List.of("Shooter", "RPG", "Single Player"))
                     .mechanics(List.of("3D"))
                     .build(),

            BoardGame.with()
                     .id(5)
                     .name("Unmapped 3")
                     .description("Nice exploration game")
                     .maxPlayers(2)
                     .minAge(4)
                     .minPlayers(4)
                     .playingTimeMins(40)
                     .categories(List.of("Shooter", "RPG", "Multiplayer"))
                     .mechanics(List.of("3D", "Aim assist"))
                     .build()

        );
    }

}
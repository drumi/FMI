package bg.sofia.uni.fmi.mjt.boardgames.statistics;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicBoardGameDistanceCalculatorTest {

    static final double DOUBLE_PRECISION = 0.001;

    @Test
    void calculate_worksNormally_testCase1() {
        BasicBoardGameDistanceCalculator calculator = new BasicBoardGameDistanceCalculator();
        var game1 = BoardGame.with()
                             .id(1)
                             .name("Sudoku")
                             .description("Nice game")
                             .maxPlayers(4)
                             .minAge(12)
                             .minPlayers(4)
                             .playingTimeMins(30)
                             .categories(List.of("Boring", "Dice", "Night"))
                             .mechanics(List.of("Luck"))
                             .build();

        var game2 = BoardGame.with()
                             .id(2)
                             .name("Sudo ku")
                             .description("Nicer game")
                             .maxPlayers(8)
                             .minAge(6)
                             .minPlayers(4)
                             .playingTimeMins(27)
                             .categories(List.of("Boring", "Dice", "Day"))
                             .mechanics(List.of("Luck", "Dynamic"))
                             .build();

        // sqrt[(4-8)^2 + (12-6)^2 + (4-4)^2 + (30-27)^2] + (|4|-|2|) + (|2|-|1|) = 10.81024967591
        assertEquals(10.81024967591, calculator.calculate(game1, game2), DOUBLE_PRECISION);
    }

    @Test
    void calculate_worksNormally_testCase2() {
        BasicBoardGameDistanceCalculator calculator = new BasicBoardGameDistanceCalculator();
        var game1 = BoardGame.with()
                             .id(1)
                             .name("Sudoku")
                             .description("Nice game")
                             .maxPlayers(4)
                             .minAge(12)
                             .minPlayers(4)
                             .playingTimeMins(30)
                             .categories(List.of("Fun", "Family", "Night"))
                             .mechanics(List.of("Luck"))
                             .build();

        var game2 = BoardGame.with()
                             .id(2)
                             .name("Sudo ku")
                             .description("Nicer game")
                             .maxPlayers(8)
                             .minAge(3)
                             .minPlayers(2)
                             .playingTimeMins(25)
                             .categories(List.of("Boring", "Dice", "Day"))
                             .mechanics(List.of("Luck", "Dynamic"))
                             .build();

        // sqrt[(4-8)^2 + (12-3)^2 + (4-2)^2 + (30-25)^2] + (|6|-|0|) + (|2|-|1|) = 18.2249721603
        assertEquals(18.2249721603, calculator.calculate(game1, game2), DOUBLE_PRECISION);
    }

}
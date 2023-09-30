package bg.sofia.uni.fmi.mjt.boardgames.statistics;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;

@FunctionalInterface
public interface BoardGameDistanceCalculator {

    double calculate(BoardGame g1, BoardGame g2);

}

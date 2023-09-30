package bg.sofia.uni.fmi.mjt.boardgames.statistics;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;

public class BasicBoardGameDistanceCalculator implements BoardGameDistanceCalculator {

    @Override
    public double calculate(BoardGame g1, BoardGame g2) {
        int playingTimeDelta = (g1.playingTimeMins() - g2.playingTimeMins());
        int maxPlayersDelta = (g1.maxPlayers() - g2.maxPlayers());
        int minAgeDelta = (g1.minAge() - g2.minAge());
        int minPlayersDelta = (g1.minPlayers() - g2.minPlayers());

        int boardGameCategoryIntersection = (int) g1.categories().stream()
                                                    .filter(g2.categories()::contains)
                                                    .count();

        int boardGameMechanicsIntersection = (int) g1.mechanics().stream()
                                                     .filter(g2.mechanics()::contains)
                                                     .count();

        int boardGameCategoryUnion = g1.categories().size() + g2.categories().size() - boardGameCategoryIntersection;
        int boardGameMechanicsUnion = g1.mechanics().size() + g2.mechanics().size() - boardGameMechanicsIntersection;

        double numericalDistance = Math.sqrt(
                playingTimeDelta * playingTimeDelta +
                maxPlayersDelta * maxPlayersDelta +
                minAgeDelta * minAgeDelta +
                minPlayersDelta * minPlayersDelta
        );

        int setDistance = (boardGameCategoryUnion + boardGameMechanicsUnion) -
                          (boardGameCategoryIntersection + boardGameMechanicsIntersection);

        return numericalDistance + setDistance;
    }

}

package bg.sofia.uni.fmi.mjt.boardgames.analyzer;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import bg.sofia.uni.fmi.mjt.boardgames.utils.StringDoublePair;
import bg.sofia.uni.fmi.mjt.boardgames.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BoardGamesStatisticsAnalyzer implements StatisticsAnalyzer {

    private final Collection<BoardGame> games;

    // Cache variables
    private final Map<String, Double> categoryToAveragePlayingTime = new HashMap<>();
    private List<String> categoriesByPopularity;
    private double averageMinAgeCache;

    // Cache validation
    private boolean areCategoriesByPopularityValid = false;
    private boolean isAverageMinAgeCacheValid = false;
    private Set<String> categoryToAveragePlayingTimeValid = new HashSet<>();
    private boolean areAllCategoriesToAveragePlayingTimeValid = false;

    public BoardGamesStatisticsAnalyzer(Collection<BoardGame> games) {
        Utils.requireNonNull(games, "games cannot be null");
        Utils.requireAllNonNull(games, "games cannot contain null elements");

        this.games = games;
    }

    private void calculateCategoriesByPopularity() {
        Map<String, Integer> multiSet = getCategoriesMultiSet();

        categoriesByPopularity = new ArrayList<>(multiSet.keySet()) {{
                sort(Comparator.comparingInt(multiSet::get).reversed());
            }};
    }

    private Map<String, Integer> getCategoriesMultiSet() {
        return games.stream()
                    .flatMap(game -> game.categories().stream())
                    .collect(Collectors.toMap(Function.identity(), category -> 1, Integer::sum));
    }

    @Override
    public List<String> getNMostPopularCategories(int n) {
        Utils.requireNonNegative(n, "Integer cannot be negative");

        if (!areCategoriesByPopularityValid) {
            calculateCategoriesByPopularity();
            areCategoriesByPopularityValid = true;
        }

        if (n >= categoriesByPopularity.size()) {
            return Collections.unmodifiableList(categoriesByPopularity);
        }

        return Collections.unmodifiableList(categoriesByPopularity.subList(0, n));
    }

    @Override
    public double getAverageMinAge() {
        if (!isAverageMinAgeCacheValid) {
            averageMinAgeCache = games.stream()
                                      .mapToInt(BoardGame::minAge)
                                      .average()
                                      .orElse(0.0);

            isAverageMinAgeCacheValid = true;
        }

        return averageMinAgeCache;
    }

    @Override
    public double getAveragePlayingTimeByCategory(String category) {
        Utils.requireNonNull(category, "Category cannot be null");
        Utils.requireNonEmpty(category, "Category cannot be an empty string");

        if (categoryToAveragePlayingTimeValid.contains(category)) {
            return categoryToAveragePlayingTime.get(category);
        }

        double average = games.stream()
                               .filter(game -> game.categories().contains(category))
                               .mapToDouble(BoardGame::playingTimeMins)
                               .average()
                               .orElse(0.0);

        categoryToAveragePlayingTime.put(category, average);
        categoryToAveragePlayingTimeValid.add(category);

        return average;
    }

    @Override
    public Map<String, Double> getAveragePlayingTimeByCategory() {
        if (areAllCategoriesToAveragePlayingTimeValid) {
            return categoryToAveragePlayingTime;
        }

        Map<String, List<StringDoublePair>> categoryToList =
            games.stream()
                 .flatMap(game -> game.categories().stream()
                                                   .map(category ->
                                                       new StringDoublePair(category, game.playingTimeMins())))
                 .collect(Collectors.groupingBy(StringDoublePair::first));

        categoryToList.keySet().forEach(key -> {
            double average = categoryToList.get(key).stream()
                                                    .mapToDouble(StringDoublePair::second)
                                                    .average()
                                                    .orElse(0.0);
            categoryToAveragePlayingTime.put(key, average);
        });

        areAllCategoriesToAveragePlayingTimeValid = true;
        categoryToAveragePlayingTimeValid = categoryToList.keySet();

        return categoryToAveragePlayingTime;
    }

}

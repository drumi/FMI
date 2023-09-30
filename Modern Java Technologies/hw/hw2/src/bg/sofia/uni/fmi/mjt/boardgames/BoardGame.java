package bg.sofia.uni.fmi.mjt.boardgames;

import java.util.Collection;

import static bg.sofia.uni.fmi.mjt.boardgames.BoardGame.IntermediateBuilder.RequiringCategories;
import static bg.sofia.uni.fmi.mjt.boardgames.BoardGame.IntermediateBuilder.RequiringDescription;
import static bg.sofia.uni.fmi.mjt.boardgames.BoardGame.IntermediateBuilder.RequiringId;
import static bg.sofia.uni.fmi.mjt.boardgames.BoardGame.IntermediateBuilder.RequiringMaxPlayers;
import static bg.sofia.uni.fmi.mjt.boardgames.BoardGame.IntermediateBuilder.RequiringMechanics;
import static bg.sofia.uni.fmi.mjt.boardgames.BoardGame.IntermediateBuilder.RequiringMinAge;
import static bg.sofia.uni.fmi.mjt.boardgames.BoardGame.IntermediateBuilder.RequiringMinPlayers;
import static bg.sofia.uni.fmi.mjt.boardgames.BoardGame.IntermediateBuilder.RequiringName;
import static bg.sofia.uni.fmi.mjt.boardgames.BoardGame.IntermediateBuilder.RequiringPlayingTimeMins;
import static bg.sofia.uni.fmi.mjt.boardgames.BoardGame.IntermediateBuilder.BoardGameBuilder;

public record BoardGame(int id, String name, String description, int maxPlayers, int minAge, int minPlayers,
                        int playingTimeMins, Collection<String> categories, Collection<String> mechanics) {

    public static RequiringId with() {
        return new FluentBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardGame game = (BoardGame) o;

        return id == game.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    private static final class FluentBuilder
        implements RequiringId, RequiringName, RequiringDescription, RequiringMaxPlayers, RequiringMinAge,
        RequiringMinPlayers, RequiringPlayingTimeMins, RequiringCategories, RequiringMechanics,
        BoardGameBuilder {

        private int id;
        private String name;
        private String description;
        private int maxPlayers;
        private int minAge;
        private int minPlayers;
        private int playingTimeMins;
        private Collection<String> categories;
        private Collection<String> mechanics;

        @Override
        public RequiringName id(int id) {
            this.id = id;
            return this;
        }

        @Override
        public RequiringDescription name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public RequiringMaxPlayers description(String description) {
            this.description = description;
            return this;
        }

        @Override
        public RequiringMinAge maxPlayers(int maxPlayers) {
            this.maxPlayers = maxPlayers;
            return this;
        }

        @Override
        public RequiringMinPlayers minAge(int minAge) {
            this.minAge = minAge;
            return this;
        }

        @Override
        public RequiringPlayingTimeMins minPlayers(int minPlayers) {
            this.minPlayers = minPlayers;
            return this;
        }

        @Override
        public RequiringCategories playingTimeMins(int playingTimeMins) {
            this.playingTimeMins = playingTimeMins;
            return this;
        }

        @Override
        public RequiringMechanics categories(Collection<String> categories) {
            this.categories = categories;
            return this;
        }

        @Override
        public BoardGameBuilder mechanics(Collection<String> mechanics) {
            this.mechanics = mechanics;
            return this;
        }

        @Override
        public BoardGame build() {
            return new BoardGame(id, name, description, maxPlayers, minAge, minPlayers,
                playingTimeMins, categories, mechanics);
        }
    }

    public static final class IntermediateBuilder {

        private IntermediateBuilder() {
        }

        public sealed interface RequiringId permits FluentBuilder {
            RequiringName id(int id);
        }

        public sealed interface RequiringName permits FluentBuilder {
            RequiringDescription name(String name);
        }

        public sealed interface RequiringDescription permits FluentBuilder {
            RequiringMaxPlayers description(String description);
        }

        public sealed interface RequiringMaxPlayers permits FluentBuilder {
            RequiringMinAge maxPlayers(int maxPlayers);
        }

        public sealed interface RequiringMinAge permits FluentBuilder {
            RequiringMinPlayers minAge(int minAge);
        }

        public sealed interface RequiringMinPlayers permits FluentBuilder {
            RequiringPlayingTimeMins minPlayers(int minPlayers);
        }

        public sealed interface RequiringPlayingTimeMins permits FluentBuilder {
            RequiringCategories playingTimeMins(int playingTimeMins);
        }

        public sealed interface RequiringCategories permits FluentBuilder {
            RequiringMechanics categories(Collection<String> categories);
        }

        public sealed interface RequiringMechanics permits FluentBuilder {
            BoardGameBuilder mechanics(Collection<String> mechanics);
        }

        public sealed interface BoardGameBuilder permits FluentBuilder {
            BoardGame build();
        }
    }

}

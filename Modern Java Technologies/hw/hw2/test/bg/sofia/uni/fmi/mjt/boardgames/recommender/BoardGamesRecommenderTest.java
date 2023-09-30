package bg.sofia.uni.fmi.mjt.boardgames.recommender;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import bg.sofia.uni.fmi.mjt.boardgames.recommender.exceptions.RecommenderException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class BoardGamesRecommenderTest {

    static final String DATA_TEST_ZIP_NAME = "81509_data.zip";
    static final String DATA_CSV_NAME = "data.csv";
    static final String STOPWORDS_TEST_FILE_NAME = "81509_stopwords.txt";

    static final String DATA_TEST_ZIP_NAME_WITH_BOM = "81509_data_BOM.zip";
    static final String DATA_CSV_NAME_WITH_BOM = "data_BOM.csv";
    static final String STOPWORDS_TEST_FILE_NAME_WITH_BOM = "81509_stopwords_BOM.txt";

    static final String BOM = "\uFEFF";

    @BeforeAll
    static void setUp() throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(DATA_TEST_ZIP_NAME));
        out.putNextEntry(new ZipEntry(DATA_CSV_NAME));
        out.write(getBytesForZipArchiving());
        out.closeEntry();
        out.flush();
        out.close();

        ZipOutputStream outWithBom = new ZipOutputStream(new FileOutputStream(DATA_TEST_ZIP_NAME_WITH_BOM));
        outWithBom.putNextEntry(new ZipEntry(DATA_CSV_NAME_WITH_BOM));
        outWithBom.write(BOM.getBytes(StandardCharsets.UTF_8));
        outWithBom.write(getBytesForZipArchiving());
        outWithBom.closeEntry();
        outWithBom.flush();
        outWithBom.close();

        Files.createFile(Path.of(STOPWORDS_TEST_FILE_NAME));
        Files.writeString(Path.of(STOPWORDS_TEST_FILE_NAME), getStopwordsStringContent());

        Files.createFile(Path.of(STOPWORDS_TEST_FILE_NAME_WITH_BOM));
        Files.writeString(Path.of(STOPWORDS_TEST_FILE_NAME_WITH_BOM), BOM + getStopwordsStringContent());
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.delete(Path.of(DATA_TEST_ZIP_NAME));
        Files.delete(Path.of(STOPWORDS_TEST_FILE_NAME));
        Files.delete(Path.of(DATA_TEST_ZIP_NAME_WITH_BOM));
        Files.delete(Path.of(STOPWORDS_TEST_FILE_NAME_WITH_BOM));
    }

    @Test
    void constructsSuccessfullyFromZip() {
        assertDoesNotThrow(() -> new BoardGamesRecommender(Path.of(DATA_TEST_ZIP_NAME), DATA_CSV_NAME, Path.of(STOPWORDS_TEST_FILE_NAME)));
    }

    @Test
    void constructsSuccessfullyFromZipWithCSVFileContainingBOM() {
        assertDoesNotThrow(() -> new BoardGamesRecommender(Path.of(DATA_TEST_ZIP_NAME_WITH_BOM), DATA_CSV_NAME_WITH_BOM, Path.of(STOPWORDS_TEST_FILE_NAME)));
    }

    @Test
    void constructsSuccessfullyFromZipWithStopwordsFileContainingBOM() {
        assertDoesNotThrow(() -> new BoardGamesRecommender(Path.of(DATA_TEST_ZIP_NAME), DATA_CSV_NAME, Path.of(STOPWORDS_TEST_FILE_NAME_WITH_BOM)));
    }


    @Test
    void givenPathConstructorIsInvoked_whenConstructorArgumentsAreNull_thenThrow() {
        assertThrows(IllegalArgumentException.class, () -> new BoardGamesRecommender(null, DATA_CSV_NAME, Path.of(STOPWORDS_TEST_FILE_NAME)));
        assertThrows(IllegalArgumentException.class, () -> new BoardGamesRecommender(Path.of(DATA_TEST_ZIP_NAME), null, Path.of(STOPWORDS_TEST_FILE_NAME)));
        assertThrows(IllegalArgumentException.class, () -> new BoardGamesRecommender(Path.of(DATA_TEST_ZIP_NAME), DATA_CSV_NAME, null));
    }

    @Test
    void givenReaderConstructorIsInvoked_whenConstructorArgumentsAreNull_thenThrow() {
        assertThrows(IllegalArgumentException.class, () -> new BoardGamesRecommender(null, createStopwordsDummyReader()));
        assertThrows(IllegalArgumentException.class, () -> new BoardGamesRecommender(createDatasetDummyReader(), null));
    }

    @Test
    void givenPathConstructorIsInvoked_whenZipFileIsMissing_thenThrow() {
        assertThrows(RecommenderException.class,
                     () -> new BoardGamesRecommender(Path.of("Missing.zip"), DATA_TEST_ZIP_NAME, Path.of(STOPWORDS_TEST_FILE_NAME)));
    }

    @Test
    void givenPathConstructorIsInvoked_whenStopwordsFileIsMissing_thenThrow() {
        assertThrows(RecommenderException.class,
                     () -> new BoardGamesRecommender(Path.of(DATA_TEST_ZIP_NAME), DATA_CSV_NAME, Path.of("Missing.txt")));
    }

    @Test
    void givenPathConstructorIsInvoked_whenFileNameIsEmpty_thenThrow() {
        assertThrows(IllegalArgumentException.class,
                     () -> new BoardGamesRecommender(Path.of(DATA_TEST_ZIP_NAME), "", Path.of(STOPWORDS_TEST_FILE_NAME)));
    }

    @Test
    void givenPathConstructorIsInvoked_whenCSVFileIsMissingFromZip_thenThrow() {
        assertThrows(RecommenderException.class,
            () -> new BoardGamesRecommender(Path.of(DATA_TEST_ZIP_NAME), "Missing.csv", Path.of(STOPWORDS_TEST_FILE_NAME)));
    }

    @Test
    void getGames_returnsUnmodifiableView() {
        BoardGamesRecommender recommender = new BoardGamesRecommender(createDatasetDummyReader(), createStopwordsDummyReader());
        var games = recommender.getGames();

        BoardGame game = BoardGame.with()
                                  .id(3)
                                  .name("The Game")
                                  .description("Desc")
                                  .maxPlayers(4)
                                  .minAge(12)
                                  .minPlayers(3)
                                  .playingTimeMins(30)
                                  .categories(List.of("Card Game", "Fantasy"))
                                  .mechanics(List.of("Trick-taking"))
                                  .build();

        assertThrows(Exception.class, () -> games.add(game));
    }

    @Test
    void getSimilarTo_worksCorrectly() {
        BoardGamesRecommender recommender = new BoardGamesRecommender(createDatasetDummyReader(), createStopwordsDummyReader());
        BoardGame game = BoardGame.with()
                                  .id(2)
                                  .name("Dragonmaster")
                                  .description("Dragonmaster is a trick-taking card game based on an older game called Coup d'etat. Each player is given a supply of plastic gems, which represent points. Each player will get to be the dealer for five different hands, with slightly different goals for each hand. After all cards have been dealt out, the dealer decides which hand best suits his or her current cards, and the other players are penalized points (in the form of crystals) for taking certain tricks or cards. For instance, if &quot first&quot  or &quot last&quot  is called, then a player is penalized for taking the first or last tricks. All players will get a chance to be dealer for five hands, but other players can steal this opportunity by taking all of the tricks during certain hands. At the end, the biggest pile of gems wins the game...Jewel contents:..11 clear (3 extra).13 green (1 extra).22 red (2 extra).22 blue (2 extra)..")
                                  .maxPlayers(4)
                                  .minAge(12)
                                  .minPlayers(3)
                                  .playingTimeMins(30)
                                  .categories(List.of("Card Game", "Fantasy"))
                                  .mechanics(List.of("Trick-taking"))
                                  .build();

        var result = recommender.getSimilarTo(game, 99);

        assertEquals(11, result.get(0).id());
        assertEquals(10, result.get(1).id());
        assertEquals(8, result.get(2).id());
    }

    @Test
    void getSimilarTo_whenGameIsNull_thenThrow() {
        BoardGamesRecommender recommender = new BoardGamesRecommender(createDatasetDummyReader(), createStopwordsDummyReader());

        assertThrows(IllegalArgumentException.class, () -> recommender.getSimilarTo(null, 99));
    }

    @Test
    void getSimilarTo_whenNumberIsNegative_thenThrow() {
        BoardGamesRecommender recommender = new BoardGamesRecommender(createDatasetDummyReader(), createStopwordsDummyReader());

        BoardGame game = BoardGame.with()
                                  .id(2)
                                  .name("Dragonmaster")
                                  .description("Dragonmaster is a trick-taking card game based on an older game called Coup d'etat. Each player is given a supply of plastic gems, which represent points. Each player will get to be the dealer for five different hands, with slightly different goals for each hand. After all cards have been dealt out, the dealer decides which hand best suits his or her current cards, and the other players are penalized points (in the form of crystals) for taking certain tricks or cards. For instance, if &quot first&quot  or &quot last&quot  is called, then a player is penalized for taking the first or last tricks. All players will get a chance to be dealer for five hands, but other players can steal this opportunity by taking all of the tricks during certain hands. At the end, the biggest pile of gems wins the game...Jewel contents:..11 clear (3 extra).13 green (1 extra).22 red (2 extra).22 blue (2 extra)..")
                                  .maxPlayers(4)
                                  .minAge(12)
                                  .minPlayers(3)
                                  .playingTimeMins(30)
                                  .categories(List.of("Card Game", "Fantasy"))
                                  .mechanics(List.of("Trick-taking"))
                                  .build();

        assertThrows(IllegalArgumentException.class, () -> recommender.getSimilarTo(game, -1));
    }

    @Test
    void getSimilarTo_whenNumberIsZero_thenReturnEmptyList() {
        BoardGamesRecommender recommender = new BoardGamesRecommender(createDatasetDummyReader(), createStopwordsDummyReader());

        BoardGame game = BoardGame.with()
                                  .id(2)
                                  .name("Dragonmaster")
                                  .description("Dragonmaster is a trick-taking card game based on an older game called Coup d'etat. Each player is given a supply of plastic gems, which represent points. Each player will get to be the dealer for five different hands, with slightly different goals for each hand. After all cards have been dealt out, the dealer decides which hand best suits his or her current cards, and the other players are penalized points (in the form of crystals) for taking certain tricks or cards. For instance, if &quot first&quot  or &quot last&quot  is called, then a player is penalized for taking the first or last tricks. All players will get a chance to be dealer for five hands, but other players can steal this opportunity by taking all of the tricks during certain hands. At the end, the biggest pile of gems wins the game...Jewel contents:..11 clear (3 extra).13 green (1 extra).22 red (2 extra).22 blue (2 extra)..")
                                  .maxPlayers(4)
                                  .minAge(12)
                                  .minPlayers(3)
                                  .playingTimeMins(30)
                                  .categories(List.of("Card Game", "Fantasy"))
                                  .mechanics(List.of("Trick-taking"))
                                  .build();

        assertEquals(0, recommender.getSimilarTo(game, 0).size());
    }

    @Test
    void getByDescription_whenGameContainsSameKeywordMultipleTimes_thenItIsIgnored() {
        BoardGamesRecommender recommender =
            new BoardGamesRecommender(createDatasetDummyReaderWithSimplerDataForDublicateKeywordsInGamesDescriptionTesting(),
                                      createStopwordsDummyReader());

        var result = recommender.getByDescription("keyword1", "keyword2");

        assertEquals(2, result.get(0).id());
    }

    @Test
    void getByDescription_ordersCorrectly() {
        BoardGamesRecommender recommender = new BoardGamesRecommender(createDatasetDummyReaderWithSimplerData(),
                                                                      createStopwordsDummyReader());
        var result = recommender.getByDescription("keyword1", "keyword2", "keyword3", "keyword4", "keyword5", "keyword6");

        assertEquals(6, result.get(0).id());
        assertEquals(5, result.get(1).id());
        assertEquals(4, result.get(2).id());
        assertEquals(3, result.get(3).id());
        assertEquals(2, result.get(4).id());
        assertEquals(1, result.get(5).id());
    }

    @Test
    void getByDescription_whenThereAreRepeatingWords_thenTheyAreIgnored() {
        var recommender = new BoardGamesRecommender(createDatasetDummyReaderWithSimplerDataForDublicateKeywordsTesting(),
                                                    createStopwordsDummyReader());
        var result = recommender.getByDescription("keyword4", "keyword6", "keyword2", "keyword2", "keyword2", "keyword2");

        assertEquals(6, result.get(0).id());
    }

    @Test
    void getByDescription_whenArrayIsNull_thenThrow() {
        var recommender = new BoardGamesRecommender(createDatasetDummyReaderWithSimplerData(), createStopwordsDummyReader());
        assertThrows(IllegalArgumentException.class, ()-> recommender.getByDescription((String) null));
    }

    @Test
    void getByDescription_whenArrayContainsNull_thenThrow() {
        var recommender = new BoardGamesRecommender(createDatasetDummyReaderWithSimplerData(), createStopwordsDummyReader());
        assertThrows(IllegalArgumentException.class,
                     ()-> recommender.getByDescription("keyword1", null, "keyword3", "keyword4", "keyword5", "keyword6"));
    }

    @Test
    void storeGamesIndex_worksCorrectly() {
        var recommender = new BoardGamesRecommender(createDatasetDummyReaderWithSimplerData(), createStopwordsDummyReader());
        StringWriter writer = new StringWriter();
        recommender.storeGamesIndex(writer);

        var actualLines = writer.toString().lines().toList();
        var expectedLines = Set.of(
            "keyword5: 5, 6",
            "keyword6: 6",
            "keyword3: 3, 4, 5, 6",
            "keyword4: 4, 5, 6",
            "keyword1: 1, 2, 3, 4, 5, 6",
            "keyword2: 2, 3, 4, 5, 6"
        );

        assertTrue(
            actualLines.size() == expectedLines.size() &&
            actualLines.containsAll(expectedLines)
        );
    }

    @Test
    void storeGamesIndex_whenReaderIsNull_thenThrow() {
        BoardGamesRecommender recommender = new BoardGamesRecommender(createDatasetDummyReaderWithSimplerData(), createStopwordsDummyReader());

        assertThrows(IllegalArgumentException.class, ()-> recommender.storeGamesIndex(null));
    }

    private Reader createDatasetDummyReader() {
        return new StringReader(
            """
            id;details.maxplayers;details.minage;details.minplayers;details.name;details.playingtime;attributes.boardgamecategory;attributes.boardgamemechanic;details.description
            1;5;14;3;Die Macher;240;Economic,Negotiation,Political;Area Control / Area Influence,Auction/Bidding,Dice Rolling,Hand Management,Simultaneous Action Selection;Die Macher is a game about seven sequential political races in different regions of Germany. Players are in charge of national political parties, and must manage limited resources to help their party to victory. The winning party will have the most victory points after all the regional elections. There are four different ways of scoring victory points. First, each regional election can supply one to eighty victory points, depending on the size of the region and how well your party does in it. Second, if a party wins a regional election and has some media influence in the region, then the party will receive some media-control victory points. Third, each party has a national party membership which will grow as the game progresses and this will supply a fair number of victory points. Lastly, parties score some victory points if their party platform matches the national opinions at the end of the game...The 1986 edition featured 4 parties from the old West Germany and supported 3-4 players. The 1997 edition supports up to 5 players in the re-united Germany and updated several features of the rules as well.  The 2006 edition also supports up to 5 players and adds a shorter 5 round variant and additional rules updates by the original designer...Die Macher is #1 in the Valley Games Classic Line..
            2;4;12;3;Dragonmaster;30;Card Game,Fantasy;Trick-taking;Dragonmaster is a trick-taking card game based on an older game called Coup d'etat. Each player is given a supply of plastic gems, which represent points. Each player will get to be the dealer for five different hands, with slightly different goals for each hand. After all cards have been dealt out, the dealer decides which hand best suits his or her current cards, and the other players are penalized points (in the form of crystals) for taking certain tricks or cards. For instance, if &quot first&quot  or &quot last&quot  is called, then a player is penalized for taking the first or last tricks. All players will get a chance to be dealer for five hands, but other players can steal this opportunity by taking all of the tricks during certain hands. At the end, the biggest pile of gems wins the game...Jewel contents:..11 clear (3 extra).13 green (1 extra).22 red (2 extra).22 blue (2 extra)..
            3;4;10;2;Samurai;60;Abstract Strategy,Medieval;Area Control / Area Influence,Hand Management,Set Collection,Tile Placement;Part of the Knizia tile-laying trilogy, Samurai is set in medieval Japan. Players compete to gain the favor of three factions: samurai, peasants, and priests, which are represented by helmet, rice paddy, and Buddha tokens scattered about the board, which features the islands of Japan. The competition is waged through the use of hexagonal tiles, each of which help curry favor of one of the three factions &mdash  or all three at once! Players can make lightning-quick strikes with horseback ronin and ships or approach their conquests more methodically. As each token (helmets, rice paddies, and Buddhas) is surrounded, it is awarded to the player who has gained the most favor with the corresponding group...Gameplay continues until all the symbols of one type have been removed from the board or four tokens have been removed from play due to a tie for influence...At the end of the game, players compare captured symbols of each type, competing for majorities in each of the three types. Ties are not uncommon and are broken based on the number of other, &quot non-majority&quot  symbols each player has collected...
            4;4;12;2;Tal der Könige;60;Ancient;Action Point Allowance System,Area Control / Area Influence,Auction/Bidding,Set Collection;When you see the triangular box and the luxurious, large blocks, you can tell this game was designed to be beautiful as well as functional.  The object of the game is to build pyramids out of the different colored blocks.  A pyramid scores more points when it's made from a few colors, but it's much harder to consistently outbid the other players for the necessary blocks.  The game is over when the Pharoah's Pyramid in the center is completed, which is built using all the blocks that the players don't use during the course of the game...Final round 1990 Hippodice Spieleautorenwettbewerb...
            5;6;12;3;Acquire;90;Economic;Hand Management,Stock Holding,Tile Placement;In Acquire, each player strategically invests in businesses, trying to retain a majority of stock.  As the businesses grow with tile placements, they also start merging, giving the majority stockholders of the acquired business sizable bonuses, which can then be used to reinvest into other chains.  All of the investors in the acquired company can then cash in their stocks for current value or trade them 2-for-1 for shares of the newer, larger business.  The game is a race to acquire the greatest wealth...This Sid Sackson classic has taken many different forms over the years depending on the publisher.  Some versions of the 3M bookshelf edition included rules for a 2-player variant. The original version is part of the 3M Bookshelf Series...Note: many books and websites list this as a 1962 publication. This is incorrect  information from Sid Sackson's diaries, correspondence, and royalty statements prove that it was published in 1964.  However, for some reason admins continue to accept &quot corrections&quot  of the publication date to 1962.  A detailed timeline of the development and publication of the game can be found at https://opinionatedgamers.com/2014/05/29/how-acquire-became-acquire/, for those interested...
            6;6;12;2;Mare Mediterraneum;240;Civilization,Nautical;Dice Rolling;In the ancient lands along the Mediterranean, players attempt to satisfy their unique victory conditions via trade, war and construction.  This lavishly produced game contains tons of wooden game components and a beautiful roll-out vinyl map.  Players produce a score of different commodities to trade with other cities in the hope of creating enough income to fill their capitals with buildings, produce artwork, and fill warehouses with goods...
            7;2;8;2;Cathedral;20;Abstract Strategy;Area Enclosure,Pattern Building,Pattern Recognition,Tile Placement;In Cathedral, each player has a set of pieces of a different color. The pieces are in the shapes of buildings, covering from one to five square units. The first player takes the single neutral Cathedral piece and places it onto the board. Players then alternate placing one of their buildings onto the board until neither player can place another building. Players capture territory by surrounding areas that are occupied by at most one opponent or neutral building. A captured piece is removed and captured territory becomes off-limits to the opponent. The player with the fewest 'square units' of buildings that can't be placed wins...
            8;5;12;2;Lords of Creation;120;Civilization,Fantasy;Modular Board;In this interesting offering from Warfrog, players become Gods seeking to dominate a world with their followers.  The first part of the game involves constructing the game board, after which players take turns using cards to determine how many new people should appear and what type of terrain they will inhabit.  All tribes start off as barbarians, but soon the calming effect of civilization settles the marauding bands  although civilized tribes score more, they can no longer attack.  What they can do, though, is civilize other players' followers.  Sure it gives opponents more points, but at least those followers won't come after you!..
            9;4;13;2;El Caballero;90;Exploration;Area Control / Area Influence,Tile Placement;Although referred to as a sequel to El Grande, El Caballero shares few aspects with its namesake, being a fun but intense brain-burner in which players explore and attempt to control the lands and waterways of the New World...The players are following Columbus by exploring the islands he discovered. Players slowly explore the islands &ndash  by picking and placing land tiles that are most favorable to them &ndash  and discover wealth in the form of gold and fish. As they learn about the land and sea areas of this new land, they position their caballeros to try to maintain control of the important regions. Castillos give them a measure of protection from others, and ships allow them to establish trade and to fish for food. Success is measured in the size of land and sea areas they control. Their success is measured twice, and in the end these scores are summed and the winner declared...
            10;6;10;2;Elfenland;60;Fantasy,Travel;Card Drafting,Hand Management,Point to Point Movement,Route/Network Building;Elfenland is a redesign of the original White Wind game Elfenroads.  The game is set in the mythical world of the elves.  A group of fledgling elves (the players) are charged with visiting as many of the twenty Elfencities as they can over the course of 4 rounds.  To accomplish the task they will use various forms of transportation such as Giant Pigs, Elfcarts, Unicorns, Rafts, Magic Clouds, Trollwagons, and Dragons...Gameplay:  Players begin in the Elf capitol, draw one face down movement tile, and are dealt eight transport cards and a secret 'home' city card that they must reach at the end of the 4th round or lose points for each city space away from 'home' they are at the end of the game. Markers of each player's color are placed in each city on the board and are collected when the player visits that city (each counts as 1 point)..  .     The round proceeds in 2 stages.  The first part of the round consists of the drawing of Tiles showing the differing types of transport (except rafts) from a combination of face up and face down tiles (if a player doesn't like the 5 tiles that are face up  they can always draw blind from the face down tiles and hope to get one they need).  These transport tiles need to match the Transportation cards in your hand to use them most effectively.  After each player has a total of 4 tiles they take turns placing a tile on any one of the roads that run between the elf cities.  Only one transport tile may be placed on each road  so players may use other players tiles to travel if they have the matching cards in their hand.  This frequently causes a readjustment of planned travel routes as other players tiles can allow you to move farther or shorter than you had first thought.  Players can play their tiles to help themselves or hinder others by playing a slow mode of transport on another players (perceived) path...Each mode of transport has certain terrain it can travel through quickly or slowly, and those that it cannot. These are listed on the top of each transportation card by the number terrain symbols.  The number of terrain symbols equals how many matching cards you must play to move across a given tile in a given terrain.  For example, a Magic Cloud tile placed in a mountain would take one Magic cloud card to travel across (1 mountain symbol on card means Magic clouds are fast in mountains). If the same tile was placed on a road in forest terrain it would require 2 Magic Cloud cards to travel that route (2 Forest symbols on card means Magic Clouds are slow in Forest).  Magic Clouds cannot travel in desert terrain at all (no desert symbols on card). All modes of transport are different and Rafts can be used on rivers or lakes without needing tiles. Rafts go slow upstream (2 raft cards needed) and fast downstream (1 card needed).  The small lake requires 1 raft card to travel across and the larger lake requires 2 cards to travel across.  Players may keep one unused transport counter and up to 4 Transportation cards from one round to the next...The second part of the round begins after all players have finished placing their transportation tiles for the round.  Each player plays his cards and moves his elf-boot around the board collecting his tokens from the cities visited.  If there is a Transport tile on a route and a player has no matching Transportation card he may 'Caravan' across it by playing any 3 Transportation cards from his hand...As a bit of 'take that' each player has a trouble tile which can be placed next to any transportation tile during the first part of the round.  This counter means that in order to travel that path an additional card of the transport type must be played or 4 cards to 'Caravan'...Victory: if at the end of round 3 a player has visited all 20 cities he is the winner.  If not the game ends after round 4 when 'Home' cities are revealed and each player subtracts points for each city he is away from his 'home' subtracting that from his collected city tokens.  The person with the highest score wins...
            11;7;13;2;Bohnanza;45;Card Game,Farming,Negotiation;Hand Management,Set Collection,Trading;Bohnanza is the first in the Bohnanza family of games and has been published in several different editions...The cards are colorful depictions of beans in various descriptive poses, and the object is to make coins by planting fields (sets) of these beans and then harvesting them.  To help players match their cards up, the game features extensive trading and deal making.  One of the most unique features of the game is that you cannot rearrange your hand, as you need to play the cards in the order that you draw them...The original German edition supports 3-5 players...The English version from Rio Grande Games comes with the first edition of the first German expansion included in a slightly oversized box.  One difference in the contents, however, is that bean #22's Weinbrandbohne (Brandy Bean) was replaced by the Wachsbohne, or Wax Bean.  This edition includes rules for up to seven players, like the Erweiterungs-Set, but also adapts the two-player rules of Al Cabohne in order to allow two people to play Bohnanza...
            """
        );
    }

    private Reader createDatasetDummyReaderWithSimplerData() {
        return new StringReader(
            """
            id;details.maxplayers;details.minage;details.minplayers;details.name;details.playingtime;attributes.boardgamecategory;attributes.boardgamemechanic;details.description
            1;1;1;1;monomoly;1;board;board;keyword1
            2;1;1;1;monomoly;1;board;board;keyword1 keyword2
            3;1;1;1;monomoly;1;board;board;keyword1 keyword2 keyword3
            4;1;1;1;monomoly;1;board;board;keyword1 keyword2 keyword3 keyword4
            5;1;1;1;monomoly;1;board;board;keyword1 keyword2 keyword3 keyword4 keyword5
            6;1;1;1;monomoly;1;board;board;keyword1 keyword2 keyword3 keyword4 keyword5 keyword6
            """
        );
    }

    private Reader createDatasetDummyReaderWithSimplerDataForDublicateKeywordsTesting() {
        return new StringReader(
            """
            id;details.maxplayers;details.minage;details.minplayers;details.name;details.playingtime;attributes.boardgamecategory;attributes.boardgamemechanic;details.description
            1;1;1;1;monomoly;1;board;board;keyword1
            2;1;1;1;monomoly;1;board;board;keyword1 keyword2
            3;1;1;1;monomoly;1;board;board;keyword1 keyword3
            4;1;1;1;monomoly;1;board;board;keyword1 keyword3 keyword4
            5;1;1;1;monomoly;1;board;board;keyword1 keyword3 keyword4 keyword5
            6;1;1;1;monomoly;1;board;board;keyword1 keyword3 keyword4 keyword5 keyword6
            """
        );
    }

    private Reader createDatasetDummyReaderWithSimplerDataForDublicateKeywordsInGamesDescriptionTesting() {
        return new StringReader(
            """
            id;details.maxplayers;details.minage;details.minplayers;details.name;details.playingtime;attributes.boardgamecategory;attributes.boardgamemechanic;details.description
            1;1;1;1;monomoly;1;board;board;keyword2 keyword2 keyword2 keyword5
            2;1;1;1;monomoly;1;board;board;keyword1 keyword2
            3;1;1;1;monomoly;1;board;board;keyword1 keyword1 keyword1 keyword1 keyword1 keyword1 keyword1 keyword1 keyword4
            """
        );
    }

    private Reader createStopwordsDummyReader() {
        return new StringReader(getStopwordsStringContent());
    }

    private static String getStopwordsStringContent() {
            return """
            a
            about
            above
            after
            again
            against
            all
            am
            an
            and
            any
            are
            aren't
            as
            at
            be
            because
            been
            before
            being
            below
            between
            both
            but
            by
            can't
            cannot
            could
            couldn't
            did
            didn't
            do
            does
            doesn't
            doing
            don't
            down
            during
            each
            few
            for
            from
            further
            had
            hadn't
            has
            hasn't
            have
            haven't
            having
            he
            he'd
            he'll
            he's
            her
            here
            here's
            hers
            herself
            him
            himself
            his
            how
            how's
            i
            i'd
            i'll
            i'm
            i've
            if
            in
            into
            is
            isn't
            it
            it's
            its
            itself
            let's
            me
            more
            most
            mustn't
            my
            myself
            no
            nor
            not
            of
            off
            on
            once
            only
            or
            other
            ought
            our
            ours
            ourselves
            out
            over
            own
            same
            shan't
            she
            she'd
            she'll
            she's
            should
            shouldn't
            so
            some
            such
            than
            that
            that's
            the
            their
            theirs
            them
            themselves
            then
            there
            there's
            these
            they
            they'd
            they'll
            they're
            they've
            this
            those
            through
            to
            too
            under
            until
            up
            very
            was
            wasn't
            we
            we'd
            we'll
            we're
            we've
            were
            weren't
            what
            what's
            when
            when's
            where
            where's
            which
            while
            who
            who's
            whom
            why
            why's
            with
            won't
            would
            wouldn't
            you
            you'd
            you'll
            you're
            you've
            your
            yours
            yourself
            yourselves
            """;
    }

    private static byte[] getBytesForZipArchiving() {
       return  """
        id;details.maxplayers;details.minage;details.minplayers;details.name;details.playingtime;attributes.boardgamecategory;attributes.boardgamemechanic;details.description
        1;5;14;3;Die Macher;240;Economic,Negotiation,Political;Area Control / Area Influence,Auction/Bidding,Dice Rolling,Hand Management,Simultaneous Action Selection;Die Macher is a game about seven sequential political races in different regions of Germany. Players are in charge of national political parties, and must manage limited resources to help their party to victory. The winning party will have the most victory points after all the regional elections. There are four different ways of scoring victory points. First, each regional election can supply one to eighty victory points, depending on the size of the region and how well your party does in it. Second, if a party wins a regional election and has some media influence in the region, then the party will receive some media-control victory points. Third, each party has a national party membership which will grow as the game progresses and this will supply a fair number of victory points. Lastly, parties score some victory points if their party platform matches the national opinions at the end of the game...The 1986 edition featured 4 parties from the old West Germany and supported 3-4 players. The 1997 edition supports up to 5 players in the re-united Germany and updated several features of the rules as well.  The 2006 edition also supports up to 5 players and adds a shorter 5 round variant and additional rules updates by the original designer...Die Macher is #1 in the Valley Games Classic Line..
        2;4;12;3;Dragonmaster;30;Card Game,Fantasy;Trick-taking;Dragonmaster is a trick-taking card game based on an older game called Coup d'etat. Each player is given a supply of plastic gems, which represent points. Each player will get to be the dealer for five different hands, with slightly different goals for each hand. After all cards have been dealt out, the dealer decides which hand best suits his or her current cards, and the other players are penalized points (in the form of crystals) for taking certain tricks or cards. For instance, if &quot first&quot  or &quot last&quot  is called, then a player is penalized for taking the first or last tricks. All players will get a chance to be dealer for five hands, but other players can steal this opportunity by taking all of the tricks during certain hands. At the end, the biggest pile of gems wins the game...Jewel contents:..11 clear (3 extra).13 green (1 extra).22 red (2 extra).22 blue (2 extra)..
        3;4;10;2;Samurai;60;Abstract Strategy,Medieval;Area Control / Area Influence,Hand Management,Set Collection,Tile Placement;Part of the Knizia tile-laying trilogy, Samurai is set in medieval Japan. Players compete to gain the favor of three factions: samurai, peasants, and priests, which are represented by helmet, rice paddy, and Buddha tokens scattered about the board, which features the islands of Japan. The competition is waged through the use of hexagonal tiles, each of which help curry favor of one of the three factions &mdash  or all three at once! Players can make lightning-quick strikes with horseback ronin and ships or approach their conquests more methodically. As each token (helmets, rice paddies, and Buddhas) is surrounded, it is awarded to the player who has gained the most favor with the corresponding group...Gameplay continues until all the symbols of one type have been removed from the board or four tokens have been removed from play due to a tie for influence...At the end of the game, players compare captured symbols of each type, competing for majorities in each of the three types. Ties are not uncommon and are broken based on the number of other, &quot non-majority&quot  symbols each player has collected...
        4;4;12;2;Tal der Könige;60;Ancient;Action Point Allowance System,Area Control / Area Influence,Auction/Bidding,Set Collection;When you see the triangular box and the luxurious, large blocks, you can tell this game was designed to be beautiful as well as functional.  The object of the game is to build pyramids out of the different colored blocks.  A pyramid scores more points when it's made from a few colors, but it's much harder to consistently outbid the other players for the necessary blocks.  The game is over when the Pharoah's Pyramid in the center is completed, which is built using all the blocks that the players don't use during the course of the game...Final round 1990 Hippodice Spieleautorenwettbewerb...
        5;6;12;3;Acquire;90;Economic;Hand Management,Stock Holding,Tile Placement;In Acquire, each player strategically invests in businesses, trying to retain a majority of stock.  As the businesses grow with tile placements, they also start merging, giving the majority stockholders of the acquired business sizable bonuses, which can then be used to reinvest into other chains.  All of the investors in the acquired company can then cash in their stocks for current value or trade them 2-for-1 for shares of the newer, larger business.  The game is a race to acquire the greatest wealth...This Sid Sackson classic has taken many different forms over the years depending on the publisher.  Some versions of the 3M bookshelf edition included rules for a 2-player variant. The original version is part of the 3M Bookshelf Series...Note: many books and websites list this as a 1962 publication. This is incorrect  information from Sid Sackson's diaries, correspondence, and royalty statements prove that it was published in 1964.  However, for some reason admins continue to accept &quot corrections&quot  of the publication date to 1962.  A detailed timeline of the development and publication of the game can be found at https://opinionatedgamers.com/2014/05/29/how-acquire-became-acquire/, for those interested...
        6;6;12;2;Mare Mediterraneum;240;Civilization,Nautical;Dice Rolling;In the ancient lands along the Mediterranean, players attempt to satisfy their unique victory conditions via trade, war and construction.  This lavishly produced game contains tons of wooden game components and a beautiful roll-out vinyl map.  Players produce a score of different commodities to trade with other cities in the hope of creating enough income to fill their capitals with buildings, produce artwork, and fill warehouses with goods...
        7;2;8;2;Cathedral;20;Abstract Strategy;Area Enclosure,Pattern Building,Pattern Recognition,Tile Placement;In Cathedral, each player has a set of pieces of a different color. The pieces are in the shapes of buildings, covering from one to five square units. The first player takes the single neutral Cathedral piece and places it onto the board. Players then alternate placing one of their buildings onto the board until neither player can place another building. Players capture territory by surrounding areas that are occupied by at most one opponent or neutral building. A captured piece is removed and captured territory becomes off-limits to the opponent. The player with the fewest 'square units' of buildings that can't be placed wins...
        8;5;12;2;Lords of Creation;120;Civilization,Fantasy;Modular Board;In this interesting offering from Warfrog, players become Gods seeking to dominate a world with their followers.  The first part of the game involves constructing the game board, after which players take turns using cards to determine how many new people should appear and what type of terrain they will inhabit.  All tribes start off as barbarians, but soon the calming effect of civilization settles the marauding bands  although civilized tribes score more, they can no longer attack.  What they can do, though, is civilize other players' followers.  Sure it gives opponents more points, but at least those followers won't come after you!..
        9;4;13;2;El Caballero;90;Exploration;Area Control / Area Influence,Tile Placement;Although referred to as a sequel to El Grande, El Caballero shares few aspects with its namesake, being a fun but intense brain-burner in which players explore and attempt to control the lands and waterways of the New World...The players are following Columbus by exploring the islands he discovered. Players slowly explore the islands &ndash  by picking and placing land tiles that are most favorable to them &ndash  and discover wealth in the form of gold and fish. As they learn about the land and sea areas of this new land, they position their caballeros to try to maintain control of the important regions. Castillos give them a measure of protection from others, and ships allow them to establish trade and to fish for food. Success is measured in the size of land and sea areas they control. Their success is measured twice, and in the end these scores are summed and the winner declared...
        10;6;10;2;Elfenland;60;Fantasy,Travel;Card Drafting,Hand Management,Point to Point Movement,Route/Network Building;Elfenland is a redesign of the original White Wind game Elfenroads.  The game is set in the mythical world of the elves.  A group of fledgling elves (the players) are charged with visiting as many of the twenty Elfencities as they can over the course of 4 rounds.  To accomplish the task they will use various forms of transportation such as Giant Pigs, Elfcarts, Unicorns, Rafts, Magic Clouds, Trollwagons, and Dragons...Gameplay:  Players begin in the Elf capitol, draw one face down movement tile, and are dealt eight transport cards and a secret 'home' city card that they must reach at the end of the 4th round or lose points for each city space away from 'home' they are at the end of the game. Markers of each player's color are placed in each city on the board and are collected when the player visits that city (each counts as 1 point)..  .     The round proceeds in 2 stages.  The first part of the round consists of the drawing of Tiles showing the differing types of transport (except rafts) from a combination of face up and face down tiles (if a player doesn't like the 5 tiles that are face up  they can always draw blind from the face down tiles and hope to get one they need).  These transport tiles need to match the Transportation cards in your hand to use them most effectively.  After each player has a total of 4 tiles they take turns placing a tile on any one of the roads that run between the elf cities.  Only one transport tile may be placed on each road  so players may use other players tiles to travel if they have the matching cards in their hand.  This frequently causes a readjustment of planned travel routes as other players tiles can allow you to move farther or shorter than you had first thought.  Players can play their tiles to help themselves or hinder others by playing a slow mode of transport on another players (perceived) path...Each mode of transport has certain terrain it can travel through quickly or slowly, and those that it cannot. These are listed on the top of each transportation card by the number terrain symbols.  The number of terrain symbols equals how many matching cards you must play to move across a given tile in a given terrain.  For example, a Magic Cloud tile placed in a mountain would take one Magic cloud card to travel across (1 mountain symbol on card means Magic clouds are fast in mountains). If the same tile was placed on a road in forest terrain it would require 2 Magic Cloud cards to travel that route (2 Forest symbols on card means Magic Clouds are slow in Forest).  Magic Clouds cannot travel in desert terrain at all (no desert symbols on card). All modes of transport are different and Rafts can be used on rivers or lakes without needing tiles. Rafts go slow upstream (2 raft cards needed) and fast downstream (1 card needed).  The small lake requires 1 raft card to travel across and the larger lake requires 2 cards to travel across.  Players may keep one unused transport counter and up to 4 Transportation cards from one round to the next...The second part of the round begins after all players have finished placing their transportation tiles for the round.  Each player plays his cards and moves his elf-boot around the board collecting his tokens from the cities visited.  If there is a Transport tile on a route and a player has no matching Transportation card he may 'Caravan' across it by playing any 3 Transportation cards from his hand...As a bit of 'take that' each player has a trouble tile which can be placed next to any transportation tile during the first part of the round.  This counter means that in order to travel that path an additional card of the transport type must be played or 4 cards to 'Caravan'...Victory: if at the end of round 3 a player has visited all 20 cities he is the winner.  If not the game ends after round 4 when 'Home' cities are revealed and each player subtracts points for each city he is away from his 'home' subtracting that from his collected city tokens.  The person with the highest score wins...
        11;7;13;2;Bohnanza;45;Card Game,Farming,Negotiation;Hand Management,Set Collection,Trading;Bohnanza is the first in the Bohnanza family of games and has been published in several different editions...The cards are colorful depictions of beans in various descriptive poses, and the object is to make coins by planting fields (sets) of these beans and then harvesting them.  To help players match their cards up, the game features extensive trading and deal making.  One of the most unique features of the game is that you cannot rearrange your hand, as you need to play the cards in the order that you draw them...The original German edition supports 3-5 players...The English version from Rio Grande Games comes with the first edition of the first German expansion included in a slightly oversized box.  One difference in the contents, however, is that bean #22's Weinbrandbohne (Brandy Bean) was replaced by the Wachsbohne, or Wax Bean.  This edition includes rules for up to seven players, like the Erweiterungs-Set, but also adapts the two-player rules of Al Cabohne in order to allow two people to play Bohnanza...
        12;5;12;2;Ra;60;Ancient,Mythology;Auction/Bidding,Press Your Luck,Set Collection;Ra is an auction and set-collection game with an Ancient Egyptian theme.  Each turn players are able to purchase lots of tiles with their bidding tiles (suns).  Once a player has used up his or her suns, the other players continue until they do likewise, which may set up a situation with a single uncontested player bidding on tiles before the end of the round occurs.  Tension builds because the round may end before all players have had a chance to win their three lots for the epoch.  The various tiles either give immediate points, prevent negative points for not having certain types at the end of the round (epoch), or give points after the final round.  The game lasts for three &quot epochs&quot  (rounds).  The game offers a short learning curve, and experienced players find it both fast-moving and a quick play...From the Box:.The game spans 1500 years of Egyptian history in less than an hour!.The players seek to expand their power and fame and there are many ways to accomplish this: Influencing Pharaohs, Building monuments, Farming on the Nile, Paying homage to the Gods, Advancing the technology and culture of the people. Ra is an auction and set collecting game where players may choose to take risks for great rewards or... And all this is for the glory of the Sun God Ra!..
        13;4;10;3;Catan;120;Negotiation;Dice Rolling,Hand Management,Modular Board,Route/Network Building,Trading;In Catan (formerly The Settlers of Catan), players try to be the dominant force on the island of Catan by building settlements, cities, and roads. On each turn dice are rolled to determine what resources the island produces. Players collect these resources (cards)&mdash wood, grain, brick, sheep, or stone&mdash to build up their civilizations to get to 10 victory points and win the game...Setup includes randomly placing large hexagonal tiles (each showing a resource or the desert) in a honeycomb shape and surrounding them with water tiles, some of which contain ports of exchange. Number disks, which will correspond to die rolls (two 6-sided dice are used), are placed on each resource tile. Each player is given two settlements (think: houses) and roads (sticks) which are, in turn, placed on intersections and borders of the resource tiles. Players collect a hand of resource cards based on which hex tiles their last-placed house is adjacent to. A robber pawn is placed on the desert tile...A turn consists of possibly playing a development card, rolling the dice, everyone (perhaps) collecting resource cards based on the roll and position of houses (or upgraded cities&mdash think: hotels) unless a 7 is rolled, turning in resource cards (if possible and desired) for improvements, trading cards at a port, and trading resource cards with other players. If a 7 is rolled, the active player moves the robber to a new hex tile and steals resource cards from other players who have built structures adjacent to that tile...Points are accumulated by building settlements and cities, having the longest road and the largest army (from some of the development cards), and gathering certain development cards that simply award victory points. When a player has gathered 10 points (some of which may be held in secret), he announces his total and claims the win...Catan has won multiple awards and is one of the most popular games in recent history due to its amazing ability to appeal to experienced gamers as well as those new to the hobby...Die Siedler von Catan was originally published by KOSMOS and has gone through multiple editions. It was licensed by Mayfair and has undergone four editions as The Settlers of Catan. In 2015, it was formally renamed Catan to better represent itself as the core and base game of the Catan series. It has been re-published in two travel editions, portable edition and compact edition, as a special gallery edition (replaced in 2009 with a family edition), as an anniversary wooden edition, as a deluxe 3D collector's edition, in the basic Simply Catan, as a beginner version, and with an entirely new theme in Japan and Asia as Settlers of Catan: Rockman Edition. Numerous spin-offs and expansions have also been made for the game...
        14;4;10;3;Basari;25;Negotiation;Roll / Spin and Move,Set Collection,Simultaneous Action Selection;Basari is a game of gem merchants competing in a marketplace&quot : racing, collecting, trading, and predicting what the other merchants will do...To start the game, players receive 12 gemstones (3 each of 4 colors). .Each turn there will be a movement phase and an action phase:..For movement, everyone simultaneously rolls their die and moves their markers around a track...They then choose one of three different actions to perform. The actions are:.- to take a variety of gems from a market stall, .- to immediately score from between 4 and 7 points, or .- to roll and move again and also collect points...The action cards are revealed simultaneously. If only 1 player chose a particular action, they simply perform that action. If 2 players chose the same action, they barter back and forth with gemstones. One player will eventually accept the other's offer of gems, and the other will take the action. If 3 or 4 players all chose the same action card, that action is cancelled. Note that in 4-player games, there will always be a conflict in choice of 3 actions, so much more negotiating goes on...A new turn begins with everyone rolling their die, moving, then choosing and revealing their action cards. When any merchant piece completes a lap around the board, the round ends and bonus points are awarded based on who has the majority in each gem color and who has completed a lap. After three rounds, the game is over and the highest score wins...Re-implemented by:...     Edel, Stein & Reich...
        15;6;12;2;Cosmic Encounter;90;Bluffing,Negotiation,Science Fiction;Hand Management,Variable Player Powers;By request of Fantasy Flight Games, Board Game Geek lists the various editions of Cosmic Encounter in separate entries. Each edition is the same game at its core, but the features, compatible expansions, and production differ among them. This entry is for the original 1977 Eon version...Players represent alien races that are seeking to spread themselves onto five foreign worlds.  To accomplish this, they make challenges against other players and enlist the aid of interested parties.  But alien powers, which are unique to each race, give players ways to bend or outright break some rule in the game...The game continues until one player occupies five planets in other systems to win. Shared victories are possible and a player need not occupy one's own system to win...
        """.getBytes(StandardCharsets.UTF_8);
    }

}
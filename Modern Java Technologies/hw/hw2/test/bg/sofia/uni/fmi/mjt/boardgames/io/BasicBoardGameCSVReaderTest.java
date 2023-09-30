package bg.sofia.uni.fmi.mjt.boardgames.io;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import bg.sofia.uni.fmi.mjt.boardgames.io.exceptions.BadCSVFormatException;
import bg.sofia.uni.fmi.mjt.boardgames.io.exceptions.DataValidationException;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicBoardGameCSVReaderTest {

    @Test
    void whenCreatedWithIllegalObjectFormat_thenThrow() {
        assertThrows(
            DataValidationException.class,
            () -> new BasicBoardGameCSVReader(createIncompleteGameBoardCSVReader())
        );
    }

    @Test
    void read_whenCreatedWithIllegalFormat_thenThrow() {
        BasicBoardGameCSVReader csv = new BasicBoardGameCSVReader(createIllegalCSVReader());
        assertThrows(BadCSVFormatException.class, csv::read);
    }

    @Test
    void read_whenRead_thenCorrectGameBoardIsReturned() {
        BasicBoardGameCSVReader csv = new BasicBoardGameCSVReader(createDummyReader());
        BoardGame boardGame = csv.read();

        assertEquals(1, boardGame.id());
        assertEquals(5, boardGame.maxPlayers());
        assertEquals(14, boardGame.minAge());
        assertEquals(3, boardGame.minPlayers());
        assertEquals("Die Macher", boardGame.name());
        assertEquals(240, boardGame.playingTimeMins());

        assertIterableEquals(List.of("Economic","Negotiation","Political"), boardGame.categories());

        assertIterableEquals(
            boardGame.mechanics(),
            List.of("Area Control / Area Influence","Auction/Bidding","Dice Rolling","Hand Management","Simultaneous Action Selection")
        );

        assertEquals(
            "Die Macher is a game about seven sequential political races in different regions of Germany. Players are in charge of national political parties, and must manage limited resources to help their party to victory. The winning party will have the most victory points after all the regional elections. There are four different ways of scoring victory points. First, each regional election can supply one to eighty victory points, depending on the size of the region and how well your party does in it. Second, if a party wins a regional election and has some media influence in the region, then the party will receive some media-control victory points. Third, each party has a national party membership which will grow as the game progresses and this will supply a fair number of victory points. Lastly, parties score some victory points if their party platform matches the national opinions at the end of the game...The 1986 edition featured 4 parties from the old West Germany and supported 3-4 players. The 1997 edition supports up to 5 players in the re-united Germany and updated several features of the rules as well.  The 2006 edition also supports up to 5 players and adds a shorter 5 round variant and additional rules updates by the original designer...Die Macher is #1 in the Valley Games Classic Line..",
            boardGame.description()
        );
    }

    @Test
    void readAll_whenRead_thenCorrectGameBoardIsReturned() {
        BasicBoardGameCSVReader csv = new BasicBoardGameCSVReader(createDummyReader());
        List<BoardGame> boardGames = csv.readAll();

        // row 1
        assertEquals(1, boardGames.get(0).id());
        assertEquals(5, boardGames.get(0).maxPlayers());
        assertEquals(14, boardGames.get(0).minAge());
        assertEquals(3, boardGames.get(0).minPlayers());
        assertEquals("Die Macher", boardGames.get(0).name());
        assertEquals(240, boardGames.get(0).playingTimeMins());

        assertIterableEquals(List.of("Economic","Negotiation","Political"), boardGames.get(0).categories());

        assertIterableEquals(
            boardGames.get(0).mechanics(),
            List.of("Area Control / Area Influence","Auction/Bidding","Dice Rolling","Hand Management","Simultaneous Action Selection")
        );

        assertEquals(
            "Die Macher is a game about seven sequential political races in different regions of Germany. Players are in charge of national political parties, and must manage limited resources to help their party to victory. The winning party will have the most victory points after all the regional elections. There are four different ways of scoring victory points. First, each regional election can supply one to eighty victory points, depending on the size of the region and how well your party does in it. Second, if a party wins a regional election and has some media influence in the region, then the party will receive some media-control victory points. Third, each party has a national party membership which will grow as the game progresses and this will supply a fair number of victory points. Lastly, parties score some victory points if their party platform matches the national opinions at the end of the game...The 1986 edition featured 4 parties from the old West Germany and supported 3-4 players. The 1997 edition supports up to 5 players in the re-united Germany and updated several features of the rules as well.  The 2006 edition also supports up to 5 players and adds a shorter 5 round variant and additional rules updates by the original designer...Die Macher is #1 in the Valley Games Classic Line..",
            boardGames.get(0).description()
        );

        // row 2

        assertEquals(2, boardGames.get(1).id());
        assertEquals(4, boardGames.get(1).maxPlayers());
        assertEquals(12, boardGames.get(1).minAge());
        assertEquals(3, boardGames.get(1).minPlayers());
        assertEquals("Dragonmaster", boardGames.get(1).name());
        assertEquals(30, boardGames.get(1).playingTimeMins());

        assertIterableEquals(List.of("Card Game","Fantasy"), boardGames.get(1).categories());

        assertIterableEquals(
            boardGames.get(1).mechanics(),
            List.of("Trick-taking")
        );

        assertEquals(
            "Dragonmaster is a trick-taking card game based on an older game called Coup d'etat. Each player is given a supply of plastic gems, which represent points. Each player will get to be the dealer for five different hands, with slightly different goals for each hand. After all cards have been dealt out, the dealer decides which hand best suits his or her current cards, and the other players are penalized points (in the form of crystals) for taking certain tricks or cards. For instance, if &quot first&quot  or &quot last&quot  is called, then a player is penalized for taking the first or last tricks. All players will get a chance to be dealer for five hands, but other players can steal this opportunity by taking all of the tricks during certain hands. At the end, the biggest pile of gems wins the game...Jewel contents:..11 clear (3 extra).13 green (1 extra).22 red (2 extra).22 blue (2 extra)..",
            boardGames.get(1).description()
        );
    }

    private Reader createDummyReader() {
        return new StringReader(
            """
            id;details.maxplayers;details.minage;details.minplayers;details.name;details.playingtime;attributes.boardgamecategory;attributes.boardgamemechanic;details.description
            1;5;14;3;Die Macher;240;Economic,Negotiation,Political;Area Control / Area Influence,Auction/Bidding,Dice Rolling,Hand Management,Simultaneous Action Selection;Die Macher is a game about seven sequential political races in different regions of Germany. Players are in charge of national political parties, and must manage limited resources to help their party to victory. The winning party will have the most victory points after all the regional elections. There are four different ways of scoring victory points. First, each regional election can supply one to eighty victory points, depending on the size of the region and how well your party does in it. Second, if a party wins a regional election and has some media influence in the region, then the party will receive some media-control victory points. Third, each party has a national party membership which will grow as the game progresses and this will supply a fair number of victory points. Lastly, parties score some victory points if their party platform matches the national opinions at the end of the game...The 1986 edition featured 4 parties from the old West Germany and supported 3-4 players. The 1997 edition supports up to 5 players in the re-united Germany and updated several features of the rules as well.  The 2006 edition also supports up to 5 players and adds a shorter 5 round variant and additional rules updates by the original designer...Die Macher is #1 in the Valley Games Classic Line..
            2;4;12;3;Dragonmaster;30;Card Game,Fantasy;Trick-taking;Dragonmaster is a trick-taking card game based on an older game called Coup d'etat. Each player is given a supply of plastic gems, which represent points. Each player will get to be the dealer for five different hands, with slightly different goals for each hand. After all cards have been dealt out, the dealer decides which hand best suits his or her current cards, and the other players are penalized points (in the form of crystals) for taking certain tricks or cards. For instance, if &quot first&quot  or &quot last&quot  is called, then a player is penalized for taking the first or last tricks. All players will get a chance to be dealer for five hands, but other players can steal this opportunity by taking all of the tricks during certain hands. At the end, the biggest pile of gems wins the game...Jewel contents:..11 clear (3 extra).13 green (1 extra).22 red (2 extra).22 blue (2 extra)..
            """
        );
    }

    private Reader createIllegalCSVReader() {
        return new StringReader(
            """
            id;details.maxplayers;details.minage;details.minplayers;details.name;details.playingtime;attributes.boardgamecategory;attributes.boardgamemechanic;details.description
            1;14;3;Die Macher;240;Economic,Negotiation,Political;Area Control / Area Influence,Auction/Bidding,Dice Rolling,Hand Management,Simultaneous Action Selection;Die Macher is a game about seven sequential political races in different regions of Germany. Players are in charge of national political parties, and must manage limited resources to help their party to victory. The winning party will have the most victory points after all the regional elections. There are four different ways of scoring victory points. First, each regional election can supply one to eighty victory points, depending on the size of the region and how well your party does in it. Second, if a party wins a regional election and has some media influence in the region, then the party will receive some media-control victory points. Third, each party has a national party membership which will grow as the game progresses and this will supply a fair number of victory points. Lastly, parties score some victory points if their party platform matches the national opinions at the end of the game...The 1986 edition featured 4 parties from the old West Germany and supported 3-4 players. The 1997 edition supports up to 5 players in the re-united Germany and updated several features of the rules as well.  The 2006 edition also supports up to 5 players and adds a shorter 5 round variant and additional rules updates by the original designer...Die Macher is #1 in the Valley Games Classic Line..
            """
        );
    }

    private Reader createIncompleteGameBoardCSVReader() {
        // Missing details.maxplayers
        return new StringReader(
            """
            id;details.minage;details.minplayers;details.name;details.playingtime;attributes.boardgamecategory;attributes.boardgamemechanic;details.description
            1;3;Die Macher;240;Economic,Negotiation,Political;Area Control / Area Influence,Auction/Bidding,Dice Rolling,Hand Management,Simultaneous Action Selection;Die Macher is a game about seven sequential political races in different regions of Germany. Players are in charge of national political parties, and must manage limited resources to help their party to victory. The winning party will have the most victory points after all the regional elections. There are four different ways of scoring victory points. First, each regional election can supply one to eighty victory points, depending on the size of the region and how well your party does in it. Second, if a party wins a regional election and has some media influence in the region, then the party will receive some media-control victory points. Third, each party has a national party membership which will grow as the game progresses and this will supply a fair number of victory points. Lastly, parties score some victory points if their party platform matches the national opinions at the end of the game...The 1986 edition featured 4 parties from the old West Germany and supported 3-4 players. The 1997 edition supports up to 5 players in the re-united Germany and updated several features of the rules as well.  The 2006 edition also supports up to 5 players and adds a shorter 5 round variant and additional rules updates by the original designer...Die Macher is #1 in the Valley Games Classic Line..
            """
        );
    }

}
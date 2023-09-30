package bg.sofia.uni.fmi.mjt.boardgames.io;

import bg.sofia.uni.fmi.mjt.boardgames.io.exceptions.BadCSVFormatException;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class BasicCSVReaderTest {

    private static final String ID                = "id";
    private static final String NAME              = "details.name";
    private static final String DESCRIPTION       = "details.description";
    private static final String MAX_PLAYERS       = "details.maxplayers";
    private static final String MIN_AGE           = "details.minage";
    private static final String MIN_PLAYERS       = "details.minplayers";
    private static final String PLAYING_TIME_MINS = "details.playingtime";
    private static final String CATEGORIES        = "attributes.boardgamecategory";
    private static final String MECHANICS         = "attributes.boardgamemechanic";

    @Test
    void whenCreated_thenCorrectColumnNamesAreRead() {
        BasicCSVReader csv = new BasicCSVReader(createDummyReader());

        var names = csv.getColumnNames();
        assertEquals(ID, names.get(0));
        assertEquals(MAX_PLAYERS, names.get(1));
        assertEquals(MIN_AGE, names.get(2));
        assertEquals(MIN_PLAYERS, names.get(3));
        assertEquals(NAME, names.get(4));
        assertEquals(PLAYING_TIME_MINS, names.get(5));
        assertEquals(CATEGORIES, names.get(6));
        assertEquals(MECHANICS, names.get(7));
        assertEquals(DESCRIPTION, names.get(8));
    }

    @Test
    void read_whenRead_thenCorrectColumnValuesAreRead() {
        BasicCSVReader csv = new BasicCSVReader(createDummyReader());

        var column = csv.read();

        assertEquals("1", column.get(ID));
        assertEquals("5", column.get(MAX_PLAYERS));
        assertEquals("14", column.get(MIN_AGE));
        assertEquals("3", column.get(MIN_PLAYERS));
        assertEquals("Die Macher", column.get(NAME));
        assertEquals("240", column.get(PLAYING_TIME_MINS));
        assertEquals("Economic,Negotiation,Political", column.get(CATEGORIES));

        assertEquals(
            "Area Control / Area Influence,Auction/Bidding,Dice Rolling,Hand Management,Simultaneous Action Selection",
            column.get(MECHANICS)
        );

        assertEquals(
            "Die Macher is a game about seven sequential political races in different regions of Germany. Players are in charge of national political parties, and must manage limited resources to help their party to victory. The winning party will have the most victory points after all the regional elections. There are four different ways of scoring victory points. First, each regional election can supply one to eighty victory points, depending on the size of the region and how well your party does in it. Second, if a party wins a regional election and has some media influence in the region, then the party will receive some media-control victory points. Third, each party has a national party membership which will grow as the game progresses and this will supply a fair number of victory points. Lastly, parties score some victory points if their party platform matches the national opinions at the end of the game...The 1986 edition featured 4 parties from the old West Germany and supported 3-4 players. The 1997 edition supports up to 5 players in the re-united Germany and updated several features of the rules as well.  The 2006 edition also supports up to 5 players and adds a shorter 5 round variant and additional rules updates by the original designer...Die Macher is #1 in the Valley Games Classic Line..",
            column.get(DESCRIPTION)
        );

    }

    @Test
    void read_whenFileIsIllegal_thenThrow() {
        BasicCSVReader csv = new BasicCSVReader(createIllegalCSVReader());
        assertThrows(BadCSVFormatException.class, csv::read);
    }

    @Test
    void readAll_whenFileIsIllegal_thenThrow() {
        BasicCSVReader csv = new BasicCSVReader(createIllegalCSVReader());
        assertThrows(BadCSVFormatException.class, csv::readAll);
    }

    @Test
    void readAll_whenRead_thenCorrectColumnValuesAreRead() {
        BasicCSVReader csv = new BasicCSVReader(createDummyReader());

        var columns = csv.readAll();
        var column1 = columns.get(0);
        var column2 = columns.get(1);

        assertEquals("1", column1.get(ID));
        assertEquals("5", column1.get(MAX_PLAYERS));
        assertEquals("14", column1.get(MIN_AGE));
        assertEquals("3", column1.get(MIN_PLAYERS));
        assertEquals("Die Macher", column1.get(NAME));
        assertEquals("240", column1.get(PLAYING_TIME_MINS));
        assertEquals("Economic,Negotiation,Political", column1.get(CATEGORIES));

        assertEquals(
            "Area Control / Area Influence,Auction/Bidding,Dice Rolling,Hand Management,Simultaneous Action Selection",
            column1.get(MECHANICS)
        );

        assertEquals(
            "Die Macher is a game about seven sequential political races in different regions of Germany. Players are in charge of national political parties, and must manage limited resources to help their party to victory. The winning party will have the most victory points after all the regional elections. There are four different ways of scoring victory points. First, each regional election can supply one to eighty victory points, depending on the size of the region and how well your party does in it. Second, if a party wins a regional election and has some media influence in the region, then the party will receive some media-control victory points. Third, each party has a national party membership which will grow as the game progresses and this will supply a fair number of victory points. Lastly, parties score some victory points if their party platform matches the national opinions at the end of the game...The 1986 edition featured 4 parties from the old West Germany and supported 3-4 players. The 1997 edition supports up to 5 players in the re-united Germany and updated several features of the rules as well.  The 2006 edition also supports up to 5 players and adds a shorter 5 round variant and additional rules updates by the original designer...Die Macher is #1 in the Valley Games Classic Line..",
            column1.get(DESCRIPTION)
        );

        assertEquals("2", column2.get(ID));
        assertEquals("4", column2.get(MAX_PLAYERS));
        assertEquals("12", column2.get(MIN_AGE));
        assertEquals("3", column2.get(MIN_PLAYERS));
        assertEquals("Dragonmaster", column2.get(NAME));
        assertEquals("30", column2.get(PLAYING_TIME_MINS));
        assertEquals("Card Game,Fantasy", column2.get(CATEGORIES));

        assertEquals("Trick-taking",
            column2.get(MECHANICS)
        );

        assertEquals(
            "Dragonmaster is a trick-taking card game based on an older game called Coup d'etat. Each player is given a supply of plastic gems, which represent points. Each player will get to be the dealer for five different hands, with slightly different goals for each hand. After all cards have been dealt out, the dealer decides which hand best suits his or her current cards, and the other players are penalized points (in the form of crystals) for taking certain tricks or cards. For instance, if &quot first&quot  or &quot last&quot  is called, then a player is penalized for taking the first or last tricks. All players will get a chance to be dealer for five hands, but other players can steal this opportunity by taking all of the tricks during certain hands. At the end, the biggest pile of gems wins the game...Jewel contents:..11 clear (3 extra).13 green (1 extra).22 red (2 extra).22 blue (2 extra)..",
            column2.get(DESCRIPTION)
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

}
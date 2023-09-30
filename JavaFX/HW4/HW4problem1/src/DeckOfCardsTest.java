public class DeckOfCardsTest {

    public static void main(String[] args) {
        DeckOfCards myDeckOfCards = new DeckOfCards();
        myDeckOfCards.shuffle();

        Card club = null;
        Card diamond = null;
        Card heart = null;
        Card spade = null;

        int numberOfTries = 0;

        while (club == null || diamond == null || heart == null || spade == null) {
            Card dealt = myDeckOfCards.dealCard();
            Suit suit = dealt.getSuit();

            if (suit == Suit.CLUBS && club == null)
                club = dealt;
            else if (suit == Suit.DIAMONDS && diamond == null)
                diamond = dealt;
            else if (suit == Suit.HEARTS && heart == null)
                heart = dealt;
            else if (suit == Suit.SPADES && spade == null)
                spade = dealt;

            numberOfTries++;
            myDeckOfCards.shuffle();
        }

        System.out.printf("%s\n%s\n%s\n%s\n", club, diamond, heart, spade);
        System.out.printf("Number of tries: %d", numberOfTries);
    }

}


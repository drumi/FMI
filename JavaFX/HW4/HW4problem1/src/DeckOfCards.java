import java.util.Random;

public class DeckOfCards {

    private final int NUMBER_OF_CARDS = 52;
    private final Random randomNumbers;
    private final Card[] deck;
    private int currentCard;

    public DeckOfCards() {
        deck = new Card[NUMBER_OF_CARDS];
        currentCard = 0;
        randomNumbers = new Random();

        int i = 0;
        for (Suit suit : Suit.values())
            for (Face face : Face.values())
                deck[i++] = new Card(face, suit);
    }

    public void shuffle() {
        currentCard = 0;

        for (int first = 0; first < deck.length; first++) {
            int second = randomNumbers.nextInt(NUMBER_OF_CARDS);

            // swap current Card with randomly selected Card
            Card temp = deck[first];
            deck[first] = deck[second];
            deck[second] = temp;
        }
    }

    public Card dealCard() {
        return currentCard < deck.length ? deck[currentCard++] : null;
    }

}

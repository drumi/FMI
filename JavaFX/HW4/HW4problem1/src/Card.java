// Fig. 7.9: Card.java
// Card class represents a playing card.

public class Card {

    private final static Face DEFAULT_FACE = Face.ACE;
    private final static Suit DEFAULT_SUIT = Suit.CLUBS;

    private Face face;
    private Suit suit;

    public Card(Face face, Suit suit) {
        setFace(face);
        setSuit(suit);
    }

    public Card() {
        this(DEFAULT_FACE, DEFAULT_SUIT);
    }

    public Card(Card c) {
        this(c.face, c.suit);
    }

    public Face getFace() {
        return face;
    }

    public void setFace(Face face) {
        this.face = face != null ? face : DEFAULT_FACE;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit != null ? suit : DEFAULT_SUIT;
    }

    @Override
    public String toString() {
        return String.format("%s of %s", face.toString(), suit.toString());
    }

}

package bg.sofia.uni.fmi.mjt.boardgames.recommender.exceptions;

public class RecommenderException extends RuntimeException {

    public RecommenderException(Throwable cause) {
        super(cause);
    }

    public RecommenderException(String message, Throwable cause) {
        super(message, cause);
    }
}

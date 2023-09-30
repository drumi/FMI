package bg.fmi.news.exceptions;

public class RateLimitedException extends ApiException {

    public RateLimitedException(String message) {
        super(message);
    }

    public RateLimitedException(String message, Throwable cause) {
        super(message, cause);
    }

}

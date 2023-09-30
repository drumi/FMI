package bg.fmi.news.exceptions;

public class SourcesTooManyException extends ApiException {

    public SourcesTooManyException(String message) {
        super(message);
    }

    public SourcesTooManyException(String message, Throwable cause) {
        super(message, cause);
    }

}

package bg.fmi.news.exceptions;

public class SourceDoesNotExistException extends ApiException {

    public SourceDoesNotExistException(String message) {
        super(message);
    }

    public SourceDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

}

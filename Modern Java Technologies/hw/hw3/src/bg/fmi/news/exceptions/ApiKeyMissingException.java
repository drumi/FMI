package bg.fmi.news.exceptions;

public class ApiKeyMissingException extends ApiException {

    public ApiKeyMissingException(String message) {
        super(message);
    }

    public ApiKeyMissingException(String message, Throwable cause) {
        super(message, cause);
    }

}

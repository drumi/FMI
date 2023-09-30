package bg.fmi.news.exceptions;

public class ApiKeyExhaustedException extends ApiException {

    public ApiKeyExhaustedException(String message) {
        super(message);
    }

    public ApiKeyExhaustedException(String message, Throwable cause) {
        super(message, cause);
    }

}

package bg.fmi.news.exceptions;

public class ApiKeyInvalidException extends ApiException {

    public ApiKeyInvalidException(String message) {
        super(message);
    }

    public ApiKeyInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

}

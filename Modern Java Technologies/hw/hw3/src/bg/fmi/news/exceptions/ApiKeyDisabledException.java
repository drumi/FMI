package bg.fmi.news.exceptions;

public class ApiKeyDisabledException extends ApiException {

    public ApiKeyDisabledException(String message) {
        super(message);
    }

    public ApiKeyDisabledException(String message, Throwable cause) {
        super(message, cause);
    }

}

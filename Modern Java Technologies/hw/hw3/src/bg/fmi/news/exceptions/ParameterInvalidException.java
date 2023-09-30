package bg.fmi.news.exceptions;

public class ParameterInvalidException extends ApiException {

    public ParameterInvalidException(String message) {
        super(message);
    }

    public ParameterInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

}

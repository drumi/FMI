package bg.fmi.news.exceptions;

public class ParametersMissingException extends ApiException {

    public ParametersMissingException(String message) {
        super(message);
    }

    public ParametersMissingException(String message, Throwable cause) {
        super(message, cause);
    }

}

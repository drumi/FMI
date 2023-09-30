package bg.sofia.uni.fmi.mjt.boardgames.io.exceptions;

public class DataValidationException extends RuntimeException {

    public DataValidationException(Throwable t) {
        super(t);
    }

    public DataValidationException(String msg, Throwable t) {
        super(msg, t);
    }

    public DataValidationException(String msg) {
        super(msg);
    }

}

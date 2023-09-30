package bg.sofia.uni.fmi.mjt.boardgames.io.exceptions;

public class BadCSVFormatException extends RuntimeException {

    public BadCSVFormatException(Throwable t) {
        super(t);
    }

    public BadCSVFormatException(String msg, Throwable t) {
        super(msg, t);
    }

    public BadCSVFormatException(String msg) {
        super(msg);
    }

}

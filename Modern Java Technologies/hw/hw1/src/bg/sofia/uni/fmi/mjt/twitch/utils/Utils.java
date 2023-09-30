package bg.sofia.uni.fmi.mjt.twitch.utils;

public class Utils {

    private Utils() {
    }

    public static void requireNonNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    public static void requireNonNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireNonEmpty(String s) {
        requireNonNull(s);

        if (s.isEmpty()) {
            throw new IllegalArgumentException("String cannot be empty");
        }
    }


    public static void requireNonEmpty(String s, String message) {
        requireNonNull(s);

        if (s.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}

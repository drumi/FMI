package bg.sofia.uni.fmi.mjt.boardgames.utils;

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

    @SafeVarargs
    public static <T> void requireAllNonNull(T... os) {
        requireNonNull(os, "Array cannot be null");

        for (Object o : os) {
            if (o == null) {
                throw new IllegalArgumentException("Array cannot contain null elements");
            }
        }
    }

    public static void requireAllNonNull(Iterable<?> os, String msg) {
        requireNonNull(os, msg);

        for (Object o : os) {
            if (o == null) {
                throw new IllegalArgumentException(msg);
            }
        }
    }

    public static void requireNonEmpty(String s) {
        requireNonNull(s);

        if (s.isEmpty()) {
            throw new IllegalArgumentException("String cannot be empty");
        }
    }


    public static void requireNonEmpty(String s, String msg) {
        requireNonNull(s);

        if (s.isEmpty()) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void requireNonNegative(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("int cannot be negative");
        }
    }

    public static void requireNonNegative(int n, String msg) {
        if (n < 0) {
            throw new IllegalArgumentException(msg);
        }
    }

}

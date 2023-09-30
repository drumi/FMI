package bg.sofia.uni.fmi.mjt.gifts;

public class Utils {

    public static void requireNonNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
    }

    public static void requireNonNull(Object... os) {
        for (var o : os) {
            if (o == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void requireNonNegative(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
    }

}

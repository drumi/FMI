package bg.sofia.uni.fmi.mjt.rentalservice.location;

public class Location {

    private final double x;
    private final double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static boolean isDistanceLessOrEqualTo(Location from, Location to, double distance) {
        return Location.calculateSquaredDistance(from, to) <= distance * distance && distance >= 0.0;
    }

    private static double calculateSquaredDistance(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dy = to.getX() - from.getY();

        return dx * dx + dy * dy;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        /*
         * This line was edited from the original auto-generated code.
         * Reason: 0.0d must be equal to -0.0d
         */
        return location.x == x && location.y == y;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

package geometry;

public class Point {

    private int[] coords;

    public Point() {
        this(new int[2]);
    }

    public Point(int[] coords) {
        setCoords(coords);
    }

    public Point(Point p) {
        this(p.coords);
    }

    public int[] getCoords() {
        int[] copy = new int[coords.length];

        for (int i = 0; i < coords.length; i++) {
            copy[i] = coords[i];
        }

        return copy;
    }

    public void setCoords(int[] coords) {
        if (coords != null && coords.length == 2) {
            this.coords = new int[coords.length];

            for (int i = 0; i < coords.length; i++) {
                this.coords[i] = coords[i];
            }
        } else {
            this.coords = new int[2];
        }
    }

    @Override
    public String toString() {
        return String.format("(%d; %d)", coords[0], coords[1]);
    }
}

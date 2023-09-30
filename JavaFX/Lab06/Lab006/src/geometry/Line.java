package geometry;

public class Line extends Point {

    private Point ePoint;

    public Line(Point ePoint) {
        setePoint(ePoint);
    }

    public Line(int[] coords, Point ePoint) {
        super(coords);
        setePoint(ePoint);
    }

    public Line() {
        this(new int[2], new Point(new int[]{1, 1}));
    }

    public Line(Line l) {
        this(l.getCoords(), l.ePoint);
    }

    public Point getePoint() {
        return new Point(ePoint);
    }

    public void setePoint(Point ePoint) {
        if (ePoint != null) {
            this.ePoint = new Point(ePoint);
        } else {
            this.ePoint = new Point(new int[]{1, 1});
        }
    }

    public double measure() {
        int x1 = getCoords()[0];
        int x2 = ePoint.getCoords()[0];
        int y1 = getCoords()[1];
        int y2 = ePoint.getCoords()[1];

        return Math.sqrt(
                (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)
        );
    }

    @Override
    public String toString() {
        return String.format(
                "Line with start (%d, %d) and end %s",
                getCoords()[0],
                getCoords()[1],
                getePoint()
        );
    }

}

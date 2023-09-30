package geometry;

public class Rectangle extends Point {

    private Point ePoint;

    public Rectangle() {
        this(new int[2], new Point(new int[]{2,3}));
    }

    public Rectangle(int[] coords, Point ePoint) {
        super(coords);
        this.ePoint = ePoint;
    }

    public Rectangle(Rectangle r) {
        this(r.getCoords(), r.ePoint);
    }

    public int measure() {
        int width = ePoint.getCoords()[0] - this.getCoords()[0];
        int height = ePoint.getCoords()[1] - this.getCoords()[1];

        return (width + height) * 2;
    }


    public Point getePoint() {
        return new Point(ePoint);
    }

    public void setePoint(Point ePoint) {
        if (ePoint != null) {
            this.ePoint = new Point(ePoint);
        } else {
            this.ePoint = new Point();
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Rect with upper left %s and lower right %s",
                super.toString(),
                ePoint
        );
    }
}

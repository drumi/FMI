public class Point {

    private double x;
    private double y;

    public Point() {
        this(0, 0);
    }

    public Point(double x, double y) {
        setX(x);
        setY(y);
    }

    public Point(Point p) {
        this(p.x, p.y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}

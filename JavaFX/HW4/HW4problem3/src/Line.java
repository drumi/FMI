import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Line {

    private Point start;
    private Point end;

    public Line() {
        this(new Point(0, 0), new Point(10, 10));
    }

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public Line(Line l) {
        this(l.start, l.end);
    }

    public Point getStart() {
        return new Point(start);
    }

    public void setStart(Point start) {
        this.start = start != null ? start : new Point(0, 0);
    }

    public Point getEnd() {
        return new Point(end);
    }

    public void setEnd(Point end) {
        this.end = end != null ? end : new Point(10, 10);
    }

    public void draw(Group pane) {
        javafx.scene.shape.Line line =
            new javafx.scene.shape.Line(start.getX(), start.getY(), end.getX(), end.getY());

        line.setStrokeWidth(3.0);
        line.setStroke(Color.RED);

        pane.getChildren().add(line);
    }

}

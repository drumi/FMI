package geometry;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Line {
    private Point startingPoint;
    private Point endingPoint;

    public Line() {
        this(new Point(), new Point(new double[]{10, 10}));
    }

    public Line(Point startingPoint, Point endingPoint) {
        setStartingPoint(startingPoint);
        setEndingPoint(endingPoint);
    }

    public Line(Line line) {
        this(line.startingPoint, line.endingPoint);
    }

    public Point getStartingPoint() {
        return new Point(startingPoint);
    }

    public void setStartingPoint(Point startingPoint) {
        if (startingPoint != null) {
            this.startingPoint = new Point(startingPoint);
        } else {
            this.startingPoint = new Point(new double[]{10, 10});
        }
    }

    public Point getEndingPoint() {
        return new Point(endingPoint);
    }

    public void setEndingPoint(Point endingPoint) {
        if (endingPoint != null) {
            this.endingPoint = new Point(endingPoint);
        } else {
            this.endingPoint = new Point(new double[]{10, 10});
        }
    }

    public void draw(Group pane) {
        javafx.scene.shape.Line line = new javafx.scene.shape.Line(
                startingPoint.getCoords()[0], startingPoint.getCoords()[1],
                endingPoint.getCoords()[0], endingPoint.getCoords()[1]
        );

        line.setStroke(Color.RED);
        line.setStrokeWidth(3);

        pane.getChildren().add(line);
    }

    @Override
    public String toString() {
        return String.format("Line with starting point %s and ending point %s", startingPoint, endingPoint);
    }

}

package com.homework.shared;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Line {

    private static Point DEFAULT_START_POINT = new Point(0, 0);
    private static Point DEFAULT_END_POINT = new Point(30, 30);

    private static Color DEFAULT_COLOR = Color.BLUE;
    private static int DEFAULT_WIDTH = 3;

    private Point startPoint;
    private Point endPoint;

    public Line() {
        this(DEFAULT_START_POINT, DEFAULT_END_POINT);
    }

    public Line(Point startPoint, Point endPoint) {
        setStartPoint(startPoint);
        setEndPoint(endPoint);
    }

    public Line(Line l) {
        this(l.startPoint, l.endPoint);
    }

    public Point getStartPoint() {
        return new Point(startPoint);
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = (startPoint != null) ? startPoint : new Point(DEFAULT_START_POINT);
    }

    public Point getEndPoint() {
        return new Point(endPoint);
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = (endPoint != null) ? endPoint : new Point(DEFAULT_END_POINT);
    }

    public void draw(Group pane) {
        javafx.scene.shape.Line line = new javafx.scene.shape.Line(
            startPoint.getX(), startPoint.getY(),
            endPoint.getX(), endPoint.getY()
        );

        line.setStroke(DEFAULT_COLOR);
        line.setStrokeWidth(DEFAULT_WIDTH);

        pane.getChildren().add(line);
    }

}

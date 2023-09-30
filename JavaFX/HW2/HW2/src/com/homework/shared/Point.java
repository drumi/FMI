package com.homework.shared;

public class Point {

    private static double DEFAULT_X = 0;
    private static double DEFAULT_Y = 0;

    private double x;
    private double y;

    public Point() {
        this(DEFAULT_X, DEFAULT_Y);
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
        this.x = (x >= 0) ? x : DEFAULT_X;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = (y >= 0) ? y : DEFAULT_Y;
    }

}

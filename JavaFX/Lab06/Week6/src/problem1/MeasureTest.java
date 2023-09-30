package problem1;

import geometry.Line;
import geometry.Point;
import geometry.Rectangle;

public class MeasureTest {

    public static void main(String[] args) {
        Point p1 = new Point(new int[]{3, 4});
        Point p2 = new Point(new int[]{6, 7});

        Line diagonal = new Line(p1.getCoords(), p2);
        Rectangle rectangle = new Rectangle(p1.getCoords(), p2);

        System.out.printf(
                "%s\nLength: %.4f \n\n%s\nPerimeter: %d\n",
                diagonal,
                diagonal.measure(),
                rectangle,
                rectangle.measure()
        );
    }
}
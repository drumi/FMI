import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SquareDraw extends Application {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private static final int INITIAL_LINE_SIZE = 48;
    private static final int GROWTH_RATE = 48;

    @Override
    public void start(Stage stage) throws Exception {
        Group group = new Group();
        Scene scene = new Scene(group, WIDTH, HEIGHT);
        stage.setTitle("Draw square shaped spiral");
        stage.sizeToScene();
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);

        double centerX = WIDTH / 2.0;
        double centerY = HEIGHT / 2.0;
        double length = INITIAL_LINE_SIZE;

        Point start = new Point(centerX, centerY);
        Direction direction = Direction.DOWN;

        while (length < WIDTH && length < HEIGHT) {
            Point end = calculateNext(start, direction, length);

            Line l = new Line(start, end);
            l.draw(group);

            direction = direction.next();
            start = end;

            if (direction == Direction.UP || direction == Direction.DOWN)
                length += GROWTH_RATE;
        }

        stage.show();
    }

    private static Point calculateNext(Point previous, Direction current, double size) {
        return switch (current) {
            case DOWN ->  new Point( previous.getX()       , previous.getY() + size );
            case LEFT ->  new Point( previous.getX() - size, previous.getY()        );
            case UP ->    new Point( previous.getX()       , previous.getY() - size );
            case RIGHT -> new Point( previous.getX() + size, previous.getY()        );
        };
    }


    public static void main(String[] args) {
        launch(args);
    }
}
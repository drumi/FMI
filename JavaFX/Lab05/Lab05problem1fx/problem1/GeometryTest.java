package problem1;

import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GeometryTest extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Group group = new Group();
        Scene scene = new Scene(group, 500, 500);

        Point s = new Point(new double[]{30, 30});

        Rectangle rectangle = new Rectangle(new Point(new double[]{30, 30}), 30, 40);
        rectangle.draw(group);

        Point e = new Point(new double[]{s.getCoords()[0] + rectangle.getWidth(), s.getCoords()[1] + rectangle.getHeight()});

        Line line = new Line(s, e);
        line.draw(group);

        // Set the Window title
        stage.setTitle("Geometry");
        stage.sizeToScene();
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
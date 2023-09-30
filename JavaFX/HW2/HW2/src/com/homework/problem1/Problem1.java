package com.homework.problem1;

import com.homework.shared.Line;
import com.homework.shared.Point;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Problem1 extends Application {

    private static int STEP_SIZE = 32;
    private static int LINES = 16;

    @Override
    public void start(Stage stage) throws Exception {
        Group group = new Group();
        Scene scene = new Scene(group);

        for (int i = 0; i < LINES; i++) {
            double endX = i * STEP_SIZE;
            double endY = (LINES - 1) * STEP_SIZE - i * STEP_SIZE;

            Line line = new Line(
                new Point(0, 0),
                new Point(endX, endY)
            );

            line.draw(group);
        }

        // Set the Window title
        stage.setTitle("Lines");
        stage.sizeToScene();
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
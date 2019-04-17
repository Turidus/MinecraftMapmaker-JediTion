package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class GUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        try{
            Line line = new Line();
            line.setStartX(100.0);
            line.setStartY(150.0);
            line.setEndX(500.0);
            line.setEndY(150.0);

            BorderPane root = new BorderPane(line);
            Scene scene = new Scene(root,600,400);
            scene.setFill(Color.LIGHTGRAY);
            primaryStage.setTitle(" Minecraft Map Maker");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

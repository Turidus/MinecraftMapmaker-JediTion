package gui;

import events.CriticalExceptionEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        } catch (IOException e) {
            EventBus.getDefault().post(new CriticalExceptionEvent("GUI.fxml File was not found!",e));
        }

        primaryStage.setTitle("Minecraft Map Maker");
        primaryStage.setScene(new Scene(root,1000,800));
        primaryStage.show();

    }
}

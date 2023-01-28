package de.turidus.minecraft_mapmaker;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<GUI.StageReadyEvent> {

    @SneakyThrows
    @Override
    public void onApplicationEvent(GUI.StageReadyEvent stageReadyEvent) {
        Stage stage = stageReadyEvent.getStage();
        Scene scene = new Scene(loadFXML(), 1200, 675);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("GUI" + ".fxml"));
        return fxmlLoader.load();
    }

}

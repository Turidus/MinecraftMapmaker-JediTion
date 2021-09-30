module MinecraftMapmaker.JediTion {
    requires org.jetbrains.annotations;
    requires eventbus;
    requires json.simple;

    requires javafx.controls;
    requires javafx.fxml;

    requires java.xml.bind;
    requires java.desktop;
    requires org.slf4j;

    opens de.turidus.minecraft_mapmaker to javafx.fxml;
    exports de.turidus.minecraft_mapmaker;
}
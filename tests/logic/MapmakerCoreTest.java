package logic;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class MapmakerCoreTest {

    @Test
    void runTest() throws FileNotFoundException, ClassNotFoundException {
        ConfigStore configStore = ConfigStore.getInstance();
        configStore.pathToImage = "resources/icon.gif";

        MapmakerCore mapmakerCore = new MapmakerCore();

        mapmakerCore.run();

    }
}
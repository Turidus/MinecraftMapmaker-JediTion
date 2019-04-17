package tests.logic;

import logic.ConfigStore;
import logic.MapmakerCore;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class MapmakerCoreTest {

    @Test
    void runTest() throws FileNotFoundException {
        ConfigStore configStore = ConfigStore.getInstance();
        configStore.pathToImage = "resources/icon.gif";

        MapmakerCore mapmakerCore = new MapmakerCore();

        mapmakerCore.run();

    }
}
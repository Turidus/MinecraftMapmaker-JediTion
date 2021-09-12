package de.turidus.logic;

import de.turidus.utils.ConfigStore;
import de.turidus.utils.FaF;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class MapmakerCoreTest {

    @Test
    void runTest() throws FileNotFoundException, ClassNotFoundException {
        ConfigStore configStore = ConfigStore.getInstance();
        configStore.pathToImage = FaF.ICON;

        MapmakerCore mapmakerCore = new MapmakerCore();

        mapmakerCore.run();

    }
}
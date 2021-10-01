package minecraft_mapmaker.logic;

import de.turidus.minecraft_mapmaker.logic.MapmakerCore;
import de.turidus.minecraft_mapmaker.utils.ConfigStore;
import de.turidus.minecraft_mapmaker.utils.FaF;
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
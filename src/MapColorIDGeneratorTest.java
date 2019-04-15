import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


class MapColorIDGeneratorTest {

    @org.junit.jupiter.api.Test
    void mapColorIDGenerator3DTest() throws IOException {
        HashMap<Integer,MapIDEntry> mapColorID = MapColorIDGenerator.mapColorIDGenerator3D(new ArrayList<String>());

        for (Map.Entry<Integer, MapIDEntry> entry : mapColorID.entrySet()){
            System.out.format("ID: %d; RGB: %d,%d,%d; BlockName: %s; BlockID: %s%n", entry.getKey(), entry.getValue().rgbList.get(0), entry.getValue().rgbList.get(1), entry.getValue().rgbList.get(2), entry.getValue().blockName, entry.getValue().blockID);
        }
    }

    @org.junit.jupiter.api.Test
    void mapColorIDGenerator2D() throws IOException {
        HashMap<Integer,MapIDEntry> mapColorID = MapColorIDGenerator.mapColorIDGenerator2D(new ArrayList<String>());

        for (Map.Entry<Integer, MapIDEntry> entry : mapColorID.entrySet()){
            System.out.format("ID: %d; RGB: %d,%d,%d; BlockName: %s; BlockID: %s%n", entry.getKey(), entry.getValue().rgbList.get(0), entry.getValue().rgbList.get(1), entry.getValue().rgbList.get(2), entry.getValue().blockName, entry.getValue().blockID);
        }
    }
}
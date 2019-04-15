package tests.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import main.ColorIDMap;
import main.MapIDEntry;


class ColorIDMapTest {

    @org.junit.jupiter.api.Test
    void mapColorIDGenerator3DTest() throws IOException {
        ColorIDMap colorIDMap = new ColorIDMap(true,new ArrayList<>());

        /*for (Map.Entry<Integer, MapIDEntry> entry : colorIDMap.getMap().entrySet()){
            System.out.format("ID: %d; RGB: %d,%d,%d; BlockName: %s; BlockID: %s%n", entry.getKey(), entry.getValue().rgbList.get(0), entry.getValue().rgbList.get(1), entry.getValue().rgbList.get(2), entry.getValue().blockName, entry.getValue().blockID);
        }*/
    }

    @org.junit.jupiter.api.Test
    void mapColorIDGenerator2D() throws IOException {
        ColorIDMap colorIDMap = new ColorIDMap(false,new ArrayList<>());

        /*for (Map.Entry<Integer, MapIDEntry> entry : colorIDMap.getMap().entrySet()){
            System.out.format("ID: %d; RGB: %d,%d,%d; BlockName: %s; BlockID: %s%n", entry.getKey(), entry.getValue().rgbList.get(0), entry.getValue().rgbList.get(1), entry.getValue().rgbList.get(2), entry.getValue().blockName, entry.getValue().blockID);
        }*/
    }
}
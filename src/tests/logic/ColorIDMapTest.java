package tests.logic;

import java.io.IOException;
import java.util.ArrayList;

import logic.ColorIDMap;


class ColorIDMapTest {

    @org.junit.jupiter.api.Test
    void mapColorIDGenerator3DTest() throws IOException {
        ColorIDMap colorIDMap = new ColorIDMap(true,new ArrayList<>());

        /*for (Map.Entry<Integer, MapIDEntry> entry : colorIDMap.getMap().entrySet()){
            int red =entry.getValue().getRed();
            int green = entry.getValue().getGreen();
            int blue = entry.getValue().getBlue();
            System.out.format("ID: %d; RGB: %d,%d,%d; BlockName: %s; BlockID: %s%n", entry.getKey(), red , green, blue, entry.getValue().blockName, entry.getValue().blockID);
        }*/
    }

    @org.junit.jupiter.api.Test
    void mapColorIDGenerator2D() throws IOException {
        ColorIDMap colorIDMap = new ColorIDMap(false, new ArrayList<>());

        /*for (Map.Entry<Integer, MapIDEntry> entry : colorIDMap.getMap().entrySet()) {
            int red = entry.getValue().getRed();
            int green = entry.getValue().getGreen();
            int blue = entry.getValue().getBlue();
            System.out.format("ID: %d; RGB: %d,%d,%d; BlockName: %s; BlockID: %s%n", entry.getKey(), red, green, blue, entry.getValue().blockName, entry.getValue().blockID);
        }*/
    }
}
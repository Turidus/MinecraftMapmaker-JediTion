package tests.logic;


import java.io.IOException;
import java.util.*;

import logic.ColorIDMap;
import logic.MapIDEntry;


class ColorIDMapTest {

    /*@Test
    void getBaseColorIDMap() throws FileNotFoundException {
        TreeMap<Integer, List<MapIDEntry>> baseColorIDMap = ColorIDMap.getBaseColorIDMap();
        for (int key : baseColorIDMap.keySet()){
            for(MapIDEntry entry : baseColorIDMap.get(key)){
            }
        }
    }*/
    
    @org.junit.jupiter.api.Test
    void mapColorIDGenerator3DTest() throws IOException {
        TreeMap<Integer, List<MapIDEntry>> baseColorIDMap = ColorIDMap.getBaseColorIDMap();
        ArrayList entryList = new ArrayList();
        for (int key : baseColorIDMap.keySet()){
            entryList.add(baseColorIDMap.get(key).get(0));
        }

        ColorIDMap colorIDMap = new ColorIDMap(true,entryList);

        for (Map.Entry<Integer, MapIDEntry> entry : colorIDMap.getMap().entrySet()){
            int red =entry.getValue().getRed();
            int green = entry.getValue().getGreen();
            int blue = entry.getValue().getBlue();
            System.out.format("ID: %d; RGB: %d,%d,%d; BlockName: %s; BlockID: %s%n", entry.getKey(), red , green, blue, entry.getValue().blockName, entry.getValue().blockID);
        }
    }

    @org.junit.jupiter.api.Test
    void mapColorIDGenerator2D() throws IOException {
        TreeMap<Integer, List<MapIDEntry>> baseColorIDMap = ColorIDMap.getBaseColorIDMap();
        ArrayList entryList = new ArrayList();
        for (int key : baseColorIDMap.keySet()){
            entryList.add(baseColorIDMap.get(key).get(0));
        }

        ColorIDMap colorIDMap = new ColorIDMap(false,entryList);

        /*for (Map.Entry<Integer, MapIDEntry> entry : colorIDMap.getMap().entrySet()) {
            int red = entry.getValue().getRed();
            int green = entry.getValue().getGreen();
            int blue = entry.getValue().getBlue();
            System.out.format("ID: %d; RGB: %d,%d,%d; BlockName: %s; BlockID: %s%n", entry.getKey(), red, green, blue, entry.getValue().blockName, entry.getValue().blockID);
        }*/
    }

    
}
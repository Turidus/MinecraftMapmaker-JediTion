package minecraft_mapmaker.logic;

import de.turidus.minecraft_mapmaker.logic.ColorIDMap;
import de.turidus.minecraft_mapmaker.logic.ColorIDMatrix;
import de.turidus.minecraft_mapmaker.logic.MapIDEntry;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ColorIDMatrixTest {

    @Test
    void getEntryfromPoint() throws IOException, ClassNotFoundException {
        TreeMap<Integer, List<MapIDEntry>> baseColorIDMap = ColorIDMap.getBaseColorIDMap();
        ArrayList<MapIDEntry> entryList = new ArrayList<>();
        for (int key : baseColorIDMap.keySet()){
            entryList.add(baseColorIDMap.get(key).get(0));
        }

        ColorIDMap colorIDMap = new ColorIDMap(true,true,entryList);

        ColorIDMatrix colorIDMatrix = new ColorIDMatrix(new File("testpictures/icon.gif"),colorIDMap, false);

        MapIDEntry mapIDEntry = colorIDMatrix.getEntryFromPoint(0, 1);
        assertEquals(116, mapIDEntry.colorID());
        assertEquals(1118481, mapIDEntry.rgb());
        assertEquals(17 ,mapIDEntry.getRed());
        assertEquals(17, mapIDEntry.getGreen());
        assertEquals(17, mapIDEntry.getBlue());
        assertEquals("minecraft:black_glazed_terracotta", mapIDEntry.blockID());
        assertEquals("Black Glazed Terracotta", mapIDEntry.blockName());
    }

    @Test
    void coordinateTest() throws IOException, ClassNotFoundException {
        TreeMap<Integer, List<MapIDEntry>> baseColorIDMap = ColorIDMap.getBaseColorIDMap();
        ArrayList<MapIDEntry> entryList = new ArrayList<>();
        for (int key : baseColorIDMap.keySet()){
            entryList.add(baseColorIDMap.get(key).get(0));
        }

        ColorIDMap colorIDMap = new ColorIDMap(true, true, entryList);
        ColorIDMatrix colorIDMatrix = new ColorIDMatrix(new File("testpictures/icon.gif"),colorIDMap, false);
        MapIDEntry mapIDEntry = colorIDMatrix.getEntryFromPoint(0, 2);
        System.out.println(mapIDEntry.colorID());
        System.out.println(mapIDEntry.getRed());
        System.out.println(mapIDEntry.getGreen());
        System.out.println(mapIDEntry.getBlue());

        assertEquals(116,mapIDEntry.colorID());
    }


    @Test
    void getAmountString() throws IOException, ClassNotFoundException {
        TreeMap<Integer, List<MapIDEntry>> baseColorIDMap = ColorIDMap.getBaseColorIDMap();
        ArrayList<MapIDEntry> entryList = new ArrayList<>();
        for (int key : baseColorIDMap.keySet()){
            entryList.add(baseColorIDMap.get(key).get(0));
        }

        ColorIDMap colorIDMap = new ColorIDMap(true, true, entryList);

        ColorIDMatrix colorIDMatrix = new ColorIDMatrix(new File("testpictures/icon.gif"), colorIDMap, false);
        String resultString = colorIDMatrix.getAmountString();
        System.out.println(resultString);

        assertFalse(resultString.isEmpty());
    }

    @Test
    void imageFromColorIDMatrix() throws IOException, ClassNotFoundException {
        TreeMap<Integer, List<MapIDEntry>> baseColorIDMap = ColorIDMap.getBaseColorIDMap();
        ArrayList<MapIDEntry> entryList = new ArrayList<>();
        for (int key : baseColorIDMap.keySet()){
            entryList.add(baseColorIDMap.get(key).get(0));
        }

        ColorIDMap colorIDMap = new ColorIDMap(true,true, entryList);

        ColorIDMatrix colorIDMatrix = new ColorIDMatrix(new File("testpictures/icon.gif"),colorIDMap, false);
        BufferedImage image = colorIDMatrix.imageFromColorIDMatrix();
        ImageIcon icon=new ImageIcon(image);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(200,300);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
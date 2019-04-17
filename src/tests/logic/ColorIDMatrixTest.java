package tests.logic;

import logic.ColorIDMap;
import logic.ColorIDMatrix;
import logic.MapIDEntry;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ColorIDMatrixTest {

    @Test
    void getEntryfromPoint() throws IOException {
        ColorIDMap colorIDMap = new ColorIDMap(true,new ArrayList<>());

        ColorIDMatrix colorIDMatrix = new ColorIDMatrix(new File("resources/icon.gif"),colorIDMap);

        MapIDEntry mapIDEntry = colorIDMatrix.getEntryfromPoint(0,1);
        assertEquals(116, mapIDEntry.colorID);
        assertEquals(1118481, mapIDEntry.rgb);
        assertEquals(17 ,mapIDEntry.getRed());
        assertEquals(17, mapIDEntry.getGreen());
        assertEquals(17, mapIDEntry.getBlue());
        assertEquals("173", mapIDEntry.blockID);
        assertEquals("Block of Coal", mapIDEntry.blockName);
    }

    @Test
    void coordinateTest() throws IOException{
        ColorIDMap colorIDMap = new ColorIDMap(true,new ArrayList<>());
        ColorIDMatrix colorIDMatrix = new ColorIDMatrix(new File("resources/test.png"),colorIDMap);
        MapIDEntry mapIDEntry = colorIDMatrix.getEntryfromPoint(0,2);
        /*System.out.println(mapIDEntry.colorID);
        System.out.println(mapIDEntry.getRed());
        System.out.println(mapIDEntry.getGreen());
        System.out.println(mapIDEntry.getBlue());*/

        assertEquals(34,mapIDEntry.colorID);
    }


    @Test
    void getAmountString() throws IOException {
        ColorIDMap colorIDMap = new ColorIDMap(true,new ArrayList<>());

        ColorIDMatrix colorIDMatrix = new ColorIDMatrix(new File("resources/icon.gif"),colorIDMap);
        String resultString = colorIDMatrix.getAmountString();

        /*System.out.println(resultString);
        System.out.println(resultString.indexOf("133"));
        System.out.println(resultString.lastIndexOf("40"));*/

        assertFalse(resultString.isEmpty());
    }

    @Test
    void imageFromColorIDMatrix() throws IOException {
        ColorIDMap colorIDMap = new ColorIDMap(true,new ArrayList<>());

        ColorIDMatrix colorIDMatrix = new ColorIDMatrix(new File("resources/icon.gif"),colorIDMap);
        BufferedImage image = colorIDMatrix.imageFromColorIDMatrix();
        /*ImageIcon icon=new ImageIcon(image);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(200,300);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
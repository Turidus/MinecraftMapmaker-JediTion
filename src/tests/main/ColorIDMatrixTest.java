package tests.main;

import main.ColorIDMap;
import main.ColorIDMatrix;
import main.MapIDEntry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ColorIDMatrixTest {

    @Test
    void getEntryfromPoint() throws IOException {
        ColorIDMap colorIDMap = new ColorIDMap(true,new ArrayList<>());

        ColorIDMatrix colorIDMatrix = new ColorIDMatrix(new File("ressources/icon.gif"),colorIDMap);

        MapIDEntry mapIDEntry = colorIDMatrix.getEntryfromPoint(0,0);
        assertEquals(116, mapIDEntry.colorID);
        assertEquals(1118481, mapIDEntry.rgb);
        assertEquals(17 ,mapIDEntry.getRed());
        assertEquals(17, mapIDEntry.getGreen());
        assertEquals(17, mapIDEntry.getBlue());
        assertEquals("173", mapIDEntry.blockID);
        assertEquals("Block of Coal", mapIDEntry.blockName);
    }
}
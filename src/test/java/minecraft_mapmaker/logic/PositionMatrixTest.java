package minecraft_mapmaker.logic;

import de.turidus.minecraft_mapmaker.logic.ColorIDMap;
import de.turidus.minecraft_mapmaker.logic.ColorIDMatrix;
import de.turidus.minecraft_mapmaker.logic.MapIDEntry;
import de.turidus.minecraft_mapmaker.logic.PositionMatrix;
import de.turidus.minecraft_mapmaker.nbt.Tag_Compound;
import de.turidus.minecraft_mapmaker.utils.FaF;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionMatrixTest {

    @Test
    void newPositionMatrix() throws IOException, ClassNotFoundException {
        File file = new File(FaF.ICON);
        TreeMap<Integer, List<MapIDEntry>> baseColorIDMap = ColorIDMap.getBaseColorIDMap();
        ArrayList<MapIDEntry> entryList = new ArrayList<>();
        for (int key : baseColorIDMap.keySet()){
            entryList.add(baseColorIDMap.get(key).get(0));
        }

        ColorIDMap colorIDMap = new ColorIDMap(true,entryList);
        ColorIDMatrix colorIDMatrix = new ColorIDMatrix(file,colorIDMap, false);

        PositionMatrix positionMatrix = new PositionMatrix(colorIDMatrix);
        assertEquals(4,positionMatrix.getMatrix()[0][16]);
    }

    @Test
    void positionString() throws IOException, ClassNotFoundException {
        File file = new File("testpictures/DTH.jpg");
        TreeMap<Integer, List<MapIDEntry>> baseColorIDMap = ColorIDMap.getBaseColorIDMap();
        ArrayList<MapIDEntry> entryList = new ArrayList<>();
        for (int key : baseColorIDMap.keySet()){
            entryList.add(baseColorIDMap.get(key).get(0));
        }

        ColorIDMap colorIDMap = new ColorIDMap(true,entryList);
        ColorIDMatrix colorIDMatrix = new ColorIDMatrix(file,colorIDMap, false);

        PositionMatrix positionMatrix = new PositionMatrix(colorIDMatrix);
        String result = positionMatrix.getPositionString();
        System.out.println(result);
    }

    @Test
    void tagCompoundList() throws IOException, ClassNotFoundException {
        File file = new File("testpictures/DTH.jpg");
        TreeMap<Integer, List<MapIDEntry>> baseColorIDMap = ColorIDMap.getBaseColorIDMap();
        ArrayList<MapIDEntry> entryList = new ArrayList<>();
        for (int key : baseColorIDMap.keySet()){
            entryList.add(baseColorIDMap.get(key).get(0));
        }

        ColorIDMap colorIDMap = new ColorIDMap(true,entryList);
        ColorIDMatrix colorIDMatrix = new ColorIDMatrix(file,colorIDMap, false);

        PositionMatrix positionMatrix = new PositionMatrix(colorIDMatrix);
        List<Tag_Compound> tag_compoundList = positionMatrix.getTag_CompoundList();

        /*int count = 0;
        for (byte item : tag_compoundList.get(0).toBytes().toByteArray()){
            System.out.println(count + ": " + String.format("%8s",String.valueOf(item)).replace(" ", "0"));
            count++;
        }*/

    }

}
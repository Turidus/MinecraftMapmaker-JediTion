package de.turidus.logic;

import de.turidus.utils.FaF;
import de.turidus.utils.FileHandler;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Generates the colorIDMap out of the BaseColorIDList.txt and a blacklist.
 *
 * @author Lars Schulze-Falck
 * <p>
 * “Commons Clause” License Condition v1.0
 * The Software is provided to you by the Licensor under the License, as defined below, subject to the following condition.
 * Without limiting other conditions in the License, the grant of rights under the License will not include, and the License does not grant to you,
 * the right to Sell the Software.
 * For purposes of the foregoing, “Sell” means practicing any or all of the rights granted to you under the License to provide to third parties,
 * for a fee or other consideration (including without limitation fees for hosting or consulting/ support services related to the Software),
 * a product or service whose value derives, entirely or substantially, from the functionality of the Software.
 * Any license notice or attribution required by the License must also include this Commons Cause License Condition notice.
 * Software: MinecraftMapMaker_JediTion
 * License: MIT
 * Licensor: Lars Schulze-Falck
 * <p>
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2019 Lars Schulze-Falck
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class ColorIDMap {

    private final HashMap<Integer, MapIDEntry> map = new HashMap<>();

    public ColorIDMap(boolean threeD, @NotNull List<MapIDEntry> usedBlocks) {
        if (threeD) mapColorIDGenerator3D(usedBlocks);
        else mapColorIDGenerator2D(usedBlocks);
    }

    private int mult180(int x) {
        return (x * 180) / 255;
    }

    private int mult220(int x) {
        return (x * 220) / 255;
    }


    /**
     * This function takes a list of {@link MapIDEntry} and generates the colorIDs for a three dimensional construct
     * @param usedBlocks A list of MapIDEntries describing the blocks that are to be used for the generation of the map.
     */
    private void mapColorIDGenerator3D(@NotNull List<MapIDEntry> usedBlocks) {

        for (MapIDEntry entry : usedBlocks) {

            int[] rgbArray = {entry.getRed(), entry.getGreen(), entry.getBlue()};

            int rgb180 = (mult180(rgbArray[0]) << 16 | mult180(rgbArray[1]) << 8 | mult180(rgbArray[2]));
            int colorID180 = entry.colorID() * 4;

            int rgb220 = (mult220(rgbArray[0]) << 16 | mult220(rgbArray[1]) << 8 | mult220(rgbArray[2]));
            int colorID220 = entry.colorID() * 4 + 1;

            int rgb255 = (rgbArray[0] << 16 | rgbArray[1] << 8 | rgbArray[2]);
            int colorID255 = entry.colorID() * 4 + 2;


            map.put(colorID180, new MapIDEntry(colorID180, rgb180, entry.blockName(), entry.blockID()));
            map.put(colorID220, new MapIDEntry(colorID220, rgb220, entry.blockName(), entry.blockID()));
            map.put(colorID255, new MapIDEntry(colorID255, rgb255, entry.blockName(), entry.blockID()));
        }
    }

    /**
     * This function takes a list of {@link MapIDEntry} and generates the colorIDs for a two dimensional construct
     * @param usedBlocks A list of MapIDEntries describing the blocks that are to be used for the generation of the map.
     */
    private void mapColorIDGenerator2D(@NotNull List<MapIDEntry> usedBlocks) {


        for (MapIDEntry entry : usedBlocks) {

            int[] rgbArray = {entry.getRed(), entry.getGreen(), entry.getBlue()};

            int rgb220 = (mult220(rgbArray[0]) << 16 | mult220(rgbArray[1]) << 8 | mult220(rgbArray[2]));
            int colorID220 = entry.colorID() * 4 + 1;

            map.put(colorID220, new MapIDEntry(colorID220, rgb220, entry.blockName(), entry.blockID()));
        }
    }

    /**
     * Parses the BaseColorID into a TreeMap for further handling.
     * @return A {@code TreeMap<Integer,List<MapIDEntry>>} that contains the base color ID as key and {@link MapIDEntry} as value
     * @throws FileNotFoundException If BaseColorIDs.txt file could not be found
     */
    public static TreeMap<Integer,List<MapIDEntry>> getBaseColorIDMap() throws FileNotFoundException {
        TreeMap<Integer,List<MapIDEntry>> baseColorIDMap = new TreeMap<>();
        BufferedReader reader = new BufferedReader(FileHandler.getFileReaderFromConfigFolderOrDefault(FaF.BASE_COLOR_ID, FaF.BASE_COLOR_ID_DEFAULT));

        int baseColorID = 0;
        int rgb = 0;
        ArrayList<MapIDEntry> tempList = new ArrayList<>();
        for (String line : reader.lines().toArray(String[]::new)){
            if (line.startsWith("#") || line.isEmpty()) continue;

            else if (!line.startsWith(" ")){
                if(!tempList.isEmpty()){
                    baseColorIDMap.put(baseColorID,tempList);
                    tempList = new ArrayList<>();
                }
                String[] firstSplit = line.split(";");
                String[] secondSplit = firstSplit[1].split(",");
                baseColorID = Integer.parseInt(firstSplit[0]);
                rgb = (Integer.parseInt(secondSplit[0].trim()) << 16) | (Integer.parseInt(secondSplit[1].trim()) << 8) | (Integer.parseInt(secondSplit[2].trim()));
                //System.out.println("ID: " + baseColorID + "red:" + secondSplit[0]);
            }
            else {
                String[] firstSplit = line.split(",");
                MapIDEntry tempEntry = new MapIDEntry(baseColorID,rgb,firstSplit[0].trim(),firstSplit[1].trim());
                tempList.add(tempEntry);
            }
        }
        if(!tempList.isEmpty()){
            baseColorIDMap.put(baseColorID,tempList);
        }

        return baseColorIDMap;
    }

    public MapIDEntry getEntry(int key) {
        return map.get(key);
    }

    public HashMap<Integer, MapIDEntry> getMap() {
        return map;
    }
}

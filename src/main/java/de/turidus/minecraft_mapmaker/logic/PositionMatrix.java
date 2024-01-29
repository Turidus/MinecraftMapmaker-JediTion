package de.turidus.minecraft_mapmaker.logic;

import de.turidus.minecraft_mapmaker.nbt.*;
import de.turidus.minecraft_mapmaker.utils.ConfigStore;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * This class holds the position matrix, a two dimensional array that contains y-coordinates for the blocks.
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
public class PositionMatrix {

    ConfigStore configStore = ConfigStore.getInstance();

    private final int maxY;
    private final int minY;
    private final int maxSize;

    private int[][] positionMatrix;

    private final int width;
    private final int length;
    private final ColorIDMatrix colorIDMatrix;

    /**
     * This constructor calls {@link PositionMatrix#positionMatrixFromColorIDMatrix()} to build the position matrix
     *
     * @param colorIDMatrix a {@link ColorIDMatrix}
     * @throws FileNotFoundException Thrown if the config.txt file was not found
     */
    public PositionMatrix(@NotNull ColorIDMatrix colorIDMatrix) throws FileNotFoundException, ClassNotFoundException {

        this.width = colorIDMatrix.getWidth();
        this.length = colorIDMatrix.getLength();
        this.colorIDMatrix = colorIDMatrix;
        this.maxSize = ConfigStore.getInstance().maxS;
        this.minY = ConfigStore.getInstance().minY;
        this.maxY = ConfigStore.getInstance().maxY;
        positionMatrixFromColorIDMatrix();
    }

    /**
     * This method builds a String containing the XZY Coordinates and block names of all blocks,
     * which then can be saved to a file
     *
     * @return A formatted string containing the XZY Coordinates of all blocks
     */
    public String getPositionString() {
        StringBuilder positionSB = new StringBuilder();
        positionSB.append(String.format("|%40s|%5s|%5s|%5s|%n", "Block", "X", "Z", "Y"));
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < length; z++) {
                /*
                The z values are inverted, because the matrix starts at the upper left corner of the image,
                but in Minecraft the coordinates will start at the lower left corner.
                 */
                positionSB.append(String.format("|%40s|%5d|%5d|%5d|%n", colorIDMatrix.getEntryFromPoint(x, z).blockName(), x, length - z - 1, positionMatrix[x][z]));
            }

            positionSB.append(String.format("%n"));
        }

        return positionSB.toString();
    }

    /**
     * This method builds a list of {@link Tag_Compound} containing at least one Tag_Compound, with
     * each Tag_Compound representing a part of the image.
     * This list can then be used to create the schematic files.
     *
     * @return A list of {@link Tag_Compound} representing the image
     * @throws IllegalArgumentException Thrown if there is a mismatch between ColorIDMap and ColorIDMatrix
     */
    public List<Tag_Compound> getTag_CompoundList() throws IllegalArgumentException {
        ArrayList<Tag_Compound> tagCompoundList = new ArrayList<>();


        int maxSchematicHeight = maxY - minY  + 1;
        int highestUsedY = minY;

        /*
        The order of indexes is [y][z][x] by the convention of Schematics, also the z-value is inverted to the image based ordering.
         */
        int[][][] schematicCube = new int[maxSchematicHeight][length][width];

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < length; z++) {
                int correctedY = positionMatrix[x][z] - minY;
                MapIDEntry entry = colorIDMatrix.getEntryFromPoint(x, z);

                //schematicCube[correctedY][length - (z + 1)][x] = entry.colorID;
                schematicCube[correctedY][z][x] = entry.colorID();
                highestUsedY = Math.max(correctedY, highestUsedY);
                /*
                Surrounds blocks of water with glass on all sides and below. Because there is no colorID for glass,
                the unused value 1 is used.
                */
                if (entry.blockID().equals("minecraft:water")) {
                    surroundWaterWithGlass(schematicCube, correctedY, z, x);
                }
                else if(entry.blockID().equals("minecraft:glow_lichen")
                        || entry.blockID().contains("_plate")){
                    try {
                        if (schematicCube[correctedY - 1][z][x] == 0) {
                            schematicCube[correctedY - 1][z][x] = -2;
                        }
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                }
            }
        }
        /*
        Preparing data for building the Tag_Compounds.
        If the picture is bigger than maxSize, it will get cut in pieces
        to make importing them easier.
        Even so, you should use Fast asynchrone world edit or similar.
        See Readme for additional information
         */
        int schematicLength = length;
        int schematicWidth = width;
        int schematicHeight;
        if (highestUsedY < maxSchematicHeight) schematicHeight = highestUsedY + 1;
        else schematicHeight = maxSchematicHeight;

        List<Integer> lengthRanges = new ArrayList<>();
        for (int i = 0; i < (schematicLength / maxSize + 1); i++) {
            lengthRanges.add(i * maxSize);
        }
        if (lengthRanges.get(lengthRanges.size() - 1) < schematicLength) lengthRanges.add(schematicLength);

        List<Integer> widthRanges = new ArrayList<>();
        for (int i = 0; i < (schematicWidth / maxSize + 1); i++) {
            widthRanges.add(i * maxSize);
        }
        if (widthRanges.get(widthRanges.size() - 1) < schematicWidth) widthRanges.add(schematicWidth);

        for (int rangeZ = 1; rangeZ < lengthRanges.size(); rangeZ++) {
            for (int rangeX = 1; rangeX < widthRanges.size(); rangeX++) {

                List<Integer> blockList = new ArrayList<>();
                Map<String, Integer> patternMap = new HashMap<>();
                int totalBlocks = 0;
                int curIndex = 0;
                patternMap.put("minecraft:air", curIndex++); //Workaround for Litematica assuming minecraft:air == 0.
                for (int y = 0; y < schematicHeight; y++) {
                    for (int z = lengthRanges.get(rangeZ - 1); z < lengthRanges.get(rangeZ); z++) {
                        for (int x = widthRanges.get((rangeX - 1)); x < widthRanges.get((rangeX)); x++) {


                            int colorID = schematicCube[y][z][x];
                            switch(colorID) {
                                case 0 -> blockList.add(patternMap.get("minecraft:air"));
                                case -1 -> {
                                    totalBlocks++;
                                    if(!patternMap.containsKey(configStore.blockForWater)) {
                                        patternMap.put(configStore.blockForWater, curIndex++);
                                    }
                                    blockList.add(patternMap.get(configStore.blockForWater));
                                }
                                case -2 -> {
                                    totalBlocks++;
                                    if(!patternMap.containsKey(configStore.supportBlock)) {
                                        patternMap.put(configStore.supportBlock, curIndex++);
                                    }
                                    blockList.add(patternMap.get(configStore.supportBlock));
                                }
                                default -> {
                                    totalBlocks++;
                                    if(colorIDMatrix.getEntryFromID(colorID) == null)
                                        throw new IllegalArgumentException(String.format("%d was not a valid colorID", colorID));
                                    MapIDEntry blockEntry = colorIDMatrix.getEntryFromID(colorID);
                                    String blockID = blockEntry.blockID() + blockEntry.blockState();
                                    if(!patternMap.containsKey(blockID)) {
                                        patternMap.put(blockID, curIndex++);
                                    }
                                    blockList.add(patternMap.get(blockID));
                                }
                            }
                        }
                    }
                }

                //Building Tag_Compound
                String tag_compoundName = (rangeZ - 1) + " " + (rangeX - 1);
                int partWidth = widthRanges.get(rangeX) - widthRanges.get(rangeX - 1);
                int partLength = lengthRanges.get(rangeZ) - lengthRanges.get(rangeZ - 1);
                if (configStore.spongeSchematic) {
                    tagCompoundList.add(getSpongeSchematica(tag_compoundName,
                                                                partWidth,
                                                                schematicHeight,
                                                                partLength,
                                                                patternMap,
                                                                blockList));
                }
                else tagCompoundList.add(getLitematicaSchematic(tag_compoundName,
                                                                partWidth,
                                                                schematicHeight,
                                                                partLength,
                                                                totalBlocks,
                                                                patternMap,
                                                                blockList));
            }
        }
        return tagCompoundList;
    }

    private static void surroundWaterWithGlass(int[][][] schematicCube, int correctedY, int z, int x) {
        try {
            if (schematicCube[correctedY - 1][z][x] == 0) {
                schematicCube[correctedY - 1][z][x] = -1;
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        try {
            if (schematicCube[correctedY][z - 1][x] == 0) {
                schematicCube[correctedY][z - 1][x] = -1;
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        try {
            if (schematicCube[correctedY][z + 1][x] == 0) {
                schematicCube[correctedY][z + 1][x] = -1;
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        try {
            if (schematicCube[correctedY][z][x - 1] == 0) {
                schematicCube[correctedY][z][x - 1] = -1;
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        try {
            if (schematicCube[correctedY][z][x + 1] == 0) {
                schematicCube[correctedY][z][x + 1] = -1;
            }
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    public int[][] getMatrix() {
        return positionMatrix;
    }

    /**
     * This method builds the positionMatrix out of the {@link ColorIDMatrix}
     */
    private void positionMatrixFromColorIDMatrix() {
        this.positionMatrix = new int[width][length];
        fillPositionMatrixWithHeightValues();
        secondPassToCorrectHeightValues();
    }

    private void fillPositionMatrixWithHeightValues() {
        int startY = minY + (minY + maxY) / 2;

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < length; z++) {

                if (z == 0) {
                    positionMatrix[x][0] = startY;
                } else {
                    switch(colorIDMatrix.getEntryFromPoint(x, z).colorID() % 4) {
                        case 1 -> positionMatrix[x][z] = positionMatrix[x][z - 1];
                        case 2 -> positionMatrix[x][z] = positionMatrix[x][z - 1] + 1;
                        case 0 -> {
                            if(positionMatrix[x][z - 1] <= minY) {
                                for(int zz = 0; zz < z; zz++) {
                                    positionMatrix[x][zz] += 1;
                                }
                            }
                            positionMatrix[x][z] = positionMatrix[x][z - 1] - 1;
                        }
                    }
                }
            }
        }
    }

    private void secondPassToCorrectHeightValues() {

        normalizeMinimumYValueOfColumns();

        correctViolationsOfMaximumYValues();
    }

    private void normalizeMinimumYValueOfColumns() {
         /*
        Normalisation of each North-South column which are independent of each other,
        contrary to the West-East rows. At the end, each column has at least one block on
        the minimal Y coordinate.
         */

        for (int x = 0; x < width; x++) {
            int lowestY = maxY;
            for (int z = 0; z < length; z++) {
                lowestY = Math.min(lowestY, positionMatrix[x][z]);
            }
            if (lowestY > minY) {
                int offsetY = lowestY - minY;
                for (int z = 0; z < length; z++) {
                    positionMatrix[x][z] -= offsetY;
                }
            }
        }
    }

    private void correctViolationsOfMaximumYValues() {
        /*
        Finding all ranges of blocks in each line that are too high and force them to be lower
        than the maximum Y coordinate. This can lead to mismatched pixels inside the picture.
        See Readme for additional information
         */

        int maxOffsetY = maxY - minY + 1;

        for (int x = 0; x < width; x++) {
            boolean exceedingY = false;
            boolean inExceedingRange = false;
            List<Integer> rangeZValues = new ArrayList<>();

            for (int z = 0; z < length; z++) {
                if (positionMatrix[x][z] > maxY && !inExceedingRange) {
                    exceedingY = true;
                    inExceedingRange = true;
                    rangeZValues.add(z);
                } else if (positionMatrix[x][z] <= maxY && inExceedingRange) {
                    inExceedingRange = false;
                    rangeZValues.add(z);
                }
            }
            if (inExceedingRange) rangeZValues.add(length);

            while (exceedingY) {
                for (int index = 0; index < rangeZValues.size(); index += 2) {
                    for (int z = rangeZValues.get(index); z < rangeZValues.get(index + 1); z++) {
                        positionMatrix[x][z] -= maxOffsetY;
                    }
                }

                exceedingY = false;
                inExceedingRange = false;
                rangeZValues = new ArrayList<>();

                for (int z = 0; z < length; z++) {
                    if (positionMatrix[x][z] > maxY && !inExceedingRange) {
                        exceedingY = true;
                        inExceedingRange = true;
                        rangeZValues.add(z);
                    } else if (positionMatrix[x][z] <= maxY && inExceedingRange) {
                        inExceedingRange = false;
                        rangeZValues.add(z);
                    }
                }
                if (inExceedingRange) rangeZValues.add(length);
            }
        }
    }


    private Tag_Compound getLitematicaSchematic(String name,
                                                int partWidth,
                                                int height,
                                                int partLength,
                                                int totalBlocks,
                                                Map<String, Integer> patternMap,
                                                List<Integer> blockList) {

        List<Tag> tagList = new ArrayList<>();
        /*
        Metadata
         */
        List<Tag> metaDataList = new ArrayList<>();

        List<Tag> enclosingList = new ArrayList<>();
        enclosingList.add(new Tag_Int("x", partWidth));
        enclosingList.add(new Tag_Int("y", height));
        enclosingList.add(new Tag_Int("z", partLength));
        metaDataList.add(new Tag_Compound("EnclosingSize", enclosingList));

        metaDataList.add(new Tag_String("Author", "MinecraftMapMaker"));
        metaDataList.add(new Tag_String("Description", ""));
        metaDataList.add(new Tag_String("Name", name.replace(" ", "")));
        metaDataList.add(new Tag_Int("RegionCount", 1));
        metaDataList.add(new Tag_Long("TimeCreated", System.currentTimeMillis()));
        metaDataList.add(new Tag_Long("TimeModified", System.currentTimeMillis()));
        metaDataList.add(new Tag_Int("TotalBlocks", totalBlocks));
        metaDataList.add(new Tag_Long("TotalVolume", (long) partWidth * height * partLength));

        tagList.add(new Tag_Compound("Metadata", metaDataList));

        /*
        Region
         */
        List<Tag> regionList = new ArrayList<>();
        List<Tag> subregionList = new ArrayList<>();

        List<Tag> positionList = new ArrayList<>();
        positionList.add(new Tag_Int("x", 0));
        positionList.add(new Tag_Int("y", 0));
        positionList.add(new Tag_Int("z", 0));
        subregionList.add(new Tag_Compound("Position",positionList));

        List<Tag> sizeList = new ArrayList<>();
        sizeList.add(new Tag_Int("x", partWidth));
        sizeList.add(new Tag_Int("y", height));
        sizeList.add(new Tag_Int("z", partLength));
        subregionList.add(new Tag_Compound("Size",sizeList));

        subregionList.add(makePaletteTagList(patternMap));
        subregionList.add(new Tag_List("Entities", new ArrayList<Tag_Compound>()));
        subregionList.add(new Tag_List("PendingBlockTicks", new ArrayList<Tag_Compound>()));
        subregionList.add(new Tag_List("PendingFluidTicks", new ArrayList<Tag_Compound>()));
        subregionList.add(new Tag_List("TileEntities", new ArrayList<Tag_Compound>()));

        int bitsPerEntry = Math.max(2, Integer.SIZE - Integer.numberOfLeadingZeros(patternMap.size() - 1));
        subregionList.add(generateBlockStates(blockList, bitsPerEntry));

        regionList.add(new Tag_Compound(name.replace(" ", ""),subregionList));
        tagList.add(new Tag_Compound("Regions", regionList));
        tagList.add(new Tag_Int("MinecraftDataVersion", configStore.mcDataVersion));
        tagList.add(new Tag_Int("Version", 5));

        return new Tag_Compound(name + ".litematic", tagList);
    }

    private Tag_Compound getSpongeSchematica(String tag_compoundName,
                                             int width,
                                             int height,
                                             int length,
                                             Map<String,Integer> patternMap,
                                             List<Integer> blockList) {

        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag_Int("Version", 2));
        tagList.add(new Tag_Int("DataVersion", configStore.mcDataVersion));
        tagList.add(makeMetaDataObject(tag_compoundName));
        tagList.add(new Tag_Short("Width", (short) width));
        tagList.add(new Tag_Short("Height", (short) height));
        tagList.add(new Tag_Short("Length", (short) length));
        tagList.add(new Tag_Int("PaletteMax", patternMap.keySet().size()));
        tagList.add(makePaletteTagCompound(patternMap));
        tagList.add(new Tag_ByteArray("BlockData", makeVarInt(blockList)));
        return new Tag_Compound(tag_compoundName, tagList);
    }


    private Tag_Compound makeMetaDataObject(String tag_compoundName) {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag_String("Name", tag_compoundName));
        tagList.add(new Tag_String("Author", "MinecraftMapmaker"));
        tagList.add(new Tag_Long("Date", System.currentTimeMillis()));

        return new Tag_Compound("Metadata Object", tagList);
    }

    private Tag_LongArray generateBlockStates(List<Integer> blockList, int bitsPerEntry) {
        /*
        Math based on the implementation in Litematica by Matti Ruohonen under the GNU Lesser General Public License v3.0.
        See https://github.com/maruohon/litematica/blob/b64b54c9ddaace55b6db8320ae23fda4dcb73fd7/src/main/java/fi/dy/masa/litematica/schematic/container/LitematicaBitArray.java#L7
        */

        long[] longArray   = getCorrectlySizedArray(blockList, bitsPerEntry);
        long maxEntryValue = (1L << bitsPerEntry) - 1L;

        for(int i = 0; i < blockList.size(); i++){
            int value = blockList.get(i);
            int startOffset = i * bitsPerEntry;
            int startArrayIndex = startOffset >> 6;
            int endArrayIndex = ((i + 1) * bitsPerEntry - 1) >> 6;
            int startBitOffset = startOffset & 0x3f;
            longArray[startArrayIndex] = longArray[startArrayIndex] & ~(maxEntryValue << startBitOffset) | ((long) value & maxEntryValue) << startBitOffset;

            if(startArrayIndex != endArrayIndex){
                int endOffset = 64 - startBitOffset;
                int j1 = bitsPerEntry -endOffset;
                longArray[endArrayIndex] = longArray[endArrayIndex] >>> j1 << j1 | ((long) value & maxEntryValue) >> endOffset;
            }
        }

        return new Tag_LongArray("BlockStates", longArray);
    }

    private Tag_Compound makePaletteTagCompound(Map<String, Integer> patternMap) {
        TreeMap<Integer, String> switchedMap = new TreeMap<>();
        for(String key : patternMap.keySet()) {
            switchedMap.put(patternMap.get(key), key);
        }
        List<Tag> tagList = new ArrayList<>();
        for(Integer key : switchedMap.keySet()){
            tagList.add(new Tag_Int(switchedMap.get(key),key));
        }
        return new Tag_Compound("Palette", tagList);
    }

    private Tag_List makePaletteTagList(Map<String, Integer> patternMap) {
        TreeMap<Integer, String> switchedMap = new TreeMap<>();
        for(String key : patternMap.keySet()) {
            switchedMap.put(patternMap.get(key), key);
        }
        List<Tag_Compound> tagList = new ArrayList<>();
        for(Integer key : switchedMap.keySet()){
            String value = switchedMap.get(key);
            if(!value.contains("[")){
                Tag_String block = new Tag_String("Name", value);
                tagList.add(new Tag_Compound("", Collections.singletonList(block)));
            }
            else {
                String[] splitState = value.split("\\[");
                Tag_String block = new Tag_String("Name", splitState[0]);
                String[] splitProperty = splitState[1].replace("]", "").split("=");
                Tag_String property = new Tag_String(splitProperty[0],splitProperty[1]);
                Tag_Compound properties = new Tag_Compound("Properties", Collections.singletonList(property));
                tagList.add(new Tag_Compound("", Arrays.asList(block, properties)));
            }
        }
        return new Tag_List("BlockStatePalette", tagList);
    }


    private static long[] getCorrectlySizedArray(List<Integer> blockList, int bitsPerEntry) {
        int bits = bitsPerEntry * blockList.size();
        int over = bits % 64;
        int arraySize;
        if(over == 0){
            arraySize = bits / 64;
        }
        else {
            arraySize = (bits + 64 - over) / 64;
        }
        return new long[arraySize];
    }

    private List<Byte> makeVarInt(List<Integer> blockList) {
        List<Byte> byteList = new ArrayList<>();
        for(Integer i : blockList){
            while( (i & -128) != 0){
                byteList.add((byte)(i & 127 | 128));
                i >>>= 7;
            }
            byteList.add(i.byteValue());
        }
        return byteList;
    }

}

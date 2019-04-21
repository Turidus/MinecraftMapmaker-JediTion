package logic;

import nbt.*;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the position matrix, a two dimensional array that contains y-coordiantes for the blocks.
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


    private int maxY;
    private int minY;
    private int maxSize;

    private int[][] positionMatrix;

    private int width;
    private int length;
    private ColorIDMatrix colorIDMatrix;

    /**
     * This constructor calls {@link PositionMatrix#positionMatrixFromColorIDMatrix()} to build the position matrix
     *
     * @param colorIDMatrix a {@link ColorIDMatrix}
     * @throws FileNotFoundException Thrown if the config file was not found
     */
    public PositionMatrix(@NotNull ColorIDMatrix colorIDMatrix) throws FileNotFoundException {

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
                positionSB.append(String.format("|%40s|%5d|%5d|%5d|%n", colorIDMatrix.getEntryfromPoint(x, z).blockName, x, length - z - 1, positionMatrix[x][z]));
            }
        }

        return positionSB.toString();
    }

    /**
     * This method builds a list of {@link Tag_Compound} containing at least one Tag_Compound, with each Tag_Compound representing a part of the image.
     * This list can then be used to create the schematic files.
     *
     * @return A list of {@link Tag_Compound} representing the image
     * @throws IllegalArgumentException Thrown if there is a mismatch between ColorIDMap and ColorIDMatrix
     */
    public List<Tag_Compound> getTag_CompoundList() throws IllegalArgumentException {
        ArrayList<Tag_Compound> tagCompoundList = new ArrayList<>();

        int maxSchematicHeight = maxY - minY;
        int highestUsedY = minY;

        /*
        The order of indexes is [y][z][x] by the convention of Schematics, also the z-value is inverted to the image based ordering.
         */
        int[][][] schematicCube = new int[maxSchematicHeight + 1][length][width];

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < length; z++) {
                int correctedY = positionMatrix[x][z] - minY;
                MapIDEntry entry = colorIDMatrix.getEntryfromPoint(x, z);

                //schematicCube[correctedY][length - (z + 1)][x] = entry.colorID;
                schematicCube[correctedY][z][x] = entry.colorID;
                highestUsedY = Math.max(correctedY, highestUsedY);
                /*
                Surrounds blocks of water with glass on all sides and below. Because there is no colorID for glass,
                the unused value 1 is used.
                */
                if (entry.blockID.equals("9")) {
                    try {
                        if (schematicCube[correctedY - 1][z][x] == 0) {
                            schematicCube[correctedY - 1][z][x] = 1;
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }

                    try {
                        if (schematicCube[correctedY][z - 1][x] == 0) {
                            schematicCube[correctedY][z - 1][x] = 1;
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }

                    try {
                        if (schematicCube[correctedY][z + 1][x] == 0) {
                            schematicCube[correctedY][z + 1][x] = 1;
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }

                    try {
                        if (schematicCube[correctedY][z][x - 1] == 0) {
                            schematicCube[correctedY][z][x - 1] = 1;
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }

                    try {
                        if (schematicCube[correctedY][z][x + 1] == 0) {
                            schematicCube[correctedY][z][x + 1] = 1;
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }
                }

                /*
                Adds a block of cobblestone below all Iron Bars
                 */
                if (entry.blockID.equals("101")) {
                    try {
                        if (schematicCube[correctedY - 1][z][x] == 0) {
                            schematicCube[correctedY - 1][z][x] = 45;
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }
                }
            }
        }
        /*
        Preparing data for building the Tag_Compounds.
        If the picture is bigger than maxSize, it will get cut in pices
        to make importing them easier.
        Even so, you should use Fast asynchrone world edit or similar.
        See Readme for additional information
         */
        int schematicLength = length;
        int schematicWidth = width;
        int schematicHeight;
        if (highestUsedY < maxSchematicHeight) schematicHeight = highestUsedY + 2;
        else schematicHeight = maxSchematicHeight + 2;

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

                List<Byte> blockList = new ArrayList<>();
                List<Byte> blockDataList = new ArrayList<>();

                for (int y = 0; y < schematicHeight; y++) {
                    for (int z = lengthRanges.get(rangeZ - 1); z < lengthRanges.get(rangeZ); z++) {
                        for (int x = widthRanges.get((rangeX - 1)); x < widthRanges.get((rangeX)); x++) {


                            int colorID = schematicCube[y][z][x];
                            switch (colorID) {
                                case 0:
                                    blockList.add((byte) 0);
                                    blockDataList.add((byte) 0);
                                    break;
                                case 1:
                                    blockList.add((byte) 20);
                                    blockDataList.add((byte) 0);
                                    break;
                                default:
                                    String blockID = colorIDMatrix.getEntryFromID(colorID).blockID;
                                    if (blockID == null)
                                        throw new IllegalArgumentException(String.format("%d was not a valid colorID", colorID));

                                    if (blockID.contains(":")) {
                                        blockList.add(Integer.valueOf(blockID.split(":")[0]).byteValue());
                                        blockDataList.add(Integer.valueOf(blockID.split(":")[1]).byteValue());
                                    } else {
                                        blockList.add(Integer.valueOf(blockID).byteValue());
                                        blockDataList.add((byte) 0);
                                    }
                            }
                        }
                    }
                }

                //Building Tag_Coumpound
                List<Tag> tagList = new ArrayList<>();
                tagList.add(new Tag_Short("Height", (short) schematicHeight));
                tagList.add(new Tag_Short("Length", (short) (lengthRanges.get(rangeZ) - lengthRanges.get(rangeZ - 1))));
                tagList.add(new Tag_Short("Width", (short) (widthRanges.get(rangeX) - widthRanges.get(rangeX - 1))));
                tagList.add(new Tag_String("Materials", "Alpha"));
                tagList.add(new Tag_List("Entities"));
                tagList.add(new Tag_List("TileEntities"));
                tagList.add(new Tag_ByteArray("Blocks", blockList));
                tagList.add(new Tag_ByteArray("Data", blockDataList));

                String tag_compoundName = (rangeZ - 1) + " " + (rangeX - 1);
                tagCompoundList.add(new Tag_Compound(tag_compoundName, tagList));
            }
        }
        return tagCompoundList;
    }

    /**
     * This method builds the positionMatrix out of the {@link ColorIDMatrix}
     */
    private void positionMatrixFromColorIDMatrix() {
        this.positionMatrix = new int[width][length];

        int startY = minY + (minY + maxY) / 2;

        /*
        First pass over the matrix to assign y values to every field
         */
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < length; z++) {

                if (z == 0) {
                    positionMatrix[x][0] = startY;
                } else {
                    switch (colorIDMatrix.getEntryfromPoint(x, z).colorID % 4) {
                        case 1:
                            positionMatrix[x][z] = positionMatrix[x][z - 1];
                            break;
                        case 2:
                            positionMatrix[x][z] = positionMatrix[x][z - 1] + 1;
                            break;
                        case 0:
                            if (positionMatrix[x][z - 1] <= minY) {
                                for (int zz = 0; zz < z; zz++) {
                                    positionMatrix[x][zz] += 1;
                                }
                            }
                            positionMatrix[x][z] = positionMatrix[x][z - 1] - 1;
                    }
                }
            }
        }

        /*
        Second pass over the matrix to fix too high y values
         */

        /*
        First Step: Normalisation of each North-South column which are independend from each other,
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

        /*
        Second Step: finding all ranges of blocks in each line that are too high and force them to be lower
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

    public int[][] getMatrix() {
        return positionMatrix;
    }
}

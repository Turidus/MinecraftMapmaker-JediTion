package logic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class contains the ColorIDMatrix, a two dimensional array containing the appropriate colorIDs for every pixel of the image.
 * This in turn maps the {@link ColorIDMap} to every pixel.
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
public class ColorIDMatrix {


    private int[][] colorIDMatrix;

    private int width;
    private int length;

    private ColorIDMap colorIDMap;
    private HashMap<Integer, Integer> amountMap = new HashMap<>();

    /**
     * Constructor
     *
     * @param imageFile
     *         A {@link File} object containing an image file
     * @param colorIDMap
     *         A {@link ColorIDMap}
     * @throws IOException
     */
    public ColorIDMatrix(@NotNull File imageFile, @NotNull ColorIDMap colorIDMap) throws IOException {
        this.colorIDMap = colorIDMap;
        BufferedImage image = ImageIO.read(imageFile);

        this.width = image.getWidth();
        this.length = image.getHeight() + 1;

        imageToColorIDMatrix(image);
    }

    /**
     * This method takes the coordinates of a point on the colorIDMatrix and returns the fitting {@link MapIDEntry}
     *
     * @param x
     *         int value in range [0,width - 1]
     * @param z
     *         int value in range [0,length - 1]
     * @return The {@link MapIDEntry} fitting to that point
     */
    public MapIDEntry getEntryfromPoint(int x, int z) throws IllegalArgumentException {
        if (x < 0 || x >= width) {
            throw new IllegalArgumentException(String.format("x value was %d, only allowed in range [0,%d]", x, width - 1));
        }
        if (z < 0 || z >= length) {
            throw new IllegalArgumentException(String.format("z value was %d, only allowed in range [0,%d]", z, length - 1));
        }
        return colorIDMap.getEntry(colorIDMatrix[x][z]);
    }

    /**
     * This method takes an colorID and returns the fitting {@link MapIDEntry} from colorIDMap
     *
     * @param id
     *         int value representing
     * @return The {@link MapIDEntry} fitting to that point. Null if the ID was not found.
     */
    public @Nullable MapIDEntry getEntryFromID(int id) {
        return colorIDMap.getEntry(id);
    }

    /**
     * This method provides the formatted amount String containing the type and amount of blocks used.
     *
     * @return Formatted String
     */
    public String getAmountString() {
        TreeMap<String, ArrayList<Integer>> sortingMap = new TreeMap<>();
        int waterAmount = 0;
        int ironBarAmount = 0;

        for (Map.Entry<Integer, Integer> entry : amountMap.entrySet()) {

            ArrayList<Integer> value = new ArrayList<>();

            String key = colorIDMap.getEntry(entry.getKey()).blockName;
            value.add(colorIDMap.getEntry(entry.getKey()).colorID);
            value.add(entry.getValue());

            if (!sortingMap.containsKey(key)) {
                sortingMap.put(key, value);
            } else {
                value.set(1, value.get(1) + sortingMap.get(key).get(1));
                sortingMap.replace(key, value);
            }
        }

        StringBuilder amountString = new StringBuilder();

        amountString.append("You need these blocks:\n");
        amountString.append("|" + centerString("Blockname", 40) + "|" + centerString("BlockID", 10) + "|" + centerString("Amount", 10) + "|\n");
        for (Map.Entry<String, ArrayList<Integer>> entry : sortingMap.entrySet()) {
            String blockName = entry.getKey();
            String blockID = (colorIDMap.getEntry(entry.getValue().get(0)).blockID).replace("_", ":");
            Integer amount = entry.getValue().get(1);

            amountString.append(String.format("|%40s|%10s|%-10d|%n", blockName, blockID, amount));

            if (blockID.equals("9")) {
                waterAmount = amount;
            }
            if (blockID.equals("101")) {
                ironBarAmount = amount;
            }

        }

        if (waterAmount > 0) amountString.append("\nGlas blocks to surround Water (at most): " + (waterAmount * 5));
        if (ironBarAmount > 0)
            amountString.append("\nCobbelstone blocks to add below Iron Bars (at most): " + ironBarAmount);

        return amountString.toString();
    }

    /**
     * This method returns a {@link BufferedImage} object that shows an approximation of the resulting map in Minecraft
     *
     * @return BufferedImage approximating the result
     */
    public BufferedImage imageFromColorIDMatrix() {
        BufferedImage resultImage = new BufferedImage(width, length, 1);
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < length; z++) {

                int rgb = colorIDMap.getEntry(colorIDMatrix[x][z]).rgb;
                resultImage.setRGB(x, z, rgb);
            }
        }
        return resultImage;
    }

    /**
     * This method contains the logic that transforms an image object to the colorIDMatrix
     * It also adds an additional line of cobblestone at the north end of the matrix to provide correct shading
     *
     * @param image
     *         A {@link BufferedImage} object
     */
    private void imageToColorIDMatrix(BufferedImage image) {

        colorIDMatrix = new int[width][length];

        //This loop adds the additional line of cobblestone on the top.
        for (int[] colume : colorIDMatrix) {
            colume[0] = 45;
        }
        amountMap.put(45, width);

        HashMap<Integer, Integer> knownLinks = new HashMap<>();
        for (int x = 0; x < width; x++) {
            for (int z = 1; z < length; z++) {

                int rgb = image.getRGB(x, z - 1);

                /*
                Finds the entry in ColorIDMap with the closest rgb value to the rgb value of the pixel
                 */
                if (knownLinks.containsKey(rgb)) {
                    int tempcolorID = knownLinks.get(rgb);
                    colorIDMatrix[x][z] = tempcolorID;

                    /*If knownLinks already contains the rgb value, then amountMap has to contain the paired colorID*/
                    int count = amountMap.get(tempcolorID);
                    amountMap.replace(tempcolorID, count + 1);
                } else {
                    double curDif = 442d; //roughly the maximum distance between rgb(0,0,0) and rgb(255,255,255)
                    int curColorID = 0;
                    for (Map.Entry<Integer, MapIDEntry> entry : colorIDMap.getMap().entrySet()) {

                        double tempdif = rgbDistance(rgb, entry.getValue().rgb);
                        if (tempdif < curDif) {
                            curDif = tempdif;
                            curColorID = entry.getKey();
                        }
                    }

                    knownLinks.put(rgb, curColorID);
                    if (amountMap.containsKey(curColorID)) {
                        int count = amountMap.get(curColorID);
                        amountMap.replace(curColorID, count + 1);
                    } else {
                        amountMap.put(curColorID, 1);
                    }

                    colorIDMatrix[x][z] = curColorID;
                }
            }
        }
    }

    /**
     * This method calculates the distance between to rgb values using the three dimensional pythagoras
     *
     * @param rgbA
     *         An int containing the red value in byte 2, the green value in byte 3 and the blue value in byte 4 (left to right)
     * @param rgbB
     *         An int containing the red value in byte 2, the green value in byte 3 and the blue value in byte 4 (left to right)
     * @return A double value representing the distance between both input values
     */
    private double rgbDistance(int rgbA, int rgbB) {
        int difRed = ((byte) (rgbA >> 16) & 0xFF) - ((byte) (rgbB >> 16) & 0xFF);
        int difGreen = ((byte) (rgbA >> 8) & 0xFF) - ((byte) (rgbB >> 8) & 0xFF);
        int difBlue = ((byte) (rgbA) & 0xFF) - ((byte) (rgbB) & 0xFF);

        return Math.sqrt((difRed * difRed) + (difGreen * difGreen) + (difBlue * difBlue));
    }

    /**
     * Centers a string inside a certain length.
     * Provided by MerryCheese on Stackoverflow (https://stackoverflow.com/questions/8154366/how-to-center-a-string-using-string-format)
     *
     * @param text
     *         String to centerString
     * @param len
     *         length in wich to centerString
     * @return Centered String
     */
    private static String centerString(String text, int len) {
        if (len <= text.length())
            return text.substring(0, len);
        int before = (len - text.length()) / 2;
        if (before == 0)
            return String.format("%-" + len + "s", text);
        int rest = len - before;
        return String.format("%" + before + "s%-" + rest + "s", "", text);
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }
}

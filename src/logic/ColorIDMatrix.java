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

    private final ColorIDMap colorIDMap;
    private boolean cie;
    private final HashMap<Integer, Integer> amountMap = new HashMap<>();

    /**
     * Constructor
     *
     * @param imageFile
     *         A {@link File} object containing an image file
     * @param colorIDMap
     *         A {@link ColorIDMap}
     * @throws IOException
     */
    public ColorIDMatrix(@NotNull File imageFile, @NotNull ColorIDMap colorIDMap, boolean cie) throws IOException, IllegalArgumentException {
        this.colorIDMap = colorIDMap;
        this.cie = cie;
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null){
            throw new IllegalArgumentException("The chosen file was not an image or could not be opened.");
        }

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

        amountString.append(String.format("You need these blocks:%n"));
        amountString.append(String.format("|" + centerString("Blockname", 30) + "|" + centerString("BlockID", 45) +
                "|" + centerString("Amount", 10) + "|" + centerString("in Stacks", 10) + "|" + "%n"));
        for (Map.Entry<String, ArrayList<Integer>> entry : sortingMap.entrySet()) {
            String blockName = entry.getKey();
            String blockID = (colorIDMap.getEntry(entry.getValue().get(0)).blockID);
            Integer amount = entry.getValue().get(1);
            Double amountInStacks = (double)amount / 64d;

            amountString.append(String.format("|%-30s|%45s|%10d|%10.1f|%n", blockName, blockID, amount,amountInStacks));

            if (blockID.equals("minecraft:water")) {
                waterAmount = amount;
            }

        }

        if (waterAmount > 0) amountString.append("\nGlas blocks to surround Water (at most): " + (waterAmount * 5));


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
                    double curDif = Double.MAX_VALUE;
                    int curColorID = 0;
                    for (Map.Entry<Integer, MapIDEntry> entry : colorIDMap.getMap().entrySet()) {

                        double tempdif = colorDistance(rgb, entry.getValue().rgb, this.cie);
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
     * This method calculates the distance between to rgb values by converting to Lab values and calculating DeltaE2000.
     * Math found here: http://www.easyrgb.com/en/math.php See Delta E2000
     * Explained here: https://en.wikipedia.org/wiki/Color_difference
     *
     * @param rgbA
     *         An int containing the red value in byte 2, the green value in byte 3 and the blue value in byte 4 (left to right)
     * @param rgbB
     *         An int containing the red value in byte 2, the green value in byte 3 and the blue value in byte 4 (left to right)
     * @return A double value representing the distance between both input values
     */
    private double colorDistance(int rgbA, int rgbB, boolean cie) {
        int redA = (byte) (rgbA >> 16) & 0xFF;
        int redB = (byte) (rgbB >> 16) & 0xFF;
        int greenA = (byte) (rgbA >> 8) & 0xFF;
        int greenB = (byte) (rgbB >> 8) & 0xFF;
        int blueA = (byte) (rgbA) & 0xFF;
        int blueB = (byte) (rgbB) & 0xFF;
        if(cie){
            double[] labA = rgb2lab(redA,greenA,blueA);
            double[] labB = rgb2lab(redB,greenB,blueB);
            return deltaE2000squared(labA, labB);
        }
        else {
            double diffR = redA - redB;
            double diffG = greenA - greenB;
            double diffB = blueA - blueB;

            return diffR * diffR + diffG * diffG + diffB * diffB;
        }
    }

    private double deltaE2000squared(double[] labA, double[] labB){
        double l1 = labA[0], a1 = labA[1], b1 = labA[2];          //Color #1 CIE-L*ab values
        double l2 = labB[0];          //Color #2 CIE-L*ab values
        double a2 = labB[1];
        double b2 = labB[2];
        double weightL = 1; //Weight factors
        double weightC = 1;
        double weightH = 1;

        double xC1 = Math.sqrt( a1 * a1 + b1 * b1 );
        double xC2 = Math.sqrt( a2 * a2 + b2 * b2 );
        double xCX = ( xC1 + xC2 ) / 2;
        double xGX = 0.5 * ( 1 - Math.sqrt( Math.pow(xCX, 7) / ( Math.pow(xCX, 7) + Math.pow(25, 7 ))));
        double xNN = ( 1 + xGX ) * a1;
        xC1 = Math.sqrt( xNN * xNN + b1 * b1 );
        double xH1 = cieLab2Hue( xNN, b1 );
        xNN = ( 1 + xGX ) * a2;
        xC2 = Math.sqrt( xNN * xNN + b2 * b2 );
        double xH2 = cieLab2Hue( xNN, b2 );
        double xDL = l2 - l1;
        double xDC = xC2 - xC1;
        double xDH;
        if ( ( xC1 * xC2 ) == 0 ) xDH = 0;
        else {
            xNN = xH2 - xH1;
            if ( Math.abs( xNN ) <= 180 ) {
                xDH = xH2 - xH1;
            }
            else {
                if ( xNN > 180 ) xDH = xH2 - xH1 - 360;
                else             xDH = xH2 - xH1 + 360;
            }
        }

        xDH = 2 * Math.sqrt( xC1 * xC2 ) * Math.sin( Math.toRadians( xDH / 2 ) );
        double xLX = ( l1 + l2 ) / 2;
        double xCY = ( xC1 + xC2 ) / 2;
        double xHX;
        if ( ( xC1 *  xC2 ) == 0 ) {
            xHX = xH1 + xH2;
        }
        else {
            xNN = Math.abs( xH1 - xH2 );
            if ( xNN >  180 ) {
                if ( ( xH2 + xH1 ) <  360 ) xHX = xH1 + xH2 + 360;
                else                        xHX = xH1 + xH2 - 360;
            }
            else {
                xHX = xH1 + xH2;
            }
            xHX /= 2;
        }
        double xTX = 1 - 0.17 * Math.cos( Math.toRadians( xHX - 30 ) ) + 0.24
                * Math.cos( Math.toRadians( 2 * xHX ) ) + 0.32
                * Math.cos( Math.toRadians( 3 * xHX + 6 ) ) - 0.20
                * Math.cos( Math.toRadians( 4 * xHX - 63 ) );
        double xPH = 30 * Math.exp( - ( ( xHX  - 275 ) / 25 ) * ( ( xHX  - 275 ) / 25 ) );
        double xRC = 2 * Math.sqrt( Math.pow(xCY, 7) / ( Math.pow( xCY, 7 ) + Math.pow( 25, 7 ) ) );
        double xSL = 1 + ( ( 0.015 * ( ( xLX - 50 ) * ( xLX - 50 ) ) )
                / Math.sqrt( 20 + ( ( xLX - 50 ) * ( xLX - 50 ) ) ) );

        double xSC = 1 + 0.045 * xCY;
        double xSH = 1 + 0.015 * xCY * xTX;
        double xRT = - Math.sin( Math.toRadians( 2 * xPH ) ) * xRC;
        xDL = xDL / ( weightL * xSL );
        xDC = xDC / ( weightC * xSC );
        xDH = xDH / ( weightH * xSH );

        return Math.pow(xDL, 2) + Math.pow(xDC, 2) + Math.pow(xDH, 2) + xRT * xDC * xDH;
    }

    private double cieLab2Hue(double var_a, double var_b) {
        int var_bias = 0;
        if ( var_a >= 0 && var_b == 0 ) return 0;
        if ( var_a <  0 && var_b == 0 ) return 180;
        if ( var_a == 0 && var_b >  0 ) return 90;
        if ( var_a == 0 && var_b <  0 ) return 270;
        if ( var_a >  0 && var_b >  0 ) var_bias = 0;
        if ( var_a <  0               ) var_bias = 180;
        if ( var_a >  0 && var_b <  0 ) var_bias = 360;
        return ( Math.toDegrees(Math.atan( var_b / var_a )) + var_bias );


    }

    /**
     * Code taken from User Thanasis1101 on stackoverflow.com<br>
     *     https://stackoverflow.com/a/45263428
     * With math explained here: http://www.easyrgb.com/en/math.php
     * @param R Red channel value
     * @param G Green channel value
     * @param B Blue channel value
     * @return Array with L, a, and b, values on index 0,1,2.
     */
    private double[] rgb2lab(int R, int G, int B) {
        //http://www.brucelindbloom.com
        double[] lab = new double[3];

        float r, g, b, X, Y, Z, fx, fy, fz, xr, yr, zr;
        float eps = 216.f/24389.f;
        float k = 24389.f/27.f;

        float Xr = 0.964221f;  // reference white D50
        float Yr = 1.0f;
        float Zr = 0.825211f;

        // RGB to XYZ
        r = R/255.f; //R 0..1
        g = G/255.f; //G 0..1
        b = B/255.f; //B 0..1

        // assuming sRGB (D65)
        if (r <= 0.04045)
            r = r/12;
        else
            r = (float) Math.pow((r+0.055)/1.055,2.4);

        if (g <= 0.04045)
            g = g/12;
        else
            g = (float) Math.pow((g+0.055)/1.055,2.4);

        if (b <= 0.04045)
            b = b/12;
        else
            b = (float) Math.pow((b+0.055)/1.055,2.4);


        X =  0.436052025f*r     + 0.385081593f*g + 0.143087414f *b;
        Y =  0.222491598f*r     + 0.71688606f *g + 0.060621486f *b;
        Z =  0.013929122f*r     + 0.097097002f*g + 0.71418547f  *b;

        // XYZ to Lab
        xr = X/Xr;
        yr = Y/Yr;
        zr = Z/Zr;

        if ( xr > eps )
            fx =  (float) Math.pow(xr, 1/3.);
        else
            fx = (float) ((k * xr + 16.) / 116.);

        if ( yr > eps )
            fy =  (float) Math.pow(yr, 1/3.);
        else
            fy = (float) ((k * yr + 16.) / 116.);

        if ( zr > eps )
            fz =  (float) Math.pow(zr, 1/3.);
        else
            fz = (float) ((k * zr + 16.) / 116);

        lab[0] = ( 116 * fy ) - 16;
        lab[1] = 500*(fx-fy);
        lab[2] = 200*(fy-fz);

        return lab;
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

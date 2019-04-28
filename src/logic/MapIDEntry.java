package logic;

import org.jetbrains.annotations.NotNull;

/**
 * This class provides a data structure for a single color of the colorIDMap.
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
public class MapIDEntry {

    public final int colorID;
    /**
     * This int contains all rgb values by shifting the red value by 16 bits, the green value by 8 bits and the blue value by zero bits.
     * The alpha value is shifted by 24 bits.
     */
    public final int rgb;
    public final String blockName;
    public final String blockID;

    public MapIDEntry(int colorID, int rgb, @NotNull String blockName, @NotNull String blockID) {
        this.colorID = colorID;
        this.rgb = rgb;
        this.blockName = blockName;
        this.blockID = blockID;
    }

    /**
     * @return The color value for red from the rgb value
     */
    public int getRed() {
        return (byte) (rgb >> 16) & 0xFF;
    }

    /**
     * @return The color value for green from the rgb value
     */
    public int getGreen() {
        return (byte) (rgb >> 8) & 0xFF;
    }

    /**
     * @return The color value for blue from the rgb value
     */
    public int getBlue() {
        return (byte) (rgb) & 0xFF;
    }
}

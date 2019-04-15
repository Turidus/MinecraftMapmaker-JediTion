package main;

import java.util.List;

public class MapIDEntry {

    public int colorID;
    /**
     * This int contains all rgb values by shifting the red value by 16 bits, the green value by 8 bits and the blue value by zero bits.
     * The alpha value is shifted by 24 bits.
     */
    public int rgb;
    public String blockName;
    public String blockID;

    public MapIDEntry(int colorID, int rgb, String blockName, String blockID){
        this.colorID = colorID;
        this.rgb = rgb;
        this.blockName = blockName;
        this.blockID = blockID;
    }

    /**
     *
     * @return The color value for red
     */
    public int getRed(){
        return (byte)(rgb >> 16) & 0xFF;
    }

    /**
     *
     * @return The color value for green
     */
    public int getGreen(){
        return (byte)(rgb >> 8) & 0xFF;
    }

    /**
     *
     * @return The color value for blue
     */
    public int getBlue(){
        return (byte)(rgb) & 0xFF;
    }
}

package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ColorIDMatrix {


    private int[][] colorIDMatrix;
    private ColorIDMap colorIDMap;

    public ColorIDMatrix(File file, ColorIDMap colorIDMap) throws IOException {
        this.colorIDMap = colorIDMap;
        fileToColorIDMatrix(file);
    }

    private void fileToColorIDMatrix(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        colorIDMatrix = new int[image.getWidth()][image.getHeight()];

        HashMap<Integer,Integer> knownLinks = new HashMap<>();


        for (int x = 0; x < image.getWidth(); x++){
            for (int y = 0; y < image.getHeight(); y++){

                int rgb = image.getRGB(x, y);

                /*
                Finds the entry in ColorIDMap with the closest rgb value to the rgb value of the pixel
                 */
                if(knownLinks.containsKey(rgb)) colorIDMatrix[x][y] = knownLinks.get(rgb);
                else {
                    double curDif = 450d; //roughly the maximum distance between rgb(0,0,0) and rgb(255,255,255)
                    int curColorID = 0;
                    for (Map.Entry<Integer,MapIDEntry> entry : colorIDMap.getMap().entrySet()){

                        double tempdif = rgbDistance(rgb,entry.getValue().rgb);
                        if (tempdif < curDif) {
                            curDif = tempdif;
                            curColorID = entry.getKey();
                        }
                    }

                    knownLinks.put(rgb,curColorID);
                    colorIDMatrix[x][y] = curColorID;
                }
            }
        }
    }

    private double rgbDistance(int rgbA, int rgbB){
        int difRed = ((byte)(rgbA >> 16) & 0xFF) - ((byte)(rgbB >> 16) & 0xFF);
        int difGreen = ((byte)(rgbA >> 8) & 0xFF) - ((byte)(rgbB >> 8) & 0xFF);
        int difBlue = ((byte)(rgbA) & 0xFF) - ((byte)(rgbB) & 0xFF);

        return Math.sqrt((difRed * difRed) + (difGreen * difGreen) + (difBlue * difBlue));
    }

    public MapIDEntry getEntryfromPoint(int x, int y){
        return colorIDMap.getMap().get(colorIDMatrix[x][y]);
    }

}

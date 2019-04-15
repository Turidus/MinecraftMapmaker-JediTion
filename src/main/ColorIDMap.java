package main;

import com.sun.istack.internal.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
        Generates the mapColorID dictionary out of the BaseColorIDList.txt and a optional blacklist.

        Made by Turidus https://github.com/Turidus/Minecraft-MapMaker
        Copyright (c) 2018 Turidus

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in all
        copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
        SOFTWARE.
*/
public class ColorIDMap {

    private HashMap<Integer, MapIDEntry> map = new HashMap<>();

    public ColorIDMap(boolean threeD, @NotNull List<String> blacklist) throws IOException {
        if (threeD) mapColorIDGenerator3D(blacklist);
        else mapColorIDGenerator2D(blacklist);
    }

    private int mult180(int x){
        return (x * 180) / 255;
    }

    private int mult220(int x){
        return (x * 220) / 255;
    }

    private void mapColorIDGenerator3D(@NotNull List<String> blacklist) throws IOException {

        List<String> lines = new ArrayList<>();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader("ressources/BaseColorIDs"));
        while ((line = reader.readLine()) != null){
            lines.add(line);
        }
        reader.close();

        lines.remove(0);
        for(String item : lines){
            String[] firstSplit = item.split("\t");
            if (blacklist.contains(firstSplit[0])) continue;

            String[] secondSplit = firstSplit[1].split(",");

            int[] rgbArray = {Integer.parseInt(secondSplit[0].trim()),Integer.parseInt(secondSplit[1].trim()),Integer.parseInt(secondSplit[2].trim())};

            int rgb180 = (mult180(rgbArray[0]) << 16 | mult180(rgbArray[1]) << 8 | mult180(rgbArray[2]));
            int colorID180 = Integer.parseInt(firstSplit[0].trim()) * 4;

            int rgb220 = (mult220(rgbArray[0]) << 16 | mult220(rgbArray[1]) << 8 | mult220(rgbArray[2]));
            int colorID220 = Integer.parseInt(firstSplit[0].trim()) * 4 + 1;

            int rgb255 = (rgbArray[0] << 16 | rgbArray[1] << 8 | rgbArray[2]);
            int colorID255 = Integer.parseInt(firstSplit[0].trim()) * 4 + 2;


            map.put(colorID180, new MapIDEntry(colorID180, rgb180,firstSplit[2],firstSplit[3]));
            map.put(colorID220, new MapIDEntry(colorID220, rgb220,firstSplit[2],firstSplit[3]));
            map.put(colorID255, new MapIDEntry(colorID255, rgb255,firstSplit[2],firstSplit[3]));
        }
    }

    private void mapColorIDGenerator2D(@NotNull List<String> blacklist) throws IOException {

        List<String> lines = new ArrayList<>();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader("ressources/BaseColorIDs"));
        while ((line = reader.readLine()) != null){
            lines.add(line);
        }
        reader.close();

        lines.remove(0);
        for(String item : lines){
            String[] firstSplit = item.split("\t");
            if (blacklist.contains(firstSplit[0])) continue;

            String[] secondSplit = firstSplit[1].split(",");

            int[] rgbArray = {Integer.parseInt(secondSplit[0].trim()),Integer.parseInt(secondSplit[1].trim()),Integer.parseInt(secondSplit[2].trim())};

            int rgb220 = (mult220(rgbArray[0]) <<16 | mult220(rgbArray[1]) << 8 | mult220(rgbArray[2]));
            int colorID220 = Integer.parseInt(firstSplit[0].trim()) * 4 + 1;

            map.put(colorID220, new MapIDEntry(colorID220, rgb220,firstSplit[2],firstSplit[3]));
        }
    }

    public HashMap<Integer, MapIDEntry> getMap() {
        return map;
    }
}

package logic;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class holds the configuration values, which it reads out of the config file
 * pathToImage and name can be null and have to be set later.
 *
 * It acts as a singelton
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
public class ConfigStore {

    private static ConfigStore single_instance = null;

    @Nullable
    public String pathToImage = null;

    @Nullable
    public String name = null;

    @Nullable
    public List<MapIDEntry> blocksToUse = null;

    public String pathToSave = "save/";

    public List<String> blacklist = new ArrayList<>();

    public boolean threeD = true;

    public boolean picture = true;

    public boolean positionFile = true;

    public boolean amountFile = true;

    public boolean schematic = true;

    public int minY = 4;

    public int maxY = 250;

    public int maxS = 129;



    /**
     * Set private to stop instantiation
     *
     * @throws FileNotFoundException If config file was not found
     */
    private ConfigStore() throws FileNotFoundException {
        loadConfig(false);
    }

    /**
     * Returns the singelton instance of the config store
     * @throws FileNotFoundException if config file was not found
     */
    public static ConfigStore getInstance() throws FileNotFoundException {
        if (single_instance == null) single_instance = new ConfigStore();
        return single_instance;
    }

    /**
     * Loads the default vaules for the config
     * @throws FileNotFoundException if config-default file is not found
     */
    public void loadDefault() throws FileNotFoundException {
        loadConfig(true);
    }

    /**
     * This Method takes the current state of the ConfigStore and writes it to the config file.
     *
     * @throws FileNotFoundException If config file was not found
     * @throws IllegalAccessException If a field in ConfigStore was not accessable
     */
    public void setCurrentAsDefault() throws FileNotFoundException, IllegalAccessException {
        BufferedReader reader;

        try{
            reader = new BufferedReader(new FileReader("resources/config"));
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("The config file could not be opened. " + e.getMessage());
        }

        String[] lines = reader.lines().toArray(String[]::new);

        for (Field field : this.getClass().getFields()){

            String fieldName = field.getName();

            if (fieldName.equals(pathToImage) || fieldName.equals(name)) continue;

            for (int i = 0; i < lines.length; i++){
                if (!lines[i].startsWith(fieldName)) continue;

                StringBuilder newline = new StringBuilder(fieldName + " = ");
                Object fieldValue = field.get(this);

                switch (fieldName) {
                    case "blacklist":
                        if (blacklist.isEmpty()) break;
                        for (String item : blacklist) {
                            newline.append(item);
                            newline.append(",");
                        }
                        newline.replace(newline.lastIndexOf(","),newline.lastIndexOf(",") + 1,"");
                        break;
                    default:
                        newline.append(fieldValue);
                        break;
                }
                lines[i] = newline.toString();
                break;
            }
        }

        try {
            File file = new File("resources/config");
            try (PrintStream out = new PrintStream(new FileOutputStream(file))) {
                for (String line : lines){
                    out.println(line);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the config values from the config or config-default file
     * @param backup If true, the values are loaded from config-default, else config is used
     * @throws FileNotFoundException If config or config-default are not found
     */
    private void loadConfig(boolean backup) throws FileNotFoundException {
        BufferedReader reader;

        try{
            if(!backup) reader = new BufferedReader(new FileReader("resources/config"));
            else reader = new BufferedReader(new FileReader("resources/config-default"));
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("The config file could not be opened. " + e.getMessage());
        }

        for (String line : reader.lines().toArray(String[]::new)) {
            line = line.replace(" ", "");

            if (line.startsWith("#") || line.length() == 0) continue;

            String[] split = line.split("=");

            if (split.length <= 1) continue;

            for (Field field : this.getClass().getFields()) {
                if (!field.getName().equals(split[0])) continue;

                switch (split[0]) {
                    case "pathToImage":
                    case "name":
                    case "pathToSave":
                        try {
                            field.set(this, split[1]);
                        } catch (IllegalAccessException e) {
                        }
                        break;
                    case "blacklist":
                        String[] ids = split[1].split(",");
                        try {
                            field.set(this, new ArrayList<>(Arrays.asList(ids)));
                        } catch (Exception e) {
                        }
                        break;
                    case "threeD":
                    case "picture":
                    case "positionFile":
                    case "amountFile":
                    case "schematic":
                        try {
                            field.set(this, Boolean.valueOf(split[1]));
                        } catch (Exception e) {
                        }

                        break;
                    case "minY":
                    case "maxY":
                    case "maxS":
                        try {
                            field.set(this, Integer.valueOf(split[1]));
                        } catch (Exception e) {
                        }
                        break;
                }
                break;
            }
        }
    }
}

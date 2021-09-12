package logic;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * This class holds the configuration values, which it reads out of the config.txt file
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

    public static class McVersion{


        private final String version;
        private final int dataVersion;
        private McVersion(String version, int dataVersion){

            this.version = version;
            this.dataVersion = dataVersion;
        }

        public String getVersion() {
            return version;
        }

        public int getDataVersion() {
            return dataVersion;
        }

    }

    private static ConfigStore single_instance = null;

    /**
     * This map contains the max color ID used by version.
     * {@code Map<dataVersion,colorID>}
     */
    public static Map<Integer,Integer> maxColorIDUsedByVersion;

    public static List<McVersion> mcVersionList;
    static {
        mcVersionList = new ArrayList<>();
        mcVersionList.add(new McVersion("1.13", 1519));
        mcVersionList.add(new McVersion("1.14", 1952));
        mcVersionList.add(new McVersion("1.15", 2225));
        mcVersionList.add(new McVersion("1.16", 2566));

        maxColorIDUsedByVersion = new HashMap<>();
        maxColorIDUsedByVersion.put(1519, 51);
        maxColorIDUsedByVersion.put(1952, 51);
        maxColorIDUsedByVersion.put(2225, 51);
        maxColorIDUsedByVersion.put(2566, 58);
    }


    @Nullable
    public String pathToImage = null;

    @Nullable
    public String name = null;

    @Nullable
    public ArrayList<MapIDEntry> blocksToUse = null;

    @Nullable
    public ArrayList<MapIDEntry> selectedBlocks = null;

    public String pathToSave = "save/";

    public List<String> blacklist = new ArrayList<>();

    public boolean threeD = true;

    public boolean picture = true;

    public boolean positionFile = true;

    public boolean amountFile = true;

    public boolean schematic = true;

    public boolean spongeSchematic = false;

    public boolean cie = true;

    public int minY = 4;

    public int maxY = 250;

    public int maxX = 0;

    public int maxZ = 0;

    public int maxS = 129;

    public int mcDataVersion = 1519;



    /**
     * Set private to stop instantiation
     *
     * @throws FileNotFoundException If config.txt file was not found
     */
    private ConfigStore() throws FileNotFoundException, ClassNotFoundException {
        loadConfig(false);
    }

    /**
     * Returns the singelton instance of the config store
     * @throws FileNotFoundException if config.txt file was not found
     */
    public static ConfigStore getInstance() throws FileNotFoundException, ClassNotFoundException {
        if (single_instance == null) single_instance = new ConfigStore();
        return single_instance;
    }

    /**
     * Loads the default values for the config
     * @throws FileNotFoundException if config-default file is not found
     */
    public void loadDefault() throws FileNotFoundException, ClassNotFoundException {
        loadConfig(true);
    }

    /**
     * This Method takes the current state of the ConfigStore and writes it to the config.txt file.
     *
     * @throws FileNotFoundException If config.txt file was not found
     * @throws IllegalAccessException If a field in ConfigStore was not accessible
     */
    public void saveCurrent() throws FileNotFoundException, IllegalAccessException {
        /*
        Saving data to the config.txt file
         */

        BufferedReader reader;

        try{
            reader = new BufferedReader(new FileReader("resources/config.txt"));
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("The config.txt file could not be opened. " + e.getMessage());
        }

        String[] lines = reader.lines().toArray(String[]::new);

        for (Field field : this.getClass().getFields()){

            String fieldName = field.getName();

            if (fieldName.equals(pathToImage) || fieldName.equals(name)) continue;

            for (int i = 0; i < lines.length; i++){
                if (!lines[i].startsWith(fieldName)) continue;

                StringBuilder newline = new StringBuilder(fieldName + " = ");
                Object fieldValue = field.get(this);

                //noinspection SwitchStatementWithTooFewBranches This switch statement increases code readability, only get called once
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
            File file = new File("resources/config.txt");
            try (PrintStream out = new PrintStream(new FileOutputStream(file))) {
                for (String line : lines){
                    out.println(line);
                }

            }
        } catch (IOException e) {
            throw new FileNotFoundException("The config.txt file could not be opened. " + e.getMessage());
        }


        /*
        Saving data to the usedBlock file
         */
        if(this.blocksToUse != null){
            try(FileOutputStream fileOutputStream = new FileOutputStream("resources/selectedBlocks")){
                try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)){
                    objectOutputStream.writeObject(this.blocksToUse);
                }
            } catch (IOException e) {
                throw new FileNotFoundException("The blockUsed file could not be opened. " + e.getMessage());
            }
        }
    }

    /**
     * Loads the config.txt values from the config.txt or config.txt-default file
     * @param fromDefault If true, the values are loaded from config.txt-default, else config.txt is used
     * @throws FileNotFoundException If config.txt or config.txt-default are not found
     */
    private void loadConfig(boolean fromDefault) throws FileNotFoundException, ClassNotFoundException {
        /*
        Loading in values from the config.txt or config.txt default file
         */

        BufferedReader reader;
        String[] stringArray;

        try{
            if(!fromDefault) {
                reader = new BufferedReader(new FileReader("resources/config.txt"));
                stringArray = reader.lines().toArray(String[]::new);
            }
            else {
                try(InputStream inputStream = getClass().getResourceAsStream("/config-default")){
                reader = new BufferedReader(new InputStreamReader(inputStream));
                stringArray = reader.lines().toArray(String[]::new);
                }
            }
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("The config.txt file could not be opened. " + e.getMessage());
        }catch (IOException e){
            throw new FileNotFoundException("The config-default file could not be opened. " + e.getMessage());
        }

        for (String line : stringArray) {

            String[] split;
            if (line.startsWith("#") || line.length() == 0) continue;
            if (line.startsWith("path")){
                split = line.split("=");
                split[0] = split[0].trim();
                split[1] = split[1].trim();
            }
            else {
                line = line.replace(" ", "");
                split = line.split("=");
            }

            if (split.length <= 1) continue;

            for (Field field : this.getClass().getFields()) {
                if (!field.getName().equals(split[0])) continue;

                switch (split[0]) {
                    case "pathToImage":
                    case "name":
                    case "pathToSave":
                        try {
                            field.set(this, split[1]);
                        } catch (IllegalAccessException ignored) {
                        }
                        break;
                    case "blacklist":
                        String[] ids = split[1].split(",");
                        try {
                            field.set(this, new ArrayList<>(Arrays.asList(ids)));
                        } catch (Exception ignored) {
                        }
                        break;
                    case "threeD":
                    case "picture":
                    case "positionFile":
                    case "amountFile":
                    case "schematic":
                    case "cie":
                        try {
                            field.set(this, Boolean.valueOf(split[1]));
                        } catch (Exception ignored) {
                        }

                        break;
                    case "minY":
                    case "maxY":
                    case "maxX":
                    case "maxZ":
                    case "maxS":
                    case "mcDataVersion":
                        try {
                            field.set(this, Integer.valueOf(split[1]));
                        } catch (Exception ignored) {
                        }
                        break;
                }
                break;
            }
        }

        /*
        Loading in selectedBlocks from usedBlock file. The selectedBlocks file is an serialized {@code ArrayList<MapIDEntry>}
         */
        if(fromDefault) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(getClass().getResourceAsStream("/selectedBlocks-default"))){

                this.selectedBlocks = (ArrayList<MapIDEntry>) objectInputStream.readObject();

            } catch (IOException e) {
                throw new FileNotFoundException("The selectedBlocks-default file could not be opened. " + e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new ClassNotFoundException("The selectedBlocks-default file could not be deserialized. " + e.getMessage());
            }
        }
        else {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("resources/selectedBlocks"))){

                this.selectedBlocks = (ArrayList<MapIDEntry>) objectInputStream.readObject();

            } catch (IOException e) {
                throw new FileNotFoundException("The selectedBlocks-default file could not be opened. " + e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new ClassNotFoundException("The selectedBlocks-default file could not be deserialized. " + e.getMessage());
            }
        }
    }
}

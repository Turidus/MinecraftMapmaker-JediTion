package logic;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigStore {

    private static ConfigStore single_instance = null;

    @Nullable
    public String pathToImage = null;

    @Nullable
    public String name = null;

    public String pathToSave = "save/";

    public List<String> blackList = new ArrayList<>();

    public boolean threeD = true;

    public boolean picture = true;

    public boolean positionFile = true;

    public boolean amountFile = true;

    public boolean schematic = true;

    public int minY = 4;

    public int maxY = 250;

    public int maxS = 129;

    private ConfigStore() throws FileNotFoundException {
        BufferedReader reader;

        try{
            reader = new BufferedReader(new FileReader("resources/config"));
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

    public static ConfigStore getInstance() throws FileNotFoundException {
        if (single_instance == null) single_instance = new ConfigStore();
        return single_instance;
    }

    public void setCurrentAsDefault(){
        //TODO implement

    }
}

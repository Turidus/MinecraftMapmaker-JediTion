package logic;

import nbt.Tag_Compound;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

public class MapmakerCore implements Runnable {

    private ConfigStore configStore;

    public MapmakerCore() {
        try {
            this.configStore = ConfigStore.getInstance();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (configStore.pathToImage == null) throw new IllegalArgumentException("No file was given");
        if (configStore.name == null) {
            String[] pathSplit = configStore.pathToImage.split("[" + Pattern.quote(File.pathSeparator) + "/]");
            configStore.name = pathSplit[pathSplit.length - 1].split(Pattern.quote("."))[0];
            configStore.pathToSave += configStore.name + "/";
        } else {
            configStore.name = configStore.name.replaceAll("[^a-zA-Z0-9_\\-]", "");
            configStore.pathToSave += configStore.name + "/";
        }

        ColorIDMatrix colorIDMatrix = null;
        PositionMatrix positionMatrix = null;

        try {

            File file = new File(configStore.pathToImage);
            ColorIDMap colorIDMap = new ColorIDMap(configStore.threeD, configStore.blacklist);
            colorIDMatrix = new ColorIDMatrix(file, colorIDMap);
            positionMatrix = new PositionMatrix(colorIDMatrix);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (configStore.picture) {
            BufferedImage image = colorIDMatrix.imageFromColorIDMatrix();
            try {
                File file = new File(configStore.pathToSave + configStore.name + ".png");
                file.getParentFile().mkdirs();
                file.createNewFile();
                ImageIO.write(image, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (configStore.amountFile) {
            String amount = colorIDMatrix.getAmountString();

            try {
                File file = new File(configStore.pathToSave + configStore.name + "_amount.txt");
                file.getParentFile().mkdirs();
                file.createNewFile();
                try (PrintStream out = new PrintStream(new FileOutputStream(file))) {
                    out.print(amount);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (configStore.positionFile) {
            String position = positionMatrix.getPositionString();
            try {
                File file = new File(configStore.pathToSave + configStore.name + "_position.txt");
                file.getParentFile().mkdirs();
                file.createNewFile();
                try (PrintStream out = new PrintStream(new FileOutputStream(file))) {
                    out.print(position);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (configStore.schematic) {
            List<Tag_Compound> tagCompoundList = positionMatrix.getTag_CompoundList();
            try {
                for (Tag_Compound tagC : tagCompoundList) {
                    String[] tagCName = tagC.getName().split(" ");
                    File file = new File(configStore.pathToSave + configStore.name + "_Z" + tagCName[0] + "-X" + tagCName[1] + ".schematic");
                    file.getParentFile().mkdirs();
                    file.createNewFile();

                    tagC.setName("Schematic");

                    ByteArrayOutputStream byteArrayOutputStream = tagC.toBytes();

                    try (OutputStream out = new GZIPOutputStream(new FileOutputStream(file))) {
                        byteArrayOutputStream.writeTo(out);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

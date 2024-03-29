package de.turidus.minecraft_mapmaker.logic;

import de.turidus.minecraft_mapmaker.events.CriticalExceptionEvent;
import de.turidus.minecraft_mapmaker.events.DoneEvent;
import de.turidus.minecraft_mapmaker.events.MessageEvent;
import de.turidus.minecraft_mapmaker.events.NonCriticalExceptionEvent;
import de.turidus.minecraft_mapmaker.nbt.Tag_Compound;
import de.turidus.minecraft_mapmaker.utils.ConfigStore;
import org.greenrobot.eventbus.EventBus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class MapmakerCore implements Runnable {

    private ConfigStore configStore;

    public MapmakerCore() {
        try {
            this.configStore = ConfigStore.getInstance();
        } catch (FileNotFoundException | ClassNotFoundException e) {
            EventBus.getDefault().post(new CriticalExceptionEvent(e.getMessage(),e));
        }
    }

    @Override
    public void run() {
        EventBus.getDefault().post(new MessageEvent("Started Processing"));
        final long time = System.currentTimeMillis();
        ColorIDMatrix colorIDMatrix;
        PositionMatrix positionMatrix;

        try {
            if(configStore.blocksToUse == null) throw new InvalidObjectException("Blocks to use was not set");
            if(configStore.pathToImage == null) throw new InvalidObjectException("Path to image was not set");
            File file = new File(configStore.pathToImage);
            ColorIDMap colorIDMap = new ColorIDMap(configStore.threeD, configStore.spongeSchematic, configStore.blocksToUse);
            colorIDMatrix = new ColorIDMatrix(file, colorIDMap, configStore.cie);
            EventBus.getDefault().post(new MessageEvent("ColorIDMatrix was calculated"));
            positionMatrix = new PositionMatrix(colorIDMatrix);
            EventBus.getDefault().post(new MessageEvent("PositionMatrix was calculated"));

        }
        catch (InvalidObjectException | ClassNotFoundException e){
            String tw = "Turidus did something wrong: ";
            EventBus.getDefault().post(new CriticalExceptionEvent( tw + e.getMessage(),e));
            return;
        }
        catch (IllegalArgumentException | IOException e){
            EventBus.getDefault().post(new NonCriticalExceptionEvent( e.getMessage(),e));
            return;
        }

        if (configStore.picture) {
            BufferedImage image = colorIDMatrix.imageFromColorIDMatrix();
            try {
                File file = new File(configStore.pathToSave + configStore.name + ".png");
                file.getParentFile().mkdirs();
                file.createNewFile();
                ImageIO.write(image, "png", file);
            } catch (IOException e) {
                EventBus.getDefault().post(new NonCriticalExceptionEvent( "Image could not be saved",e));
            }
            EventBus.getDefault().post(new MessageEvent("Image was saved"));
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
                EventBus.getDefault().post(new NonCriticalExceptionEvent( "Amount file could not be saved",e));
            }
            EventBus.getDefault().post(new MessageEvent("Amount file was saved"));
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
                EventBus.getDefault().post(new NonCriticalExceptionEvent( "Position file could not be saved",e));
            }
            EventBus.getDefault().post(new MessageEvent("Position file was saved"));
        }

        if (configStore.schematic) {
            List<Tag_Compound> tagCompoundList = positionMatrix.getTag_CompoundList();
            if(configStore.spongeSchematic) saveSpongeSchematic(tagCompoundList);
            else saveLitematicaSchematic(tagCompoundList);
        }
        EventBus.getDefault().post(new MessageEvent("Time passed: " + (System.currentTimeMillis() - time)/1000d + " s"));
        EventBus.getDefault().post(new MessageEvent("Done!"));
        EventBus.getDefault().post(new DoneEvent());
    }

    private void saveLitematicaSchematic(List<Tag_Compound> tagCompoundList) {
        try {
            for (Tag_Compound tagC : tagCompoundList) {
                String[] tagCName = tagC.getName().split(" ");
                File file = new File(configStore.pathToSave + configStore.name + "_Z" +
                        tagCName[0] + "-X" + tagCName[1]);
                file.getParentFile().mkdirs();
                file.createNewFile();

                tagC.setName(configStore.name + "_Z" + tagCName[0] + "-X" + tagCName[1]);

                ByteArrayOutputStream byteArrayOutputStream = tagC.toBytes();

                try (OutputStream out = new GZIPOutputStream(new FileOutputStream(file))) {
                    byteArrayOutputStream.writeTo(out);
                }
            }
            EventBus.getDefault().post(new MessageEvent("Schematics where saved"));
        } catch (IOException e) {
            EventBus.getDefault().post(new NonCriticalExceptionEvent( "Schematic files could not be saved",e));
        }
    }

    private void saveSpongeSchematic(List<Tag_Compound> tagCompoundList) {
        try {
            for (Tag_Compound tagC : tagCompoundList) {
                String[] tagCName = tagC.getName().split(" ");
                File file = new File(configStore.pathToSave + configStore.name + "_Z" +
                        tagCName[0] + "-X" + tagCName[1] + ".schem");
                file.getParentFile().mkdirs();
                file.createNewFile();

                tagC.setName("Schematic");

                ByteArrayOutputStream byteArrayOutputStream = tagC.toBytes();

                try (OutputStream out = new GZIPOutputStream(new FileOutputStream(file))) {
                    byteArrayOutputStream.writeTo(out);
                }
            }
            EventBus.getDefault().post(new MessageEvent("Schematics where saved"));
        } catch (IOException e) {
            EventBus.getDefault().post(new NonCriticalExceptionEvent( "Schematic files could not be saved",e));
        }
    }
}

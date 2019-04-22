package logic;

import events.CriticalExceptionEvent;
import events.MessageEvent;
import events.NonCriticalExceptionEvent;
import nbt.Tag_Compound;
import org.greenrobot.eventbus.EventBus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Timer;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

public class MapmakerCore implements Runnable {

    private ConfigStore configStore;
    private String tw = "Turidus did something wrong: ";

    public MapmakerCore() {
        try {
            this.configStore = ConfigStore.getInstance();
        } catch (FileNotFoundException e) {
            EventBus.getDefault().post(new CriticalExceptionEvent("Configuration File was not found",e));
        }
    }

    @Override
    public void run() {
        final long time = System.currentTimeMillis();
        ColorIDMatrix colorIDMatrix = null;
        PositionMatrix positionMatrix = null;

        try {
            if(configStore.blocksToUse == null) throw new IllegalArgumentException("Blocks to use was not set");
            File file = new File(configStore.pathToImage);
            ColorIDMap colorIDMap = new ColorIDMap(configStore.threeD, configStore.blocksToUse);
            colorIDMatrix = new ColorIDMatrix(file, colorIDMap);
            positionMatrix = new PositionMatrix(colorIDMatrix);

        }
        catch (IllegalArgumentException e){
            EventBus.getDefault().post(new CriticalExceptionEvent( tw + e.getMessage(),e));
            return;
        }
        catch (IOException e) {
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
                EventBus.getDefault().post(new NonCriticalExceptionEvent( "Schematic files could not be saved",e));
            }
        }
        EventBus.getDefault().post(new MessageEvent(String.valueOf(System.currentTimeMillis() - time)));
        EventBus.getDefault().post(new MessageEvent("Done!"));
    }
}

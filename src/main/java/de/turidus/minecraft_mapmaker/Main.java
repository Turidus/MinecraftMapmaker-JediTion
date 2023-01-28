package de.turidus.minecraft_mapmaker;

import de.turidus.minecraft_mapmaker.utils.FaF;
import de.turidus.minecraft_mapmaker.utils.FileHandler;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class.getSimpleName());
    public static void main(String[] args) {
        setUp();
        logger.info(" ---------------------------------------- ");
        logger.info(" ---------------------------------------- ");
        logger.info("Program Start");
        Application.launch(GUI.class, args);
    }

    /**
     * Decompresses needed files
     */
    private static void setUp() {

        //Setting up config.txt file
        FileHandler.getFileFromConfigFolderOrDefault(FaF.CONFIG, FaF.CONFIG_DEFAULT);

        //Setting up BaseColorIDs.txt file
        FileHandler.getFileReaderFromConfigFolderOrDefault(FaF.BASE_COLOR_ID, FaF.BASE_COLOR_ID_DEFAULT);

        //Setting up LICENSE.txt file
        FileHandler.getFileReaderFromConfigFolderOrDefault(FaF.LICENSE, FaF.LICENSE);

        //Setting up README file
        FileHandler.getFileReaderFromConfigFolderOrDefault(FaF.README, FaF.README);
    }
}

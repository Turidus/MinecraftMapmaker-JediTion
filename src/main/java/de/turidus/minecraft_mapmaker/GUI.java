package de.turidus.minecraft_mapmaker;

import de.turidus.minecraft_mapmaker.utils.FaF;
import de.turidus.minecraft_mapmaker.utils.FileHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * This is the main class of Minecraft Map Maker. It sets up the environment and starts the GUI
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
public class GUI extends Application {

    private static final Logger logger = LoggerFactory.getLogger(GUI.class.getSimpleName());

    public static void main(String[] args) {
        logger.info(" ---------------------------------------- ");
        logger.info(" ---------------------------------------- ");
        logger.info("Program Start");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            setUp();
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.error("Could not set up files.", e);
            return;
        }


        Parent root;
        try {
            root = loadFXML();
            if (root == null) throw new IOException("Could not find GUI.xml file.");
        }
        catch (IOException e) {
            logger.error("Could not set GUI.", e);
            return;
        }
        primaryStage.setTitle("Minecraft Map Maker");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(FaF.ICON))));
        primaryStage.setScene(new Scene(root,1400,800));
        primaryStage.show();

    }

    private static Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource(FaF.GUI_FXML));
        return fxmlLoader.load();
    }

    /**
     * Decompresses needed files
     * @throws IOException If files can not be found or files can not be written to disc
     */
    private void setUp() throws IOException {

        //Setting up config.txt file
        FileHandler.getFileFromConfigFolderOrDefault(FaF.CONFIG,FaF.CONFIG_DEFAULT);

        //Setting up BaseColorIDs.txt file
        FileHandler.getFileReaderFromConfigFolderOrDefault(FaF.BASE_COLOR_ID, FaF.BASE_COLOR_ID_DEFAULT);

        //Setting up LICENSE.txt file
        FileHandler.getFileReaderFromConfigFolderOrDefault(FaF.LICENSE, FaF.LICENSE);

        //Setting up README file
        FileHandler.getFileReaderFromConfigFolderOrDefault(FaF.README, FaF.README);
    }
}

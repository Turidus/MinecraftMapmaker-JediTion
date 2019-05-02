package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            setUp();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not set up");
        }


        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
            if (root == null) throw new IOException();
        }
        catch (IOException e) {
            System.out.println("GUI.fxml File was not found!");
            return;
        }
        primaryStage.setTitle("Minecraft Map Maker");
        primaryStage.setScene(new Scene(root,1000,800));
        primaryStage.show();

    }


    /**
     * Decompresses needed files
     * @throws IOException If files can not be found or files can not be written to disc
     */
    private void setUp() throws IOException {

        //Setting up config.txt file
        File fileConfig = new File("resources/config.txt");
        if(!fileConfig.exists()){
            fileConfig.getParentFile().mkdir();
            fileConfig.createNewFile();
            try(InputStream inputStream = getClass().getResourceAsStream("/config")){

                try(FileOutputStream outputStream = new FileOutputStream(fileConfig)) {
                    int read;
                    byte[] bytes = new byte[1024];
                    while ((read = inputStream.read(bytes)) != -1){
                        outputStream.write(bytes,0,read);
                    }
                }
            }
        }

        //Setting up BaseColorIDs.txt file
        File fileBaseColorID = new File("resources/BaseColorIDs.txt");
        if(!fileBaseColorID.exists()){
            fileBaseColorID.getParentFile().mkdir();
            fileBaseColorID.createNewFile();
            try(InputStream inputStream = getClass().getResourceAsStream("/BaseColorIDs")){

                try(FileOutputStream outputStream = new FileOutputStream(fileBaseColorID)) {

                    int read;
                    byte[] bytes = new byte[1024];
                    while ((read = inputStream.read(bytes)) != -1){
                        outputStream.write(bytes,0,read);
                    }
                }
            }
        }

        //Setting up LICENSE.txt file
        File fileLicense = new File("resources/LICENSE.txt");
        if(!fileLicense.exists()){
            fileLicense.getParentFile().mkdir();
            fileLicense.createNewFile();
            try(InputStream inputStream = getClass().getResourceAsStream("/LICENSE")){

                try(FileOutputStream outputStream = new FileOutputStream(fileLicense)) {

                    int read;
                    byte[] bytes = new byte[1024];
                    while ((read = inputStream.read(bytes)) != -1){
                        outputStream.write(bytes,0,read);
                    }
                }
            }
        }

        //Setting up README file
        File fileReadme = new File("resources/README.md");
        if(!fileReadme.exists()){
            fileReadme.getParentFile().mkdir();
            fileReadme.createNewFile();
            try(InputStream inputStream = getClass().getResourceAsStream("/README.md")){

                try(FileOutputStream outputStream = new FileOutputStream(fileReadme)) {

                    int read;
                    byte[] bytes = new byte[1024];
                    while ((read = inputStream.read(bytes)) != -1){
                        outputStream.write(bytes,0,read);
                    }
                }
            }
        }

        //Setting up READMEde file
        File fileReadmeDE = new File("resources/READMEde.md");
        if(!fileReadmeDE.exists()){
            fileReadmeDE.getParentFile().mkdir();
            fileReadmeDE.createNewFile();
            try(InputStream inputStream = getClass().getResourceAsStream("/READMEde.md")){

                try(FileOutputStream outputStream = new FileOutputStream(fileReadmeDE)) {

                    int read;
                    byte[] bytes = new byte[1024];
                    while ((read = inputStream.read(bytes)) != -1){
                        outputStream.write(bytes,0,read);
                    }
                }
            }
        }

        //Setting up selectedBlocks file
        File fileUsedBlocks = new File("resources/selectedBlocks");
        if(!fileUsedBlocks.exists()){
            fileUsedBlocks.getParentFile().mkdir();
            fileUsedBlocks.createNewFile();
            try(InputStream inputStream = getClass().getResourceAsStream("/selectedBlocks")){

                try(FileOutputStream outputStream = new FileOutputStream(fileUsedBlocks)) {

                    int read;
                    byte[] bytes = new byte[1024];
                    while ((read = inputStream.read(bytes)) != -1){
                        outputStream.write(bytes,0,read);
                    }
                }
            }
        }
    }
}

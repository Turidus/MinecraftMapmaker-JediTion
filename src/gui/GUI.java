package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

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
        }
        primaryStage.setTitle("Minecraft Map Maker");
        primaryStage.setScene(new Scene(root,1000,800));
        primaryStage.show();

    }

    private void setUp() throws IOException {

        //Setting up config file
        File fileConfig = new File("resources/config");
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

        //Setting up BaseColorIDs file
        File fileBaseColorID = new File("resources/BaseColorIDs");
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

        //Setting up LICENSE file
        File fileLicense = new File("resources/LICENSE");
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

        //Setting up READEMEde file
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
    }
}

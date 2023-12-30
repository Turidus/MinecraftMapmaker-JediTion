package de.turidus.minecraft_mapmaker;

import de.turidus.minecraft_mapmaker.events.*;
import de.turidus.minecraft_mapmaker.utils.FaF;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import de.turidus.minecraft_mapmaker.logic.ColorIDMap;
import de.turidus.minecraft_mapmaker.utils.ConfigStore;
import de.turidus.minecraft_mapmaker.logic.MapIDEntry;
import de.turidus.minecraft_mapmaker.logic.MapmakerCore;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

/**
 * This class controls the GUI
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
public class GUIController {



    /*
    Logic Fields
     */

    private ConfigStore configStore;

    private TreeMap<Integer, List<MapIDEntry>> baseColorIDs;



    /*
    FX Fields
     */

    @FXML
    private TextField Tname;

    @FXML
    private TextField Tpath;

    @FXML
    private TextField TpathSave;

    @FXML
    private TextField TminY;

    @FXML
    private TextField TmaxY;

    @FXML
    private TextField TmaxX;

    @FXML
    private TextField TmaxZ;

    @FXML
    private TextField TmaxS;

    @FXML
    private TextArea Tstatus;

    @FXML
    private ChoiceBox<String> DBthreeD;

    @FXML
    private ChoiceBox<String> CIE;

    @FXML
    private ChoiceBox<String> mcDataVersion;

    @FXML
    private ChoiceBox<String> DBSchematic;

    @FXML
    private CheckBox CBpicture;

    @FXML
    private CheckBox CBposition;

    @FXML
    private CheckBox CBamount;

    @FXML
    private CheckBox CBschematic;

    @FXML
    private Button BchoosePath;

    @FXML
    private Button BchoosePathSave;

    @FXML
    private Button BcolorBlocks;

    @FXML
    private Button Bgo;

    @FXML
    private Button Bexit;



    /**
     * Public no args controller
     */
    public GUIController(){}

    @FXML
    private void initialize(){
        EventBus.getDefault().register(this);
        try {
            configStore = ConfigStore.getInstance();
        } catch (FileNotFoundException e) {
            EventBus.getDefault().post(new MessageEvent(new File("t").getAbsolutePath()));
            EventBus.getDefault().post(new CriticalExceptionEvent(e.getMessage(),e));
            return;
        } catch (ClassNotFoundException e) {
            EventBus.getDefault().post(new CriticalExceptionEvent(e.getMessage(),e));
        }

        try {
            baseColorIDs = ColorIDMap.getBaseColorIDMap();
        } catch (FileNotFoundException e) {
            EventBus.getDefault().post(new CriticalExceptionEvent("BaseColorIDs.txt File was not found!",e));
            return;
        }

        setUIFromConfigStore();
    }

    /*
    Actions
     */

    @FXML
    private void exit(){
        System.exit(0);
    }

    @FXML
    private void choosePath(){
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(Tpath.getScene().getWindow());
        if (file != null){
            Tpath.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void choosePathSave(){
        DirectoryChooser fc = new DirectoryChooser();
        File file = fc.showDialog(TpathSave.getScene().getWindow());
        if (file != null){
            TpathSave.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void getDefaultConfig(){
        try {
            configStore.loadDefault();
            setUIFromConfigStore();

        } catch (FileNotFoundException e) {
            EventBus.getDefault().post(new NonCriticalExceptionEvent("config-default file could not be found",e));
        } catch (ClassNotFoundException e) {
            EventBus.getDefault().post(new CriticalExceptionEvent(e.getMessage(),e));
        }
    }

    @FXML
    private void saveConfig(){
        updateConfigStore();
        try {
            configStore.saveCurrent();
        } catch (FileNotFoundException e) {
            EventBus.getDefault().post(new NonCriticalExceptionEvent("config.txt file could not be saved",e));
        } catch (IllegalAccessException e) {
            EventBus.getDefault().post(new NonCriticalExceptionEvent("Turidus did something wrong: IllegalAccessException in ConfigStore",e));
        }
    }

    @FXML
    private void colorBlockWindow(){

        String selectedVersion = mcDataVersion.getSelectionModel().getSelectedItem();
        for(ConfigStore.McVersion mcv : ConfigStore.mcVersionList){
            if(selectedVersion.equals(mcv.version())) {
                configStore.mcDataVersion = mcv.dataVersion();
                break;
            }
        }

        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource(FaF.COLOR_BLOCK_FXML));
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new CriticalExceptionEvent("ColorBlock.fxml was not found!",e));
            return;
        }

        Stage stage = new Stage();
        stage.setTitle("Colors and Blocks");
        stage.setScene(new Scene(root,1400,800));
        stage.show();
    }

    @FXML
    private void aboutWindow(){
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource(FaF.ABOUT_FXML));
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new CriticalExceptionEvent("About.fxml was not found!",e));
            return;
        }

        Stage stage = new Stage();
        stage.setTitle("About");
        stage.setScene(new Scene(root,600,400));
        stage.show();
    }

    @FXML
    private void runProgram(){
        try {
            updateConfigStore();
        }
        catch (IllegalArgumentException e){
            EventBus.getDefault().post(new NonCriticalExceptionEvent(e.getMessage(),e));
            return;
        }

        if (Objects.equals(configStore.pathToImage, "") || configStore.pathToImage == null){
            EventBus.getDefault().post(new NonCriticalExceptionEvent("Image file was not set",new IllegalArgumentException()));
            return;
        }
        if (Objects.equals(configStore.name, "") || configStore.name == null) {
            File file = new File(configStore.pathToImage);
            String name = file.getName();
            if (name.contains(".")) configStore.name = name.split("\\.")[0];
            else configStore.name = name;
        } else {
            configStore.name = configStore.name.replaceAll("[^a-zA-Z0-9_\\-]", "");
        }
        configStore.pathToSave += "/" + configStore.name + "/";
        EventBus.getDefault().post(new StartEvent());
        Thread thread = new Thread(new MapmakerCore());
        thread.start();
    }

    /*
    EventBus Handling
     */

    @Subscribe
    public void criticalExceptionHandling(CriticalExceptionEvent cee){
        BchoosePath.setDisable(true);
        Bgo.setDisable(true);
        BcolorBlocks.setDisable(true);
        BchoosePathSave.setDisable(true);
        Bexit.setDisable(false);
        Tstatus.setText(Tstatus.getText() + "\nCritical Error: " + cee.msg + "\n");
    }

    @Subscribe
    public void nonCriticalExceptionHandling(NonCriticalExceptionEvent ncee){
        EventBus.getDefault().post(new DoneEvent());
        String status = Tstatus.getText() + "\nError: ";
        Tstatus.setText(status + ncee.msg);
    }

    @Subscribe
    public void messageHandling(MessageEvent me){
        String status = Tstatus.getText() + "\n";
        Tstatus.setText(status +"Message: " + me.msg);
    }

    @Subscribe
    public void startHandling(StartEvent se){
        Bgo.setDisable(true);
        Bexit.setDisable(true);
    }

    @Subscribe
    public void doneHandling(DoneEvent de){
        Bgo.setDisable(false);
        Bexit.setDisable(false);
    }

    /*
    Helper
     */
    private void setUIFromConfigStore(){

        setBlocksToUseFromBlacklist();

        /*
        Setting config values into GUI
        */
        //Text fields

        if(configStore.pathToImage != null){
            Tpath.setText(new File(configStore.pathToImage).getAbsolutePath());
        }
        Tooltip TpathTP = new Tooltip("Picture that will be processed");
        Tooltip.install(Tpath, TpathTP);

        File file = new File(configStore.pathToSave);
        TpathSave.setText(file.getAbsolutePath());
        Tooltip TpathSaveTP = new Tooltip("Folder which will contain the results");
        Tooltip.install(Tpath, TpathSaveTP);

        if(configStore.name != null){
            Tname.setText(configStore.name);
        }
        Tooltip TnameTP = new Tooltip("Optional name of the project");
        Tooltip.install(Tname, TnameTP);

        TminY.setText(String.valueOf(configStore.minY));
        Tooltip TminYTP = new Tooltip("Minimal world height you will place the structure on. Default: 4");
        Tooltip.install(TminY, TminYTP);

        TmaxY.setText(String.valueOf(configStore.maxY));
        Tooltip TmaxYTP = new Tooltip("Maximal world height you want to place blocks. The greater" +
                " the difference between maxY and min Y becomes," +
                " the lower is the chance of pixel errors and colors mismatches in the finale picture. Default: 250");
        Tooltip.install(TmaxY, TmaxYTP);

        TmaxX.setText(String.valueOf(configStore.maxX));
        Tooltip TmaxXTP = new Tooltip("This determines the width of the final structure. " +
                "This can be used to resize the input picture to a desired size. 0 will use the original size. Default: 0");
        Tooltip.install(TmaxX, TmaxXTP);

        TmaxZ.setText(String.valueOf(configStore.maxZ));
        Tooltip TmaxZTP = new Tooltip("This determines the length of the final structure. " +
                "This can be used to resize the input picture to a desired size. 0 will use the original size. Default: 0");
        Tooltip.install(TmaxZ, TmaxZTP);

        TmaxS.setText(String.valueOf(configStore.maxS));
        Tooltip TmaxSTP = new Tooltip("Sets the size of the final square schematic. If smaller then the" +
                " size of the structure, multiple schematics will be created. This massively improves performance while importing in minecraft." +
                " Default: 129");
        Tooltip.install(TmaxS, TmaxSTP);

        //Checkboxes
        CBpicture.setSelected(configStore.picture);
        Tooltip CBpictureTP = new Tooltip("If checked, an approximated preview will be generated and saved to the folder");
        Tooltip.install(CBpicture, CBpictureTP);

        CBamount.setSelected(configStore.amountFile);
        Tooltip CBamountTP = new Tooltip("If checked, a text file containing the amount of needed blocks per block type " +
                "will be generated and saved to the folder");
        Tooltip.install(CBamount, CBamountTP);

        CBposition.setSelected(configStore.positionFile);
        Tooltip CBpositionTP = new Tooltip("If checked, a text file containing the relative positions of all blocks " +
                "will be generated and saved to the folder");
        Tooltip.install(CBposition, CBpositionTP);

        CBschematic.setSelected(configStore.schematic);
        Tooltip CBschematicTP = new Tooltip("If checked, one or more Sponge Schematic (.schem) files will be generated");
        Tooltip.install(CBschematic, CBschematicTP);

        //Choiceboxes
        ObservableList<String> choices = FXCollections.observableArrayList("Flat","Staircase");
        DBthreeD.setItems(choices);
        if (configStore.threeD) DBthreeD.getSelectionModel().select(1);
        else DBthreeD.getSelectionModel().select(0);
        Tooltip DBthreeDTP  = new Tooltip("Selecting flat will lead to a flat structure." +
                " Staircase will lead to a 3D structure with every map color having two additional shades.");
        Tooltip.install(DBthreeD, DBthreeDTP);

        ObservableList<String> cieChoices = FXCollections.observableArrayList("CIE deltaE2000","Euclidean");
        CIE.setItems(cieChoices);
        if (!configStore.cie) CIE.getSelectionModel().select(1);
        else CIE.getSelectionModel().select(0);
        Tooltip CIETP  = new Tooltip("Allows to choose a color matching algorithm. CIE tries to match human" +
                " perception and should lead to better results.");
        Tooltip.install(CIE, CIETP);

        ObservableList<String> mcVersionChoices = FXCollections.observableArrayList();
        String toSelect = null;
        for (ConfigStore.McVersion mcv : ConfigStore.mcVersionList){
            mcVersionChoices.add(mcv.version());
            if(configStore.mcDataVersion == mcv.dataVersion()) toSelect = mcv.version();
        }
        mcDataVersion.setItems(mcVersionChoices);
        if(toSelect != null) mcDataVersion.getSelectionModel().select(toSelect);
        Tooltip McDataVersionTP  = new Tooltip("Choose the minecraft version you are playing." +
                " For Version 1.11 and 1.12, use MinecraftMapMaker v2.x.x");
        Tooltip.install(mcDataVersion, McDataVersionTP);

        ObservableList<String> schemChoices = FXCollections.observableArrayList("Sponge","Litematic");
        DBSchematic.setItems(schemChoices);
        if (configStore.spongeSchematic) DBSchematic.getSelectionModel().select(0);
        else DBSchematic.getSelectionModel().select(1);
        Tooltip DBSchmaticDTP  = new Tooltip("Schematic format." +
                " Sponge is compatible with Worldedit, Litematic with Litematica.");
        Tooltip.install(DBSchematic, DBSchmaticDTP);
    }

    private void setBlocksToUseFromBlacklist(){
        ArrayList<MapIDEntry> blocksToUse = new ArrayList<>();

        for (Integer key : baseColorIDs.keySet()){
            if(configStore.blacklist.contains(String.valueOf(key))) continue;
            blocksToUse.add(baseColorIDs.get(key).get(0));
        }

        configStore.blocksToUse = blocksToUse;
    }

    private void updateConfigStore() throws IllegalArgumentException {

        configStore.pathToImage = Tpath.getText();
        configStore.name = Tname.getText();
        configStore.pathToSave = TpathSave.getText();

        int minY;
        int maxY;
        int maxX;
        int maxZ;
        int maxS;

        try{
            minY = Integer.parseInt(TminY.getText());
            if(minY > 316 || minY < 0){
                throw new IllegalArgumentException("The value for minY is only valid between 0 and 316 inclusive.");
            }
        }
        catch (NumberFormatException e){
            throw new NumberFormatException("The value for minY was not a Number");
        }

        try{
            maxY = Integer.parseInt(TmaxY.getText());
            if(maxY > 320 || maxY < 4){
                throw new IllegalArgumentException("The value for maxY is only valid between 4 and 320 inclusive.");
            }
        }
        catch (NumberFormatException e){
            throw new NumberFormatException("The value for maxY was not a Number.");
        }

        if(minY > maxY || (maxY - minY) < 4){
            throw new IllegalArgumentException("The value for minY has to be at least 4 smaller than maxY.");

        }

        try{
            maxX = Integer.parseInt(TmaxX.getText());
            maxX = Math.max(maxX, 0);
        } catch (NumberFormatException e){
            throw new NumberFormatException("Finale size in X was not a Number.");
        }

        try{
            maxZ = Integer.parseInt(TmaxZ.getText());
            maxZ = Math.max(maxZ, 0);
        } catch (NumberFormatException e){
            throw new NumberFormatException("Finale size in Z was not a Number.");
        }

        try{
            maxS = Integer.parseInt(TmaxS.getText());
            if(maxS <= 0){
                throw new IllegalArgumentException("The value maxS for is only valid when greater then 0.");
            }
        }
        catch (NumberFormatException e){
            throw new NumberFormatException("The value for maxS was not a Number.");
        }

        configStore.minY = minY;
        configStore.maxY = maxY;
        configStore.maxX = maxX;
        configStore.maxZ = maxZ;
        configStore.maxS = maxS;

        configStore.threeD = DBthreeD.getSelectionModel().getSelectedIndex() == 1;
        configStore.cie = CIE.getSelectionModel().getSelectedIndex() == 0;
        configStore.spongeSchematic = DBSchematic.getSelectionModel().getSelectedIndex() == 0;

        String selectedVersion = mcDataVersion.getSelectionModel().getSelectedItem();
        for(ConfigStore.McVersion mcv : ConfigStore.mcVersionList){
            if(selectedVersion.equals(mcv.version())) {
                configStore.mcDataVersion = mcv.dataVersion();
                break;
            }
        }

        configStore.picture = CBpicture.isSelected();
        configStore.amountFile = CBamount.isSelected();
        configStore.positionFile = CBposition.isSelected();
        configStore.schematic = CBschematic.isSelected();

        if (configStore.blocksToUse == null) throw new IllegalArgumentException("blocksToUse was null");
        List<Integer> tempList = new ArrayList<>(baseColorIDs.keySet());
        List<MapIDEntry> tempEntries = new ArrayList<>();
        for(MapIDEntry entry : configStore.blocksToUse){
            //Only remove blocks that are used by the current selected MC Version, else remove that block from blocksToUse
            if(entry.colorID() <= ConfigStore.maxColorIDUsedByVersion.get(configStore.mcDataVersion)){
                tempList.remove((Integer) entry.colorID());
            }
            else {
                    tempEntries.add(entry);
            }
        }
        for(MapIDEntry entry : tempEntries){
            configStore.blocksToUse.remove(entry);
        }
        List<String> newBlacklist = new ArrayList<>();
        for (Integer i : tempList){
            newBlacklist.add(String.valueOf(i));
        }

        configStore.blacklist = newBlacklist;
    }
}

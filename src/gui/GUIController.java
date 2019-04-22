package gui;

import events.CriticalExceptionEvent;
import events.MessageEvent;
import events.NonCriticalExceptionEvent;
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
import logic.ColorIDMap;
import logic.ConfigStore;
import logic.MapIDEntry;
import logic.MapmakerCore;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class GUIController {

    /*
    Logic Fields
     */

    ConfigStore configStore;

    TreeMap<Integer, List<MapIDEntry>> baseColorIDs;



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
    private TextField TmaxS;

    @FXML
    private TextArea Tstatus;

    @FXML
    private ChoiceBox<String> DBthreeD;

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

    /*
    Initialisation
     */

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
            EventBus.getDefault().post(new CriticalExceptionEvent("Configuration File was not found!",e));
            return;
        }

        try {
            baseColorIDs = ColorIDMap.getBaseColorIDMap();
        } catch (FileNotFoundException e) {
            EventBus.getDefault().post(new CriticalExceptionEvent("BaseColorIDs File was not found!",e));
            return;
        }

        setBlocksToUseFromBlacklist();

        /*
        Setting config values into GUI
        */
        //Text fields

        File file = new File(configStore.pathToSave);
        TpathSave.setText(file.getAbsolutePath());

        if(configStore.pathToImage != null){
            Tpath.setText(new File(configStore.pathToImage).getAbsolutePath());
        }

        if(configStore.name != null){
            Tname.setText(configStore.name);
        }

        TminY.setText(String.valueOf(configStore.minY));

        TmaxY.setText(String.valueOf(configStore.maxY));

        TmaxS.setText(String.valueOf(configStore.maxS));

        //Checkboxes
        CBpicture.setSelected(configStore.picture);

        CBamount.setSelected(configStore.amountFile);

        CBposition.setSelected(configStore.positionFile);

        CBschematic.setSelected(configStore.schematic);

        //Choicebox
        ObservableList<String> choices = FXCollections.observableArrayList("2D Construct - 51 Colors","3D Construct - 153 Colors");
        DBthreeD.setItems((ObservableList<String>) choices);
        if (configStore.threeD) DBthreeD.getSelectionModel().select(1);
        else DBthreeD.getSelectionModel().select(0);


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
        if (file == null){
            Tpath.setText("No file found");
        }
        else {
            Tpath.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void choosePathSave(){
        DirectoryChooser fc = new DirectoryChooser();
        File file = fc.showDialog(TpathSave.getScene().getWindow());
        if (file == null){
            TpathSave.setText("No directory found");
        }
        else {
            TpathSave.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void getDefaultConfig(){
        try {
            configStore.loadDefault();
        } catch (FileNotFoundException e) {
            EventBus.getDefault().post(new NonCriticalExceptionEvent("config-default file could not be found",e));
        }
    }

    @FXML
    private void saveConfig(){
        updateConfigStore();
        try {
            configStore.setCurrentAsDefault();
        } catch (FileNotFoundException e) {
            EventBus.getDefault().post(new NonCriticalExceptionEvent("config file could not be saved",e));
        } catch (IllegalAccessException e) {
            EventBus.getDefault().post(new NonCriticalExceptionEvent("Turidus did something wrong: IllegalAccessException in ConfigStore",e));
        }
    }

    @FXML
    private void colorBlockWindow(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("ColorBlock.fxml"));

        } catch (IOException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new CriticalExceptionEvent("ColorBlock.fxml was not found!",e));
            return;
        }

        Stage stage = new Stage();
        stage.setTitle("Colors and Blocks");
        stage.setScene(new Scene(root,1200,800));
        stage.show();
    }

    @FXML
    private void runProgram(){
        updateConfigStore();

        if (configStore.pathToImage.equals("")){
            EventBus.getDefault().post(new NonCriticalExceptionEvent("Image file was not set",new IllegalArgumentException()));
            return;
        }
        if (configStore.name.equals("")) {
            File file = new File(configStore.pathToImage);
            String name = file.getName();
            if (name.contains(".")) configStore.name = name.split("\\.")[0];
            else configStore.name = name;
            configStore.pathToSave += "/" + configStore.name + "/";
        } else {
            configStore.name = configStore.name.replaceAll("[^a-zA-Z0-9_\\-]", "");
            configStore.pathToSave += "/" + configStore.name + "/";
        }

        Thread thread = new Thread(new MapmakerCore());
        thread.start();
    }

    /*
    EventBus Handling
     */

    @Subscribe
    public void criticalExceptionHandeling(CriticalExceptionEvent cee){
        BchoosePath.setDisable(true);
        Bgo.setDisable(true);
        BcolorBlocks.setDisable(true);
        BchoosePathSave.setDisable(true);
        Bexit.setDisable(false);
        Tstatus.setText(Tstatus.getText() + "\n" + cee.msg + "\n");
    }

    @Subscribe
    public void nonCriticalExceptionHandling(NonCriticalExceptionEvent ncee){
        String status = Tstatus.getText() + "\n";
        Tstatus.setText(status + ncee.msg);
    }

    @Subscribe
    public void messageHandling(MessageEvent me){
        String status = Tstatus.getText() + "\n";
        Tstatus.setText(status +"Message: " + me.msg);
    }

    /*
    Helper
     */

    private void setBlocksToUseFromBlacklist(){
        List<MapIDEntry> blocksToUse = new ArrayList<>();

        for (Integer key : baseColorIDs.keySet()){
            if(configStore.blacklist.contains(String.valueOf(key))) continue;
            blocksToUse.add(baseColorIDs.get(key).get(0));
        }

        configStore.blocksToUse = blocksToUse;
    }

    private void updateConfigStore() throws IllegalArgumentException,NumberFormatException{

        configStore.pathToImage = Tpath.getText();
        configStore.name = Tname.getText();
        configStore.pathToSave = TpathSave.getText();

        int minY;
        int maxY;
        int maxS;

        try{
            minY = Integer.parseInt(TminY.getText());
            if(minY > 251 || minY < 0){
                throw new IllegalArgumentException("minY is only vaild when 0 <= minY <= 251");
            }
        }
        catch (NumberFormatException e){
            throw new NumberFormatException("minY was not a Number");
        }

        try{
            maxY = Integer.parseInt(TmaxY.getText());
            if(maxY > 255 || maxY < 4){
                throw new IllegalArgumentException("maxY is only vaild when 4 <= maxY <= 255");
            }
        }
        catch (NumberFormatException e){
            throw new NumberFormatException("maxY was not a Number");
        }

        if(minY > maxY || (maxY - minY) < 4){
            throw new IllegalArgumentException("minY has to be at least 4 smaller than maxY");

        }

        try{
            maxS = Integer.parseInt(TmaxS.getText());
            if(maxS <= 0){
                throw new IllegalArgumentException("maxS is only vaild when maxS > 0");
            }
        }
        catch (NumberFormatException e){
            throw new NumberFormatException("maxS was not a Number");
        }

        configStore.minY = minY;
        configStore.maxY = maxY;
        configStore.maxS = maxS;

        if (DBthreeD.getSelectionModel().getSelectedIndex() == 1) configStore.threeD = true;
        else configStore.threeD = false;

        configStore.picture = CBpicture.isSelected();
        configStore.amountFile = CBamount.isSelected();
        configStore.positionFile = CBposition.isSelected();
        configStore.schematic = CBschematic.isSelected();

        List<Integer> tempList = new ArrayList<>(baseColorIDs.keySet());
        for(MapIDEntry entry : configStore.blocksToUse){
            tempList.remove(tempList.indexOf(entry.colorID));
        }
        List<String> newBlacklist = new ArrayList<>();
        for (Integer i : tempList){
            newBlacklist.add(String.valueOf(i));
        }

        configStore.blacklist = newBlacklist;
    }
}

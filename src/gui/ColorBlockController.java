package gui;

import events.CriticalExceptionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import logic.ColorIDMap;
import logic.ConfigStore;
import logic.MapIDEntry;
import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ColorBlockController {

    TreeMap<Integer, List<MapIDEntry>> baseColorIDs;
    private ConfigStore configStore;

    /*
    FX Fields
     */
    @FXML
    private GridPane GPpane;

    @FXML
    private Button Bcancel;

    @FXML
    private Button Bdone;

    /*
    Initialisation
     */

    /**
     * Public no args controller
     */
    public ColorBlockController(){}

    @FXML
    private void initialize() {
        try {
            configStore = ConfigStore.getInstance();
        } catch (FileNotFoundException e) {
            EventBus.getDefault().post(new CriticalExceptionEvent("Configuration File was not found!",e));
            return;
        }
        {
            try {
                baseColorIDs = ColorIDMap.getBaseColorIDMap();
            } catch (FileNotFoundException e) {
                EventBus.getDefault().post(new CriticalExceptionEvent("BaseColorIDs File was not found!",e));
                return;
            }
        }

        List<Integer> keyList = new ArrayList<>(baseColorIDs.keySet());

        int numberOfColumns = (keyList.size() / 10 + 1);
        GPpane.setMinWidth(1200);
        double pWidth = 1200d/ numberOfColumns;

        if(keyList.size() > 20) {
            ObservableList<ColumnConstraints> constraints = GPpane.getColumnConstraints();
            for (int i = 2; i < numberOfColumns; i++) {
                GPpane.addColumn(i);
                ColumnConstraints cc = new ColumnConstraints();
                cc.setMinWidth(pWidth);
                constraints.add(i,cc);
            }
        }

        for (int i = 0; i < keyList.size(); i++ ){
            MapIDEntry entry = baseColorIDs.get(keyList.get(i)).get(0);

            GridPane childGP = new GridPane();
            ObservableList<ColumnConstraints> constraintsChildGP = childGP.getColumnConstraints();

            //Label
            Label label = new Label();
            label.setText(String.valueOf(keyList.get(i)) + ": ");
            label.setPadding(new Insets(0d,2d,0d,5d));
            label.setAlignment(Pos.CENTER);

            childGP.addColumn(0,label);
            ColumnConstraints cc = new ColumnConstraints();
            cc.setMinWidth(10);
            constraintsChildGP.add(0,cc);

            //Rectangle
            int red = entry.getRed();
            int green = entry.getGreen();
            int blue = entry.getBlue();
            Rectangle rectangle = new Rectangle();
            rectangle.setFill(Color.rgb(red,green,blue));
            rectangle.setHeight(20);
            rectangle.setWidth(20);
            rectangle.setStrokeType(StrokeType.INSIDE);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(1);

            childGP.addColumn(1,rectangle);
            cc = new ColumnConstraints();
            cc.setMinWidth(11);
            constraintsChildGP.add(1,cc);

            //CheckBox
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(!configStore.blacklist.contains(String.valueOf(keyList.get(i))));
            checkBox.setPadding(new Insets(0d,2d,4d,2d));

            childGP.addColumn(2,checkBox);
            cc = new ColumnConstraints();
            cc.setMinWidth(11);
            constraintsChildGP.add(2,cc);

            //ChoiceBox
            ChoiceBox<String> choiceBox = new ChoiceBox<>();
            ObservableList<String> choices = FXCollections.observableArrayList();
            for (MapIDEntry blockEntry : baseColorIDs.get(keyList.get(i))){
                choices.add(blockEntry.blockName);
            }
            choiceBox.setItems(choices);
            choiceBox.getSelectionModel().selectFirst();

            childGP.addColumn(3,choiceBox);
            cc = new ColumnConstraints();
            cc.setMinWidth(110);
            cc.setMaxWidth(110);
            constraintsChildGP.add(3,cc);


            //Add childGP
            childGP.setAlignment(Pos.CENTER);
            GPpane.add(childGP,i / 10,i % 10 );
            GPpane.setGridLinesVisible(true);
        }


    }

    /*
    Actions
     */
    @FXML
    private void cancel(){
        Bcancel.getScene().getWindow().hide();
    }

    @FXML
    private void done(){
        List<MapIDEntry> blocksToUse = new ArrayList<>();
        for (Node node : GPpane.getChildren()){
            if (!node.getTypeSelector().equals("GridPane")) continue;
            GridPane childPane = (GridPane) node;
            ObservableList<Node> children = childPane.getChildren();

            CheckBox checkBox = (CheckBox)children.get(2);
            if (!checkBox.isSelected()) continue;

            Label idLabel = (Label)children.get(0);
            Integer id = Integer.valueOf(idLabel.getText().replace(": ",""));

            ChoiceBox<String> choiceBox = (ChoiceBox<String>)children.get(3);
            for (MapIDEntry entry : baseColorIDs.get(id)){
                if (entry.blockName.equals(choiceBox.getSelectionModel().getSelectedItem())){
                    blocksToUse.add(entry);
                    break;
                }
            }
        }

        configStore.blocksToUse = blocksToUse;

        List<Integer> tempList = new ArrayList<>(baseColorIDs.keySet());
        for(MapIDEntry entry : configStore.blocksToUse){
            tempList.remove((Integer) entry.colorID);
        }
        List<String> newBlacklist = new ArrayList<>();
        for (Integer i : tempList){
            newBlacklist.add(String.valueOf(i));
        }

        configStore.blacklist = newBlacklist;

        Bdone.getScene().getWindow().hide();
    }


}

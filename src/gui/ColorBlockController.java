package gui;

import events.CriticalExceptionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
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

/**
 * This class controls the ColorBlock window
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
public class ColorBlockController {

    private TreeMap<Integer, List<MapIDEntry>> baseColorIDs;
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
        double percentage = 100d / numberOfColumns;

        if(keyList.size() > 20) {
            ObservableList<ColumnConstraints> constraints = GPpane.getColumnConstraints();
            for (int i = 2; i < numberOfColumns; i++) {
                GPpane.addColumn(i);
                ColumnConstraints cc = new ColumnConstraints();
                cc.setMinWidth(pWidth);
                cc.setPercentWidth(percentage);
                constraints.add(i,cc);
            }
        }

        for (int i = 0; i < keyList.size(); i++ ){
            MapIDEntry entry = baseColorIDs.get(keyList.get(i)).get(0);
            if (configStore.blocksToUse != null){
                System.out.println(entry.blockName);
                for (MapIDEntry usedEntry : configStore.blocksToUse){
                    if (usedEntry.colorID == entry.colorID){
                        entry = usedEntry;
                        System.out.println(entry.blockName);
                        break;
                    }
                }
            }
            System.out.println("---");

            GridPane childGP = new GridPane();
            ObservableList<ColumnConstraints> constraintsChildGP = childGP.getColumnConstraints();

            //Label
            Label label = new Label();
            label.setText(keyList.get(i) + ": ");
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
            choiceBox.getSelectionModel().select(entry.blockName);




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

            //noinspection unchecked If this fails, it should fail as hard as possible
            ChoiceBox<String> choiceBox = (ChoiceBox<String>)children.get(3);
            for (MapIDEntry entry : baseColorIDs.get(id)){
                if (entry.blockName.equals(choiceBox.getSelectionModel().getSelectedItem())){
                    System.out.println(entry.blockName);
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

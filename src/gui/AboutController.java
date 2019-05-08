package gui;

import events.MessageEvent;
import events.NonCriticalExceptionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;

/**
 * This class controls the about window
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
public class AboutController {

    @FXML
    private Label Lversion;

    @FXML
    private Button Bversion;

    public AboutController(){}

    @FXML
    private void initialize(){


        try(InputStream inputStream = getClass().getResourceAsStream("/version")){
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            Lversion.setText(reader.readLine());
            reader.close();
        }catch (IOException e){
            EventBus.getDefault().post(new NonCriticalExceptionEvent("Version file could not be found", e));
        }
    }

    //Action

    /**
     * Checks the version found in the version file vs the version found as tag of the latest release on github.
     */
    @FXML
    private void checkVersion() {
        if (!Lversion.getText().contains("Version")) {
            EventBus.getDefault().post(new NonCriticalExceptionEvent("Version file could not be found", new IOException()));
            return;
        }
        String curVersion = Lversion.getText().split(" ")[1].trim();

        String url = "https://api.github.com/repos/Turidus/MinecraftMapMaker-JediTion/releases/latest";
        InputStream response;

        try {
            response = new URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(response));
        } catch (IOException e) {
            EventBus.getDefault().post(new NonCriticalExceptionEvent("Could not get the version of the newest release",e));
            return;
        } catch (ParseException e) {
            EventBus.getDefault().post(new NonCriticalExceptionEvent("Could not find the version of the newest release",e));
            return;
        }
        System.out.println(jsonObject.get("tag_name"));
        if(jsonObject.get("tag_name").equals(curVersion)) EventBus.getDefault().post(new MessageEvent("This version is up to date"));
        else EventBus.getDefault().post(new MessageEvent("There is a new version, get it under About -> Latest Version"));
    }

}

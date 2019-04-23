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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class AboutController {

    @FXML
    private Label Lversion;

    @FXML
    private Button Bversion;

    public AboutController(){};

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

    @FXML
    private void checkVersion() {
        if (!Lversion.getText().contains("Version")) {
            EventBus.getDefault().post(new NonCriticalExceptionEvent("Version file could not be found", new IOException()));
            return;
        }
        String curVersion = Lversion.getText().split(" ")[1].trim();

        String url = "https://api.github.com/repos/Turidus/Minecraft-MapMaker/releases/latest";
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

        if(jsonObject.get("tag_name").equals(curVersion)) EventBus.getDefault().post(new MessageEvent("This version is up to date"));
        else EventBus.getDefault().post(new MessageEvent("There is a new version, get it under About -> Latest Version"));
    }

}

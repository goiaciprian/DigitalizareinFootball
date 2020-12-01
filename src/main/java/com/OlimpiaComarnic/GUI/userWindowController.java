package com.OlimpiaComarnic.GUI;

import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.GUI.Utils.SaveWindowPosition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Map;


public class userWindowController {

    private final SaveWindowPosition position = new SaveWindowPosition("userWindow");

    @FXML
    AnchorPane anchorPane;

    @FXML
    Button logOutButt;

    @FXML
    Label helloText;

    @FXML
    Label nume, nrTricou, goluri, paseDeGol, cartGalben, cartRosii;

    @FXML
    VBox aparitii;

    @FXML
    void initialize() {
        position.defaultSetting();
        initAparitii();
        initUI();
    }

    @FXML
    public void logOut() {
        try {
            LogInController.loggedIn = null;
            position.updatePref();
            anchorPane.getScene().setRoot(FXMLLoader.load(GUIRun.class.getResource("LogIn.fxml")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void initAparitii() {
        ObservableList<String> data = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> aparitie : LogInController.loggedIn.getAparitii().entrySet())
            data.add(aparitie.getKey() + " - " + aparitie.getValue());
        ListView<String> list = new ListView<>(data);
        list.setFocusTraversable(false);
        aparitii.getChildren().add(list);
    }

    private void initUI() {
        Player loggedIn = LogInController.loggedIn;
        helloText.setText(loggedIn.getNume());
        helloText.setMinWidth(Region.USE_PREF_SIZE);
        nume.setText(loggedIn.getNume());
        nume.setMinWidth(Region.USE_PREF_SIZE);
        nrTricou.setText(String.valueOf(loggedIn.getNumarTricou()));
        nrTricou.setMinWidth(Region.USE_PREF_SIZE);
        goluri.setText(String.valueOf(loggedIn.getGoluri()));
        goluri.setMinWidth(Region.USE_PREF_SIZE);
        paseDeGol.setText(String.valueOf(loggedIn.getPaseGol()));
        paseDeGol.setMinWidth(Region.USE_PREF_SIZE);
        cartGalben.setText(String.valueOf(loggedIn.getCartonaseGalbene()));
        cartGalben.setMinWidth(Region.USE_PREF_SIZE);
        cartRosii.setText(String.valueOf(loggedIn.getCartonaseRosii()));
        cartRosii.setMinWidth(Region.USE_PREF_SIZE);
    }

}

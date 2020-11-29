package com.OlimpiaComarnic.GUI;

import com.OlimpiaComarnic.GUI.Utils.SaveWindowPosition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;


public class userWindowController {

    private final SaveWindowPosition position = new SaveWindowPosition("userWindow");

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button logOutButt;

    @FXML
    void initialize() {
        position.defaultSetting();
        GUIRun.currStage.setOnCloseRequest(windowEvent -> position.updatePref());
    }

    @FXML
    public void logOut() {
        try {
            position.updatePref();
            anchorPane.getScene().setRoot(FXMLLoader.load(GUIRun.class.getResource("LogIn.fxml")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

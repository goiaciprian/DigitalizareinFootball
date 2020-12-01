package com.OlimpiaComarnic.GUI;

import com.OlimpiaComarnic.GUI.Utils.SaveWindowPosition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;


public class adminWindowController {

    private final SaveWindowPosition position = new SaveWindowPosition("adminWindow");

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button logOutButt;

    @FXML
    void initialize() {
        position.defaultSetting();
    }

    @FXML
    public void logOut() {
        try {
            position.updatePref();
            anchorPane.getScene().setRoot(FXMLLoader.load(GUIRun.class.getResource("LogIn.fxml")));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

}

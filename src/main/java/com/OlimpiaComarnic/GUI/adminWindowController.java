package com.OlimpiaComarnic.GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class adminWindowController {

    @FXML
    private GridPane gridParent;

    @FXML
    void initialize() {

        // TODO correct size and position
        GUIRun.currStage.setResizable(true);
        GUIRun.currStage.setWidth(900);
        GUIRun.currStage.setHeight(500);
    }

}

package com.OlimpiaComarnic.GUI;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

import java.util.prefs.Preferences;

public class adminWindowController {

    private static final Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

    private static final String WINDOW_POSITION_X = "Window_Position_X";
    private static final String WINDOW_POSITION_Y = "Window_Position_Y";
    private static final String WINDOW_WIDTH = "Window_Width";
    private static final String WINDOW_HEIGHT = "Window_Height";
    private static final String IS_MAXIMIZED = "Is_Maximized";
    private static final double DEFAULT_X = (primScreenBounds.getWidth() - GUIRun.currStage.getWidth()) / 2;
    private static final double DEFAULT_Y = (primScreenBounds.getHeight() - GUIRun.currStage.getHeight()) / 2;
    private static final double DEFAULT_WIDTH = 900;
    private static final double DEFAULT_HEIGHT = 500;
    private static final boolean DEFAULT_MAXIMIZED = false;
    private static final String NODE_NAME = "adminWindow";

    @FXML
    private AnchorPane anchorPane;

    @FXML
    void initialize() {
        GUIRun.currStage.setResizable(true);

        Preferences pref = Preferences.userRoot().node(NODE_NAME);
        double x = pref.getDouble(WINDOW_POSITION_X, DEFAULT_X);
        double y = pref.getDouble(WINDOW_POSITION_Y, DEFAULT_Y);
        double width = pref.getDouble(WINDOW_WIDTH, DEFAULT_WIDTH);
        double height = pref.getDouble(WINDOW_HEIGHT, DEFAULT_HEIGHT);
        boolean fullscreen = pref.getBoolean(IS_MAXIMIZED, DEFAULT_MAXIMIZED);

        if (fullscreen) {
            GUIRun.currStage.setMaximized(true);
        }
        else {
            // resize the window to saved prefs
            GUIRun.currStage.setWidth(width);
            GUIRun.currStage.setHeight(height);

            // positions window to saved prefs
            GUIRun.currStage.setX(x);
            GUIRun.currStage.setX(y);
        }

        // prevent from resizing
        GUIRun.currStage.setMinWidth(900);
        GUIRun.currStage.setMinHeight(500);

        savePrefs();
    }

    public static void savePrefs() {
        GUIRun.currStage.setOnCloseRequest(windowEvent -> {
            Preferences preferences = Preferences.userRoot().node(NODE_NAME);
            preferences.putDouble(WINDOW_POSITION_X, GUIRun.currStage.getX());
            preferences.putDouble(WINDOW_POSITION_Y, GUIRun.currStage.getY());
            preferences.putDouble(WINDOW_WIDTH, GUIRun.currStage.getWidth());
            preferences.putDouble(WINDOW_HEIGHT, GUIRun.currStage.getHeight());
            preferences.putBoolean(IS_MAXIMIZED, GUIRun.currStage.isMaximized());

        });
    }

}

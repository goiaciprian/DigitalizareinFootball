package com.OlimpiaComarnic.GUI.Utils;

import com.OlimpiaComarnic.GUI.GUIRun;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.util.prefs.Preferences;

public class SaveWindowPosition {
    private static final Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

    public final String WINDOW_POSITION_X = "Window_Position_X";
    public final String WINDOW_POSITION_Y = "Window_Position_Y";
    public final String WINDOW_WIDTH = "Window_Width";
    public final String WINDOW_HEIGHT = "Window_Height";
    public final String IS_MAXIMIZED = "Is_Maximized";
    public final double DEFAULT_X = (primScreenBounds.getWidth() - GUIRun.currStage.getWidth()) / 2;
    public final double DEFAULT_Y = (primScreenBounds.getHeight() - GUIRun.currStage.getHeight()) / 2;
    public final double DEFAULT_WIDTH = 900;
    public final double DEFAULT_HEIGHT = 500;
    public final boolean DEFAULT_MAXIMIZED = false;
    public String node_name;
    public Preferences pref;

    public SaveWindowPosition(String NODE_NAME) {
        this.node_name = NODE_NAME;
        this.pref = Preferences.userRoot().node(NODE_NAME);
    }

    public double getX() {
        return pref.getDouble(WINDOW_POSITION_X, DEFAULT_X);
    }

    public double getY() {
        return pref.getDouble(WINDOW_POSITION_Y, DEFAULT_Y);
    }

    public double getWidth() {
        return pref.getDouble(WINDOW_WIDTH, DEFAULT_WIDTH);
    }

    public double getHeight() {
        return pref.getDouble(WINDOW_HEIGHT, DEFAULT_HEIGHT);
    }

    public boolean isFullscreen() {
        return pref.getBoolean(IS_MAXIMIZED, DEFAULT_MAXIMIZED);
    }


    public void updatePref() {
        pref.putDouble(WINDOW_POSITION_X, GUIRun.currStage.getX());
        pref.putDouble(WINDOW_POSITION_Y, GUIRun.currStage.getY());
        pref.putDouble(WINDOW_WIDTH, GUIRun.currStage.getWidth());
        pref.putDouble(WINDOW_HEIGHT, GUIRun.currStage.getHeight());
        pref.putBoolean(IS_MAXIMIZED, GUIRun.currStage.isMaximized());
    }

    public void defaultSetting() {
        GUIRun.currStage.setResizable(true);

        // prevent from resizing
        GUIRun.currStage.setMinWidth(900);
        GUIRun.currStage.setMinHeight(500);

        if (isFullscreen()) {
            GUIRun.currStage.setMaximized(true);
        } else {
            // resize the window to saved size
            GUIRun.currStage.setWidth(getWidth());
            GUIRun.currStage.setHeight(getHeight());

            // place window to saved position
            GUIRun.currStage.setX(getX());
            GUIRun.currStage.setY(getY());
        }

    }

}

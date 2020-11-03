package com.OlimpiaComarnic.GUI.Popup;

import com.OlimpiaComarnic.GUI.GUIRun;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class Popup1 extends Application{
    @FXML
    Button closeButt;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent parent = FXMLLoader.load(Popup1.class.getResource("Popup1.fxml"));
        Stage newStage = new Stage();

        Scene scene = new Scene(parent);
        newStage.setResizable(false);
        newStage.setTitle("Error");
        newStage.setScene(scene);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primaryStage);

        newStage.show();
    }

    @FXML
    private void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage) closeButt.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public static Stage returnMainStage() {
        return (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
    }
}
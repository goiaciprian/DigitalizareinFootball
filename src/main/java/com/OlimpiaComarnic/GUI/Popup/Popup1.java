package com.OlimpiaComarnic.GUI.Popup;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Popup for network errors.
 */
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
        Stage stage = (Stage) closeButt.getScene().getWindow();
        stage.close();
    }
}
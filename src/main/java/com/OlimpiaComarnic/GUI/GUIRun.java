package com.OlimpiaComarnic.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIRun  extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(GUIRun.class.getResource("test.fxml"));

        Scene scene = new Scene(parent);
        primaryStage.setTitle("MERGE wai");
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}

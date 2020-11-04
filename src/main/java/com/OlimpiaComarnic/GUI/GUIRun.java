package com.OlimpiaComarnic.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUIRun  extends Application {

    static Stage currStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(GUIRun.class.getResource("LogIn.fxml"));

        Scene scene = new Scene(parent);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Olimpia Comarnic Manager");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(GUIRun.class.getResourceAsStream("olimpiaCom.png")));

        currStage = primaryStage;

        primaryStage.show();
    }
}

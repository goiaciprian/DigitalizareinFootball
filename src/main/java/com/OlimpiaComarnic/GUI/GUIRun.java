package com.OlimpiaComarnic.GUI;

import com.OlimpiaComarnic.Backend.dao.EvenimentDAO;
import com.OlimpiaComarnic.Backend.utils.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.concurrent.CompletableFuture;

public class GUIRun extends Application {

    public static Stage currStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //opens database connection
        CompletableFuture.runAsync(DBConnection::createConn);

        // Deletes past events;
        EvenimentDAO.checkPastEvents();

        currStage = primaryStage;

        Parent parent = FXMLLoader.load(GUIRun.class.getResource("LogIn.fxml"));
        Scene scene = new Scene(parent);

        primaryStage.setTitle("Olimpia Comarnic Manager");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(GUIRun.class.getResourceAsStream("olimpiaCom.png")));

        //add closing event listener to close database connection and to the timer if there is any
        primaryStage.setOnCloseRequest(windowEvent -> {
            CompletableFuture.runAsync(DBConnection::closeConn);
            try {
                userWindowController.schedule.cancel();
            } catch (NullPointerException ignored) {
            }
            try {
                adminWindowController.schedule.cancel();
            } catch (NullPointerException ignored) {
            }
        });
        primaryStage.show();
    }
}

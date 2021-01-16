package com.OlimpiaComarnic.GUI;

import com.OlimpiaComarnic.Backend.dao.EvenimentDAO;
import com.OlimpiaComarnic.Backend.utils.DBConnection;
import com.OlimpiaComarnic.GUI.Utils.Updater;
import com.OlimpiaComarnic.GUI.Utils.Version;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

public class GUIRun extends Application {

    public static Stage currStage;
    public Timer timer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //opens database connection
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
            try {
                timer.cancel();
            } catch (NullPointerException ignored) {
            }
        });
        CompletableFuture.runAsync(DBConnection::createConn);

        new Updater.UpdaterBuilder()
                .setUrl("https://api.github.com/repos/goiaciprian/DigitalizareinFootball/releases/latest")
                .build()
                .Start();

        // Deletes past events;
        checkEventsSchedule();

        currStage = primaryStage;

        Parent parent = FXMLLoader.load(GUIRun.class.getResource("LogIn.fxml"));
        Scene scene = new Scene(parent);

        primaryStage.setTitle("Olimpia Comarnic Manager");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(GUIRun.class.getResourceAsStream("olimpiaCom.png")));

        primaryStage.show();
    }

    private void checkEventsSchedule() {
        timer = new Timer();
        TimerTask update = new TimerTask() {
            @Override
            public void run() {
                try {
                    EvenimentDAO.checkPastEvents().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(update, 5 * 1000, 5 * 1000);
    }
}

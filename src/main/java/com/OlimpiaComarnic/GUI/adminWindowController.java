package com.OlimpiaComarnic.GUI;

import com.OlimpiaComarnic.Backend.dao.EvenimentDAO;
import com.OlimpiaComarnic.Backend.dao.PlayerDAO;
import com.OlimpiaComarnic.Backend.entity.Eveniment;
import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.GUI.Managers.eventsManagerController;
import com.OlimpiaComarnic.GUI.Managers.playersManagerController;
import com.OlimpiaComarnic.GUI.Utils.SaveWindowPosition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.DepthTest;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;


public class adminWindowController {

    static final SaveWindowPosition position = new SaveWindowPosition("adminWindow");
    public static Timer schedule;
    Eveniment next = EvenimentDAO.getNextEvent();

    @FXML
    AnchorPane anchorPane;

    @FXML
    ScrollPane scrollPane;

    @FXML
    Button logOutButt, managerEvents, managerJucatori;

    @FXML
    Label tipEventNext, dataEventNext;

    @FXML
    BarChart<String, Number> goluriChart, paseGolChart, aparitiiChart, cartonaseChart;

    @FXML
    CategoryAxis xAxisNumeG, xAxisNumeP, xAxisNumeA;

    @FXML
    NumberAxis yAxisGoluri, yAxisPaseGol, yAxisAparitii, yAxisCartonase;


    @FXML
    void initialize() {
        position.defaultSetting();
        CompletableFuture.runAsync(() -> Platform.runLater(this::initUI));
        CompletableFuture.runAsync(() -> Platform.runLater(this::initCharts));
        CompletableFuture.runAsync(this::backgroundUpdate);
    }

    @FXML
    public void logOut() {
        try {
            position.updatePref();
            schedule.cancel();
            anchorPane.getScene().setRoot(FXMLLoader.load(GUIRun.class.getResource("LogIn.fxml")));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void eventsManager() {
        try {
            new eventsManagerController().start(GUIRun.currStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playersManager() {
        try {
            new playersManagerController().start(GUIRun.currStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initUI() {
        if (next != null) {
            tipEventNext.setText(next.getEvent());
            tipEventNext.setMinWidth(Region.USE_PREF_SIZE);

            SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
            dataEventNext.setText(SimpleDateFormat.format(next.getDate()));
            dataEventNext.setMinWidth(Region.USE_PREF_SIZE);

        } else {
            tipEventNext.setText("Nu sunt evenimente");
            dataEventNext.setText("");
        }
    }

    private void initCharts() {
        yAxisGoluri.setLabel("Nr. goluri");
        yAxisPaseGol.setLabel("Nr. pase de gol");
        yAxisAparitii.setLabel("Minute jucate");
        yAxisCartonase.setLabel("Cartonase");
        XYChart.Series<String, Number> s1 = new XYChart.Series<>();
        XYChart.Series<String, Number> s2 = new XYChart.Series<>();
        XYChart.Series<String, Number> s3 = new XYChart.Series<>();
        XYChart.Series<String, Number> s4 = new XYChart.Series<>();
        XYChart.Series<String, Number> s5 = new XYChart.Series<>();

        List<Player> players = PlayerDAO.findAll();
        for (Player player : players) {
            s1.getData().add(new XYChart.Data<>(player.getNume(), player.getGoluri()));
            s2.getData().add(new XYChart.Data<>(player.getNume(), player.getPaseGol()));
            int s = 0;
            for (Map.Entry<String, Integer> kv : player.getAparitii().entrySet()) {
                s += kv.getValue();
            }
            s3.getData().add(new XYChart.Data<>(player.getNume(), s));
            s4.getData().add(new XYChart.Data<>(player.getNume(), player.getCartonaseGalbene()));
            s5.getData().add(new XYChart.Data<>(player.getNume(), player.getCartonaseRosii()));

        }
        if (goluriChart.getData().isEmpty())
            goluriChart.getData().add(s1);
        else {
            goluriChart.getData().set(0, s1);
        }
        goluriChart.setAnimated(false);
        goluriChart.setLegendVisible(false);

        if (paseGolChart.getData().isEmpty())
            paseGolChart.getData().add(s2);
        else {
            paseGolChart.getData().set(0, s2);
        }
        paseGolChart.setAnimated(false);
        paseGolChart.setLegendVisible(false);

        if (aparitiiChart.getData().isEmpty())
            aparitiiChart.getData().add(s3);
        else {
            aparitiiChart.getData().set(0, s3);
        }
        aparitiiChart.setAnimated(false);
        aparitiiChart.setLegendVisible(false);

        if (cartonaseChart.getData().isEmpty()) {
            cartonaseChart.getData().add(s4);
            cartonaseChart.getData().add(s5);
        } else {
            cartonaseChart.getData().set(0, s4);
            cartonaseChart.getData().set(1, s5);
        }

        try {
            Set<Node> ns = cartonaseChart.lookupAll(".default-color0.chart-bar");
            for (Node n : ns)
                n.setStyle("-fx-bar-fill: yellow");
        } catch (Exception e) {
            e.printStackTrace();
        }

        cartonaseChart.setAnimated(false);
        cartonaseChart.setLegendVisible(false);

    }

    private void backgroundUpdate() {
        schedule = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                next = EvenimentDAO.getNextEvent();
                Platform.runLater(() -> {
                    initUI();
                    initCharts();
                });
            }
        };
        schedule.scheduleAtFixedRate(task, 2 * 1000, 2 * 1000);
    }

}

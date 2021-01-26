package com.OlimpiaComarnic.GUI;

import com.OlimpiaComarnic.Backend.dao.EvenimentDAO;
import com.OlimpiaComarnic.Backend.dao.PlayerDAO;
import com.OlimpiaComarnic.Backend.entity.Eveniment;
import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.GUI.Utils.SaveWindowPosition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class userWindowController {

    static final SaveWindowPosition position = new SaveWindowPosition("userWindow");
    public static Timer schedule;
    Player loggedIn = LogInController.loggedIn;
    Eveniment next = EvenimentDAO.getNextEvent();


    @FXML
    AnchorPane anchorPane;

    @FXML
    Button logOutButt;

    @FXML
    Label helloText;

    @FXML
    Label nume, nrTricou, goluri, paseDeGol, cartGalben, cartRosii, tipEvent, dataEvent;

    @FXML
    BarChart<String, Number> aparitiiChart;

    @FXML
    CategoryAxis xAxisNume;

    @FXML
    NumberAxis yAxisMinute;


    private int numarDeAparitiiInChart = 0;

    @FXML
    void initialize() {
        position.defaultSetting();
        initUI();
        initChart();
        backgroundUpdate();
    }

    @FXML
    public void logOut() {
        try {
            schedule.cancel();
            LogInController.loggedIn = null;
            position.updatePref();
            anchorPane.getScene().setRoot(FXMLLoader.load(GUIRun.class.getResource("LogIn.fxml")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void initUI() {

        helloText.setText(loggedIn.getNume());
        helloText.setMinWidth(Region.USE_PREF_SIZE);
        nume.setText(loggedIn.getNume());
        nume.setMinWidth(Region.USE_PREF_SIZE);
        nrTricou.setText(String.valueOf(loggedIn.getNumarTricou()));
        nrTricou.setMinWidth(Region.USE_PREF_SIZE);
        goluri.setText(String.valueOf(loggedIn.getGoluri()));
        goluri.setMinWidth(Region.USE_PREF_SIZE);
        paseDeGol.setText(String.valueOf(loggedIn.getPaseGol()));
        paseDeGol.setMinWidth(Region.USE_PREF_SIZE);
        cartGalben.setText(String.valueOf(loggedIn.getCartonaseGalbene()));
        cartGalben.setMinWidth(Region.USE_PREF_SIZE);
        cartRosii.setText(String.valueOf(loggedIn.getCartonaseRosii()));
        cartRosii.setMinWidth(Region.USE_PREF_SIZE);

        if (next == null) {
            tipEvent.setText("Nu sunt evenimente");
            dataEvent.setVisible(false);
            return;
        }
        dataEvent.setVisible(true);
        tipEvent.setText(next.getEvent());
        tipEvent.setMinWidth(Region.USE_PREF_SIZE);

        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        dataEvent.setText(SimpleDateFormat.format(next.getDate()));
        dataEvent.setMinWidth(Region.USE_PREF_SIZE);

    }

    private void initChart() {
        Player loggedIn = LogInController.loggedIn;
        xAxisNume.setLabel("Meciuri");
        yAxisMinute.setLabel("Minute");
        XYChart.Series<String, Number> s1 = new XYChart.Series<>();
        int currNrItems = 0;
        final int maxNrItems = 10;
        for (Map.Entry<String, Integer> e : PlayerDAO.findOneByUsername(loggedIn.getUsername()).getAparitii().entrySet()) {
            if (currNrItems < maxNrItems) {
                s1.getData().add(new XYChart.Data<>(e.getKey(), e.getValue()));
                currNrItems++;
            }
        }
        int size = s1.getData().size();
        if (aparitiiChart.getData().isEmpty())
            aparitiiChart.getData().add(s1);
        else if(size != numarDeAparitiiInChart)
            aparitiiChart.getData().set(0, s1);

        aparitiiChart.setAnimated(false);
        aparitiiChart.setLegendVisible(false);

        numarDeAparitiiInChart = size;
    }

    private void backgroundUpdate() {
        schedule = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                loggedIn = PlayerDAO.findOneByUsername(loggedIn.getUsername());
                next = EvenimentDAO.getNextEvent();
                Platform.runLater(() -> {
                    try {
                        initUI();
                        initChart();
                    } catch (NullPointerException ignored) {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        };
        schedule.scheduleAtFixedRate(task, 2 * 1000, 2 * 1000);
    }

}

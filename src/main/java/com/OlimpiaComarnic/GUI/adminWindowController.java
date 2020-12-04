package com.OlimpiaComarnic.GUI;

import com.OlimpiaComarnic.Backend.dao.EvenimentDAO;
import com.OlimpiaComarnic.Backend.dao.PlayerDAO;
import com.OlimpiaComarnic.Backend.entity.Eveniment;
import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.GUI.Utils.SaveWindowPosition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class adminWindowController {

    private final SaveWindowPosition position = new SaveWindowPosition("adminWindow");
    public static Timer schedule;
    Eveniment next = EvenimentDAO.getNextEvent();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button logOutButt;

    @FXML
    private Label tipEventNext, dataEventNext;

    @FXML
    BarChart<String, Number> goluriChart, paseGolChart, aparitiiChart, cartonaseChart;

    @FXML
    CategoryAxis xAxisNumeG, xAxisNumeP, xAxisNumeA;

    @FXML
    NumberAxis yAxisGoluri, yAxisPaseGol, yAxisAparitii, yAxisCartonase;

    @FXML
    void initialize() {
        position.defaultSetting();
        initUI();
        initCharts();
        backgroundUpdate();
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

    private void initUI() {
        if (next != null) {
            tipEventNext.setText(next.getEvent());
            tipEventNext.setMinWidth(Region.USE_PREF_SIZE);

            SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            dataEventNext.setText(SimpleDateFormat.format(next.getDate()));
            dataEventNext.setMinWidth(Region.USE_PREF_SIZE);

        } else {
            tipEventNext.setText("Nu sunt sunt evenimente viitoare");
            dataEventNext.setVisible(false);
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

        int currNrItems = 0;
        List<Player> players = PlayerDAO.findAll();
        final int maxNrItems = players.size();
        for (int i = 0; i < maxNrItems; i++) {
            if (currNrItems < maxNrItems) {
                s1.getData().add(new XYChart.Data<>(players.get(i).getNume(), players.get(i).getGoluri()));
                s2.getData().add(new XYChart.Data<>(players.get(i).getNume(), players.get(i).getPaseGol()));
                int s = 0;
                for (Map.Entry<String, Integer> kv : players.get(i).getAparitii().entrySet()) {
                    s += kv.getValue();
                }
                s3.getData().add(new XYChart.Data<>(players.get(i).getNume(), s));
                s4.setName("Galbene");
                s5.setName("Rosii");
                s4.getData().add(new XYChart.Data<>(players.get(i).getNume(), players.get(i).getCartonaseGalbene()));
                s5.getData().add(new XYChart.Data<>(players.get(i).getNume(), players.get(i).getCartonaseRosii()));
            }
            currNrItems++;
        }
        if (goluriChart.getData().isEmpty())
            goluriChart.getData().add(s1);
        else {
            if (!aparitiiChart.getData().get(0).toString().equals(s1.toString()))
                goluriChart.getData().set(0, s1);
        }
        goluriChart.setLegendVisible(false);

        if (paseGolChart.getData().isEmpty())
            paseGolChart.getData().add(s2);
        else {
            if (!aparitiiChart.getData().get(0).toString().equals(s2.toString()))
                paseGolChart.getData().set(0, s2);
        }
        paseGolChart.setLegendVisible(false);

        if (aparitiiChart.getData().isEmpty())
            aparitiiChart.getData().add(s3);
        else {
            if (!aparitiiChart.getData().get(0).toString().equals(s3.toString()))
                aparitiiChart.getData().set(0, s3);
        }
        aparitiiChart.setLegendVisible(false);

        if (cartonaseChart.getData().isEmpty()) {
            cartonaseChart.getData().add(s4);
            cartonaseChart.getData().add(s5);
        } else {
            if (!cartonaseChart.getData().get(0).toString().equals(s4.toString()) || !cartonaseChart.getData().get(1).toString().equals(s5.toString())) {
                cartonaseChart.getData().set(0, s4);
                cartonaseChart.getData().set(1, s5);
            }
        }

        try {
            Node n = cartonaseChart.lookup(".default-color0.chart-bar");
            n.setStyle("-fx-bar-fill: yellow");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        schedule.scheduleAtFixedRate(task, 15 * 1000, 15 * 1000);
    }

}

package com.OlimpiaComarnic.GUI;

import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.GUI.Utils.SaveWindowPosition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.util.Map;


public class userWindowController {

    private final SaveWindowPosition position = new SaveWindowPosition("userWindow");

    @FXML
    AnchorPane anchorPane;

    @FXML
    Button logOutButt;

    @FXML
    Label helloText;

    @FXML
    Label nume, nrTricou, goluri, paseDeGol, cartGalben, cartRosii;

    @FXML
    BarChart<String, Number> aparitiiChart;

    @FXML
    CategoryAxis xAxisNume;

    @FXML
    NumberAxis yAxisMinute;

    @FXML
    void initialize() {
        position.defaultSetting();
        initUI();
        initChart();
    }

    @FXML
    public void logOut() {
        try {
            LogInController.loggedIn = null;
            position.updatePref();
            anchorPane.getScene().setRoot(FXMLLoader.load(GUIRun.class.getResource("LogIn.fxml")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void initUI() {
        Player loggedIn = LogInController.loggedIn;
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
    }

    private void initChart() {
        Player loggedIn = LogInController.loggedIn;
        xAxisNume.setLabel("Meciuri");
        yAxisMinute.setLabel("Minute");
        XYChart.Series<String, Number> s1 = new XYChart.Series<>();
        for (Map.Entry<String, Integer> e : loggedIn.getAparitii().entrySet()) {
            s1.getData().add(new XYChart.Data<>(e.getKey(), e.getValue()));
        }
        aparitiiChart.getData().add(s1);
        aparitiiChart.setLegendVisible(false);
    }

}

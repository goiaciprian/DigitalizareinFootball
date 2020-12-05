package com.OlimpiaComarnic.GUI.Managers;

import com.OlimpiaComarnic.Backend.dao.EvenimentDAO;
import com.OlimpiaComarnic.Backend.entity.Eveniment;
import com.OlimpiaComarnic.GUI.GUIRun;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tornadofx.control.DateTimePicker;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class eventsManagerController extends Application {

    private Eveniment isSelected = null;
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    public static Timer schedule;

    @FXML
    AnchorPane createView;

    @FXML
    TextField numeEventCreate;

    @FXML
    Button eventCreate, deleteEvent;

    @FXML
    ListView<Eveniment> allEvents;

    @FXML
    public void initialize() {
        getAllEvents();
        changeView();
        initPicker();
        initListView();
        CompletableFuture.runAsync(this::backgroundUpdateList);
        createView.setOnMouseClicked((e) -> {
            isSelected = null;
            changeView();
            createView.requestFocus();
        });
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        CompletableFuture.runAsync(EvenimentDAO::checkPastEvents);
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("com/OlimpiaComarnic/GUI/EventsManager/eventsManager.fxml")));
        Stage newStage = new Stage();
        Scene scene = new Scene(parent);
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.setTitle("Olimpia Comarnic Evenimente Manager");
        newStage.getIcons().add(new Image(GUIRun.class.getResourceAsStream("olimpiaCom.png")));
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primaryStage);
        newStage.setOnCloseRequest(windowEvent -> {
            if (schedule != null) {
                schedule.cancel();
            }
        });

        newStage.show();
    }

    public void updateEvent() {
        String fromField = numeEventCreate.getText();
        LocalDateTime fromPicker = dateTimePicker.getDateTimeValue();
        if (!fromField.equals("")) {
            LocalDateTime curr = LocalDateTime.now();
            if (curr.compareTo(fromPicker) < 0) {
                try {
                    EvenimentDAO.updateEventById(isSelected.get_id(), new Eveniment(fromField, Timestamp.valueOf(fromPicker))).get();
                    getAllEvents();
                    isSelected = null;
                    changeView();
                    createView.requestFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createEvent() {
        String nume = numeEventCreate.getText();
        LocalDateTime fromPicker = dateTimePicker.getDateTimeValue();
        if (!nume.equals("")) {
            LocalDateTime curr = LocalDateTime.now();
            if (curr.compareTo(fromPicker) < 0) {
                try {
                    EvenimentDAO.insertNewEvent(new Eveniment(nume, Timestamp.valueOf(fromPicker))).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getAllEvents();
            }
        }
    }

    private void getAllEvents() {
        List<Eveniment> found = EvenimentDAO.findAll();
        Collections.sort(found);
        ObservableList<Eveniment> labelsEvents = FXCollections.observableArrayList(found);
        allEvents.setItems(labelsEvents);
        allEvents.setFocusTraversable(false);
    }

    private void initListView() {
        allEvents.setCellFactory(lv -> {
            ListCell<Eveniment> cell = new ListCell<Eveniment>() {
                @Override
                protected void updateItem(Eveniment item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null)
                        setText(item.getEvent());
                }
            };
            cell.setOnMouseClicked(e -> {
                if (!cell.isEmpty()) {
                    isSelected = cell.getItem();
                    changeView();
                    e.consume();
                }
            });
            return cell;
        });

        allEvents.setOnMouseClicked(e -> {
            isSelected = null;
            changeView();
        });
    }

    private void changeView() {
        createView.setVisible(true);
        if (isSelected == null) {
            numeEventCreate.setText("");
            numeEventCreate.setPromptText("Exemplu: Meci: Barceona");
            dateTimePicker.setDateTimeValue(LocalDateTime.now());
            eventCreate.setText("Submit");
            eventCreate.setOnAction((e) -> createEvent());
            deleteEvent.setDisable(true);
        } else {
            String numeEvent = isSelected.getEvent();
            Date dataEvent = isSelected.getDate();
            numeEventCreate.setText(numeEvent);
            dateTimePicker.setDateTimeValue(dataEvent.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            eventCreate.setText("Update");
            eventCreate.setOnAction((e) -> updateEvent());
            deleteEvent.setDisable(false);
            deleteEvent.setOnAction((e) -> {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Delete eveniment");
                confirm.initOwner(GUIRun.currStage);
                confirm.setHeaderText("Stergere eveniment: " + isSelected.getEvent() + ".");
                confirm.setContentText("Esti sigur ca vrei sa continui?");
                Optional<ButtonType> result = confirm.showAndWait();
                if (result.isPresent())
                    if (result.get() != ButtonType.OK) {
                        return;
                    }
                try {
                    EvenimentDAO.deleteEventById(isSelected.get_id()).get();
                    getAllEvents();
                    initListView();
                    isSelected = null;
                    changeView();
                } catch (Exception interruptedException) {
                    interruptedException.printStackTrace();
                }
            });
        }
    }

    private void initPicker() {
        dateTimePicker.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("com/OlimpiaComarnic/GUI/EventsManager/eventsManagerCSS.css")).toString());
        dateTimePicker.getStyleClass().add("pickerBorders");
        dateTimePicker.setDateTimeValue(LocalDateTime.now());
        dateTimePicker.setLayoutX(180);
        dateTimePicker.setLayoutY(145);
        createView.getChildren().add(dateTimePicker);
    }

    private void backgroundUpdateList() {
        TimerTask update = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> getAllEvents());
            }
        };

        schedule = new Timer();
        schedule.scheduleAtFixedRate(update, 15 * 1000, 15 * 1000);
    }
}

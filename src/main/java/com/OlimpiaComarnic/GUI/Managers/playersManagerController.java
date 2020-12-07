package com.OlimpiaComarnic.GUI.Managers;

import com.OlimpiaComarnic.Backend.dao.PlayerDAO;
import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.GUI.GUIRun;
import com.OlimpiaComarnic.GUI.Managers.Utils.Aparitie;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class playersManagerController extends Application {

    private Player isSelected = null;

    @FXML
    AnchorPane root;

    @FXML
    ListView<Player> allPlayers;

    @FXML
    TableView<Aparitie> aparitii;

    @FXML
    TableColumn<Aparitie, String> numeMeci;

    @FXML
    TableColumn<Aparitie, Integer> minuteInMeci;

    @FXML
    public void initialize() {
        getAllPlayers();
        ObservableList<Aparitie> data = FXCollections.observableArrayList(
                new Aparitie("asda", 78),
                new Aparitie("dasdsafs", 90),
                new Aparitie("dsadsad", 55)
        );
        numeMeci.setCellFactory(cell -> new TextFieldTableCell<>(new DefaultStringConverter()));
        numeMeci.setCellValueFactory(new PropertyValueFactory<>("numeMeci"));
        minuteInMeci.setCellValueFactory(new PropertyValueFactory<>("minuteJucate"));
        aparitii.setItems(data);


        root.setOnMouseClicked((e) -> {
            isSelected = null;
//            changeView();
            root.requestFocus();
            allPlayers.getSelectionModel().select(-1);
        });
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("com/OlimpiaComarnic/GUI/PlayersManager/playersManager.fxml")));
        Stage newStage = new Stage();
        Scene scene = new Scene(parent);
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.setTitle("Olimpia Comarnic Jucatori Manager");
        newStage.getIcons().add(new Image(GUIRun.class.getResourceAsStream("olimpiaCom.png")));
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primaryStage);
        newStage.show();
    }

    private void getAllPlayers() {
        List<Player> allFromDB = PlayerDAO.findAll();
        ObservableList<Player> toListViews = FXCollections.observableArrayList(allFromDB);
        allPlayers.setItems(toListViews);
        allPlayers.setFocusTraversable(false);
        initListView();
    }

    private void initListView() {
        allPlayers.setCellFactory(lv -> {
            ListCell<Player> cell = new ListCell<Player>() {
                @Override
                protected void updateItem(Player item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null)
                        setText(item.getNume());
                }
            };
            cell.setOnMouseClicked(e -> {
                if (!cell.isEmpty()) {
                    isSelected = cell.getItem();
//                    changeView();
                    e.consume();
                }
            });
            return cell;
        });

        allPlayers.setOnMouseClicked(e -> {
            isSelected = null;
            allPlayers.getSelectionModel().select(-1);
//            changeView();
        });
    }
}

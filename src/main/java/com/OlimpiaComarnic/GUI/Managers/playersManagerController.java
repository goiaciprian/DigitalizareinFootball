package com.OlimpiaComarnic.GUI.Managers;

import com.OlimpiaComarnic.Backend.dao.PlayerDAO;
import com.OlimpiaComarnic.Backend.dao.UserDAO;
import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.Backend.entity.User;
import com.OlimpiaComarnic.GUI.GUIRun;
import com.OlimpiaComarnic.GUI.Managers.Utils.Aparitie;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class playersManagerController extends Application {

    private Player playerSelected = null;
    private Aparitie aparitieSelected = null;
    private Timer refresh;

    @FXML
    AnchorPane root, infoAnchor, aparitiiAnchor;

    @FXML
    ListView<Player> allPlayers;

    @FXML
    TableView<Aparitie> aparitii;

    @FXML
    TableColumn<Aparitie, String> numeMeci;

    @FXML
    TableColumn<Aparitie, Integer> minuteInMeci;

    @FXML
    TextField username, nume, nrTricou, goluri, paseDeGol, cartGalbene, cartRosii, aparitiiNume, aparitiiMinute;

    @FXML
    Button playersDelete, playersAddModify, aparitiiDelete, aparitiiAddModify;

    @FXML
    Label passwordLabel;

    @FXML
    PasswordField passwordField;

    @FXML
    TabPane tabs;

    @FXML
    public void initialize() {
        CompletableFuture.runAsync(this::backgroundUpdate);
        getAllPlayers();
        initTable();
        changeView();
        anchorsEvents();
        playerDeleteAction();
        aparitieDeleteAction();
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
        newStage.setOnCloseRequest(e -> {
            try {
                if (refresh != null)
                    refresh.cancel();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        });
    }

    private void anchorsEvents() {
        root.setOnMouseClicked((e) -> {
            playerSelected = null;
            aparitieSelected = null;
            changeView();
            root.requestFocus();
            allPlayers.getSelectionModel().select(-1);
            aparitii.getSelectionModel().clearSelection();
            aparitii.setItems(FXCollections.observableArrayList());
            passwordField.clear();
        });

        infoAnchor.setOnMouseClicked((e) -> {
            playerSelected = null;
            aparitieSelected = null;
            changeView();
            root.requestFocus();
            aparitii.getSelectionModel().clearSelection();
            aparitii.setItems(FXCollections.observableArrayList());
            allPlayers.getSelectionModel().select(-1);
            passwordField.clear();
        });

        aparitiiAnchor.setOnMouseClicked((e) -> {
            aparitieSelected = null;
            changeView();
            root.requestFocus();
            aparitii.getSelectionModel().clearSelection();
            passwordField.clear();
        });

    }

    private void addAparitie() {
        int minute;
        String nume;
        AtomicBoolean inList = new AtomicBoolean(false);
        try {
            nume = aparitiiNume.getText();
            minute = Integer.parseInt(aparitiiMinute.getText());
            if (nume.equals("") || minute <= 0)
                return;
        } catch (NumberFormatException ignored) {
            return;
        }
        aparitii.getItems().forEach(ap -> {
            if (ap.getNumeMeci().equals(nume)) {
                inList.set(true);
            }
        });
        if (!inList.get()) {
            aparitii.getItems().add(new Aparitie(nume, minute));
            if (playerSelected != null) {
                playerSelected.addAparitie(minute, nume);
                PlayerDAO.updateOne(playerSelected.getUsername(), playerSelected);
            }
        }

        initTableEvents();
    }

    private void modificaAparitie() {
        String nume;
        int minute;

        try {
            nume = aparitiiNume.getText();
            minute = Integer.parseInt(aparitiiMinute.getText());
            if (nume.equals("") || minute <= 0)
                return;
        } catch (NumberFormatException ignored) {
            return;
        }


        int index = aparitii.getSelectionModel().getSelectedIndex();
        Aparitie changedAp = new Aparitie(nume, minute);
        playerSelected.updateAparitie(aparitieSelected.getNumeMeci(), changedAp.getNumeMeci(), changedAp.getMinuteJucate());
        PlayerDAO.updateOne(playerSelected.getUsername(), playerSelected);
        aparitieSelected = changedAp;
        aparitii.getItems().set(index, changedAp);
        aparitii.getSelectionModel().select(index);

        aparitiiMinute.clear();
        aparitiiNume.clear();

        initTable();

    }

    private void aparitieDeleteAction() {
        aparitiiDelete.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Sterge aparitie");
            confirm.initOwner(GUIRun.currStage);
            confirm.setHeaderText("Stergere aparitie: " + aparitieSelected.getNumeMeci() + ".");
            confirm.setContentText("Esti sigur ca vrei sa continui?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent())
                if (result.get() != ButtonType.OK) {
                    return;
                }
            aparitii.getItems().remove(aparitii.getSelectionModel().getSelectedIndex());
            initTableEvents();
            aparitiiNume.clear();
            aparitiiMinute.clear();
            playerSelected.removeAparitie(aparitieSelected.getNumeMeci());
            PlayerDAO.updateOne(playerSelected.getUsername(), playerSelected);
        });

    }

    private void addPlayer() {
        if (username.getText().equals("") || nume.getText().equals("") || passwordField.getText().equals(""))
            return;
        User newUser = new User(username.getText(), passwordField.getText(), false);
        Player newPlayer = new Player();

        newPlayer.setUsername(username.getText());
        newPlayer.setNume(nume.getText());

        setNewPlayerStats(newPlayer);

        allPlayers.getItems().add(newPlayer);

        aparitieSelected = null;
        playerSelected = null;
        passwordField.clear();
        changeView();

        UserDAO.insertUser(newUser);
        PlayerDAO.insertPlayer(newPlayer);
        aparitii.setItems(FXCollections.observableArrayList());

    }

    private void modificaPlayer() {
        String usernameS = username.getText();
        String numeS = nume.getText();
        String passS = passwordField.getText();
        if (usernameS.equals("") || numeS.equals(""))
            return;

        User newUser = null;
        if (!usernameS.equals(playerSelected.getUsername())) {
            if (passS.equals(""))
                return;
            newUser = new User(usernameS, passS, false);
            UserDAO.updateUserByUsername(playerSelected.getUsername(), newUser);
        }

        Player newPlayer = new Player();

        newPlayer.setUsername(username.getText());
        newPlayer.setNume(numeS);

        setNewPlayerStats(newPlayer);


        PlayerDAO.updateOne(playerSelected.getUsername(), newPlayer);
        if (newUser == null) {
            int index = allPlayers.getSelectionModel().getSelectedIndex();
            allPlayers.getSelectionModel().select(index);
            allPlayers.getItems().set(index, newPlayer);
            playerSelected = newPlayer;
        } else {
            playerSelected = null;
            aparitieSelected = null;
        }
        changeView();
        initTableEvents();
    }

    private void playerDeleteAction() {
        playersDelete.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Sterge jucator");
            confirm.initOwner(GUIRun.currStage);
            confirm.setHeaderText("Stergere jucator: " + playerSelected.getNume() + ".");
            confirm.setContentText("Esti sigur ca vrei sa continui?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent())
                if (result.get() != ButtonType.OK) {
                    return;
                }
            PlayerDAO.deleteOne(playerSelected.getUsername());
            UserDAO.deleteUser(playerSelected.getUsername());
            int selected = allPlayers.getSelectionModel().getSelectedIndex();
            allPlayers.getItems().remove(selected);
            initListView();
            playerSelected = null;
            aparitieSelected = null;
            allPlayers.getSelectionModel().clearSelection();
            changeView();

        });
    }

    private void setNewPlayerStats(Player newPlayer) {
        CompletableFuture<Void> addAparitiiAsync = CompletableFuture.runAsync(() -> {
            for (Aparitie a : aparitii.getItems()) {
                newPlayer.addAparitie(a.getMinuteJucate(), a.getNumeMeci());
            }
        });
        try {
            try {
                newPlayer.setNumarTricou(Integer.parseInt(nrTricou.getText()));
            } catch (NumberFormatException ignored) {
            }

            try {
                newPlayer.setGoluri(Integer.parseInt(goluri.getText()));
            } catch (NumberFormatException ignored) {
            }

            try {
                newPlayer.setPaseGol(Integer.parseInt(paseDeGol.getText()));
            } catch (NumberFormatException ignored) {
            }

            try {
                newPlayer.setCartonaseGalbene(Integer.parseInt(cartGalbene.getText()));
            } catch (NumberFormatException ignored) {
            }

            try {
                newPlayer.setCartonaseRosii(Integer.parseInt(cartRosii.getText()));
            } catch (NumberFormatException ignored) {
            }

            addAparitiiAsync.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    playerSelected = cell.getItem();
                    initTable();
                    changeView();
                    aparitiiNume.setText("");
                    aparitiiMinute.setText("");

                    e.consume();
                }
            });
            return cell;
        });

        allPlayers.setOnMouseClicked(e -> {
            playerSelected = null;
            aparitieSelected = null;
            allPlayers.getSelectionModel().select(-1);
            aparitii.setItems(FXCollections.observableArrayList());
            changeView();
        });
    }

    private void initTable() {
        ObservableList<Aparitie> data = FXCollections.observableArrayList();
        if (playerSelected != null) {
            for (Map.Entry<String, Integer> e : playerSelected.getAparitii().entrySet()) {
                data.add(new Aparitie(e.getKey(), e.getValue()));
            }
        }
        numeMeci.setCellFactory(cell -> new TextFieldTableCell<>(new DefaultStringConverter()));
        numeMeci.setCellValueFactory(new PropertyValueFactory<>("numeMeci"));
        minuteInMeci.setCellValueFactory(new PropertyValueFactory<>("minuteJucate"));
        aparitii.setItems(data);
        aparitii.setOnMouseClicked(event -> {
            aparitii.getSelectionModel().clearSelection();
            aparitieSelected = null;
            changeView();
        });
        initTableEvents();
    }

    private void initTableEvents() {
        aparitii.setRowFactory(tv -> {
            TableRow<Aparitie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    aparitieSelected = row.getItem();
                    changeView();
                } else if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    aparitii.getSelectionModel().clearSelection();
                    aparitieSelected = null;
                    changeView();
                }
                event.consume();
            });
            return row;
        });
    }

    private void changeView() {

        if (playerSelected != null) {
            username.setText(playerSelected.getUsername());
            nume.setText(playerSelected.getNume());
            nrTricou.setText(String.valueOf(playerSelected.getNumarTricou()));
            goluri.setText(String.valueOf(playerSelected.getGoluri()));
            paseDeGol.setText(String.valueOf(playerSelected.getPaseGol()));
            cartGalbene.setText(String.valueOf(playerSelected.getCartonaseGalbene()));
            cartRosii.setText(String.valueOf(playerSelected.getCartonaseRosii()));
            passwordLabel.setText("Modifica parola:");
            playersDelete.setDisable(false);
            playersAddModify.setText("Modifica");
            playersAddModify.setOnAction(e -> modificaPlayer());
        } else {
            username.clear();
            nume.clear();
            nrTricou.clear();
            goluri.clear();
            paseDeGol.clear();
            cartGalbene.clear();
            cartRosii.clear();
            passwordLabel.setText("Parola:");
            playersDelete.setDisable(true);
            playersAddModify.setText("Adauga");
            playersAddModify.setOnAction(e -> addPlayer());
        }

        if (aparitieSelected != null) {
            aparitiiNume.setText(aparitieSelected.getNumeMeci());
            aparitiiMinute.setText(String.valueOf(aparitieSelected.getMinuteJucate()));
            aparitiiDelete.setDisable(false);
            aparitiiAddModify.setText("Modifica");
            aparitiiAddModify.setOnAction(e -> modificaAparitie());
        } else {
            aparitiiNume.setText("");
            aparitiiMinute.setText("");
            aparitiiDelete.setDisable(true);
            aparitiiAddModify.setText("Adauga");
            passwordField.clear();
            aparitiiAddModify.setOnAction(e -> addAparitie());
        }
    }

    private void backgroundUpdate() {
        TimerTask update = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        int selected = allPlayers.getSelectionModel().getSelectedIndex();
                        getAllPlayers();
                        allPlayers.getSelectionModel().select(selected);
                    } catch (NullPointerException ignored) {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        };
        refresh = new Timer();
        refresh.scheduleAtFixedRate(update, 2 * 1000, 2 * 1000);
    }
}

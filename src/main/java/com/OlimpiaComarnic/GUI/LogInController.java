package com.OlimpiaComarnic.GUI;

import com.OlimpiaComarnic.Backend.dao.PlayerDAO;
import com.OlimpiaComarnic.Backend.dao.UserDAO;
import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.Backend.entity.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

import java.util.concurrent.CompletableFuture;

public class LogInController {

    private final Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    volatile public static Player loggedIn;

    @FXML
    AnchorPane anchor;
    @FXML
    TextField userName;
    @FXML
    PasswordField password;
    @FXML
    Button logIn;
    @FXML
    Label errorText;

    @FXML
    public void initialize() {
        GUIRun.currStage.setMaximized(false);
        GUIRun.currStage.setResizable(false);
        GUIRun.currStage.setMinWidth(200);
        GUIRun.currStage.setMinHeight(200);
        GUIRun.currStage.setWidth(608);
        GUIRun.currStage.setHeight(298);

        GUIRun.currStage.setX((screenBounds.getWidth() - GUIRun.currStage.getWidth()) / 2);
        GUIRun.currStage.setY((screenBounds.getHeight() - GUIRun.currStage.getHeight()) / 2);

        // removes default focus on inputs
        Platform.runLater(() -> anchor.requestFocus());

    }

    public void checkUser() {

        String username = userName.getText();
        String pass = password.getText();

        errorText.setOpacity(0);

        if (username.equals("") && pass.equals("")) {
            errorText.setText("Username and password cannot be empty.");
            errorText.setOpacity(1.0);
            return;
        }
        else if (username.equals("")) {
            errorText.setText("Username cannot be empty.");
            errorText.setOpacity(1.0);
            return;
        } else if (pass.equals("")) {
            errorText.setText("Password cannot be empty.");
            errorText.setOpacity(1.0);
            return;
        }

        User curr = UserDAO.findUser(username);

        CompletableFuture getUser = CompletableFuture.runAsync(() -> {
            if (curr != null && !curr.isAdmin()) {
                loggedIn = PlayerDAO.findOneByUsername(username);
            }
        });

        if (curr != null) {
            if (curr.checkPassword(pass)) {
                try {
                    if (curr.isAdmin()) {
                        anchor.getScene().setRoot(FXMLLoader.load(GUIRun.class.getResource("adminWindow.fxml")));
                    } else {
                        if (!getUser.isDone()) {
                            getUser.get();
                        }
                        anchor.getScene().setRoot(FXMLLoader.load(GUIRun.class.getResource("userWindow.fxml")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                errorText.setText("Username or password is wrong.");
                errorText.setOpacity(1.0);
            }
        } else {
            errorText.setText("Username or password is wrong.");
            errorText.setOpacity(1.0);
        }
    }

}

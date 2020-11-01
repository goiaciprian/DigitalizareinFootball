package com.OlimpiaComarnic.GUI;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class LogInController {

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

        // removes default focus on inputs
        Platform.runLater( () -> anchor.requestFocus() );


        logIn.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                logIn.setStyle("-fx-background-color:#dae7f3;");
            }
        });

        logIn.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                logIn.setStyle("-fx-background-color:  #78D5D7;");
            }
        });

    }

    public void checkUser() {

        String username = userName.getText();
        String pass = password.getText();

        if (username.equals("dannyM")) {
            if (pass.equals("barosanuNr1")) {
                System.out.println("merge wei");
            } else {
                if (pass.equals("")) {
                    errorText.setText("Password cannot be blank.");
                } else {
                    errorText.setText("Username or password is wrong.");
                }
                errorText.setOpacity(1.0);
            }
        } else {
            if (username.equals("")) {
                errorText.setText("Username cannot be blank.");
            } else {
                if (pass.equals("")) {
                    errorText.setText("Password cannot be blank.");
                } else {
                    errorText.setText("Username or password is wrong.");
                }
            }
            errorText.setOpacity(1.0);
        }


    }

}

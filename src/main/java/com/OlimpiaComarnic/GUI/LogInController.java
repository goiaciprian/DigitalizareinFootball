package com.OlimpiaComarnic.GUI;

import com.OlimpiaComarnic.Backend.dao.UserDAO;
import com.OlimpiaComarnic.Backend.entity.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

        logIn.setOnMouseEntered(t -> logIn.setStyle("-fx-background-color:#dae7f3;"));
        logIn.setOnMouseExited(t -> logIn.setStyle("-fx-background-color:  #78D5D7;"));

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
        else if(username.equals("")) {
            errorText.setText("Username cannot be empty.");
            errorText.setOpacity(1.0);
            return;
        }
        else if(pass.equals("")) {
            errorText.setText("Password cannot be empty.");
            errorText.setOpacity(1.0);
            return;
        }

        User curr = UserDAO.findUser(username);
//        User curr = new User("admin", "admin", true);

        if(curr == null)
            return;

        if (!curr.getUsername().equals("null")) {
            if (curr.checkPassword(pass)) {
                try {
                    System.out.println("aici");
                    anchor.getScene().setRoot(FXMLLoader.load(GUIRun.class.getResource("adminWindow.fxml")));
                }
                catch (Exception ignored) {
//                    System.out.println(e);
                }
//                finally { System.out.println("done"); }
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

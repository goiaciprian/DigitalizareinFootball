module com.OlimpiaComarnic.GUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;
    requires jbcrypt;

    opens com.OlimpiaComarnic.GUI to javafx.fxml;
    exports com.OlimpiaComarnic.GUI;
}
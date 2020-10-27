module com.OlimpiaComarnic.GUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;

    opens com.OlimpiaComarnic.GUI to javafx.fxml;
    exports com.OlimpiaComarnic.GUI;
}
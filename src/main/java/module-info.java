module com.OlimpiaComarnic.GUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;
    requires jbcrypt;

    opens com.OlimpiaComarnic.GUI to javafx.fxml;
    opens com.OlimpiaComarnic.GUI.Popup to javafx.fxml, javafx.graphics;
    exports com.OlimpiaComarnic.GUI;
    exports com.OlimpiaComarnic.GUI.Popup;
}
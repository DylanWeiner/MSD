module com.example.synthesizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;

    opens com.example.synthesizer to javafx.fxml;
    exports com.example.synthesizer;
}
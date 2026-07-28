module com.example.hellothreads {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hellothreads to javafx.fxml;
    exports com.example.hellothreads;
}
module com.example.threadinclass {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.threadinclass to javafx.fxml;
    exports com.example.threadinclass;
}
module com.example.webchat {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.webchat to javafx.fxml;
    exports com.example.webchat;
}
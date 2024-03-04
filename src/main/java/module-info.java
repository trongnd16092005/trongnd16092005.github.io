module com.example.multiclock {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.multiclock to javafx.fxml;
    exports com.example.multiclock;
}
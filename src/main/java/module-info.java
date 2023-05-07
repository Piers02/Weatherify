module com.example.weatherify {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.weatherify to javafx.fxml;
    exports com.example.weatherify;
}
module com.example.weatherify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    requires java.sql;
    requires org.slf4j;
    requires com.zaxxer.hikari;

    opens com.example.weatherify to javafx.fxml;
    exports com.example.weatherify;
    exports com.example.weatherify.APIFields;
    opens com.example.weatherify.APIFields to javafx.fxml;
}
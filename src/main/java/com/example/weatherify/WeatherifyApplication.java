package com.example.weatherify;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class WeatherifyApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Weatherify.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Weatherify");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("weather-icon-png-5.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

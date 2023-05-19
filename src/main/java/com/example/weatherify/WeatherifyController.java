package com.example.weatherify;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.databind.util.JSONPObject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

// Used for getWeather()
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.io.IOException;
import java.util.List;

// USed for JSON parsing
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class WeatherifyController {
    @FXML public TextField tfSearch;
    @FXML private Label avgTemperatureLabel;
    @FXML public Label MaxTemperatureLabel;
    @FXML public Label MinTemperatureLabel;
    @FXML public Label humidityLabel;
    @FXML public Label popLabel;
    @FXML public Label windSpeedLabel;


    public void onQuitClicked() {
        System.exit(0);
    }

    public void onAboutClicked() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Weatherify Application");
        alert.setHeaderText("Realized for OOP final project\n" + "UniMore - Nicola Bicocchi");
        alert.setContentText("""
                Author: Piergiorgio Signorino
                Matr: 164822
                API Used: TimeLineWeather""");
        alert.showAndWait();
    }

    // To do
    public void onTemperatureClicked() {
        System.exit(0);
    }

    // To do
    public void onPrecipitationClicked() {
        System.exit(0);
    }

    // Send current location to a method that searches for current weather
    public void onSearchClicked() {
        getWeather(tfSearch.getText());
    }

    public void getWeather(String city){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=51bc2219822a99c72600d6f370951801&lang=eng&units=metric"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        // Debug purposes
        System.out.println(response.body());

        // Works with rain/snow
        String to_replace = response.body();
        StringBuilder stringBuilder = new StringBuilder(to_replace);
        if(response.body().contains("rain") || response.body().contains("snow")) {
            stringBuilder.insert(stringBuilder.indexOf("1h"), '_');
        }

        ObjectMapper tmpobject = new ObjectMapper();
        try {
            JsonResponse jsonResponse2 = tmpobject.readValue(stringBuilder.toString(), JsonResponse.class);
            System.out.println("Stop!");

            int tmp = jsonResponse2.getMain().getTemp();
            avgTemperatureLabel.setText(Integer.toString(tmp));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }



        // Does not work with rain/snow
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonResponse jsonResponse = objectMapper.readValue(new URL ("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=51bc2219822a99c72600d6f370951801&lang=eng&units=metric"), JsonResponse.class);
            System.out.println("Stop!");
            int temp = jsonResponse.getMain().getTemp();
            avgTemperatureLabel.setText("" + temp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

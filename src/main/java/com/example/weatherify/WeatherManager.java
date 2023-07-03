package com.example.weatherify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class WeatherManager {

    public String getWeatherForecast(String city) {

        // Get API response from city location
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=51bc2219822a99c72600d6f370951801&lang=eng&units=metric"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if(Objects.requireNonNull(response).body().contains("{\"cod\":\"404\",\"message\":\"city not found\"}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Response error");
            alert.setHeaderText("City not found or not existent");
            alert.setContentText("Try again with a different city name");
            alert.showAndWait();
            return null;
        }

        // Works with rain/snow
        String to_replace = Objects.requireNonNull(response).body();
        StringBuilder stringBuilder = new StringBuilder(to_replace);
        if (response.body().contains("rain") || response.body().contains("snow")) {
            stringBuilder.insert(stringBuilder.indexOf("1h"), '_');
        }

        return stringBuilder.toString();
    }

}

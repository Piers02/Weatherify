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
import java.util.Objects;

// USed for JSON parsing
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WeatherifyController {
    @FXML public TextField tfSearch;
    @FXML private Label avgTemperatureLabel;
    @FXML public Label MaxTemperatureLabel;
    @FXML public Label MinTemperatureLabel;
    @FXML public Label humidityLabel;
    @FXML public Label AmountOfRainLabel;
    @FXML public Label windSpeedLabel;
    @FXML private ImageView weatherIcon;


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
                API Used: OpenWeatherMap""");
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

        // In order to accommodate with city names that are composed by multiple string
        // We replace the spaces with the symbol "+", in this way the API can respond correctly
        getWeatherForecast(tfSearch.getText().replace(" ", "+"));
    }

    public void getWeatherForecast(String city){

        // Get API response from city location
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

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonResponse jsonResponse = objectMapper.readValue(stringBuilder.toString(), JsonResponse.class);

            avgTemperatureLabel.setText(jsonResponse.getMain().getTemp() + "°");
            MaxTemperatureLabel.setText(jsonResponse.getMain().getTemp_max() + "°");
            MinTemperatureLabel.setText(jsonResponse.getMain().getTemp_min() + "°");
            if(jsonResponse.getRain() == null) {
                AmountOfRainLabel.setText(0 + " mm");
            }
            else {
                AmountOfRainLabel.setText(jsonResponse.getRain().get_1h() + " mm");
            }
            humidityLabel.setText(jsonResponse.getMain().getHumidity() + "%");
            windSpeedLabel.setText(jsonResponse.getWind().getSpeed() + " km/h");

            //Working
            weatherIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("WeatherIcons/" + jsonResponse.getWeather()[0].getIcon() + ".png"))));


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

}

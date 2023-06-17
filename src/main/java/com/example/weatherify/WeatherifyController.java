package com.example.weatherify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class WeatherifyController {
    @FXML public TextField tfSearch;
    @FXML public Label CityLabel;
    @FXML public Label SunriseLabel;
    @FXML public Label SunsetLabel;
    @FXML public Label currentTimeLabel;
    @FXML private Label avgTemperatureLabel;
    @FXML public Label MaxTemperatureLabel;
    @FXML public Label MinTemperatureLabel;
    @FXML public Label humidityLabel;
    @FXML public Label AmountOfRainLabel;
    @FXML public Label AmountOfSnowLabel;
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

        // In order to accommodate with city names that are composed by multiple strings
        // We replace the spaces with the symbol '+', in this way the API can respond correctly
        WeatherManager weatherManager = new WeatherManager();
        ShowWeather(weatherManager.getWeatherForecast(tfSearch.getText().replace(" ", "+")));
    }

    public void ShowWeather (String response){

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonResponse jsonResponse = objectMapper.readValue(response, JsonResponse.class);

            CityLabel.setText(jsonResponse.getName() + ", " + jsonResponse.getSys().getCountry());
            avgTemperatureLabel.setText(jsonResponse.getMain().getTemp() + "°");
            MaxTemperatureLabel.setText(jsonResponse.getMain().getTemp_max() + "°");
            MinTemperatureLabel.setText(jsonResponse.getMain().getTemp_min() + "°");
            humidityLabel.setText(jsonResponse.getMain().getHumidity() + "%");
            windSpeedLabel.setText(jsonResponse.getWind().getSpeed() + " m/s");

            if(jsonResponse.getRain() == null) {
                AmountOfRainLabel.setText(0 + " mm");
            }
            else {
                AmountOfRainLabel.setText(jsonResponse.getRain().get_1h() + " mm");
            }

            if (jsonResponse.getSnow() == null) {
                AmountOfSnowLabel.setText(0 + " mm");
            } else {
                AmountOfSnowLabel.setText(jsonResponse.getSnow().get_1h() + " mm");
            }

            Date date = new Date(jsonResponse.getSys().getSunrise() * 1000L);
            SimpleDateFormat jdf = new SimpleDateFormat("HH:mm");
            int tmp = jsonResponse.getTimezone() / 3600;
            if(tmp > 0) {
                jdf.setTimeZone(TimeZone.getTimeZone("GMT+" + (jsonResponse.getTimezone() / 3600)));
            }
            else {
                jdf.setTimeZone(TimeZone.getTimeZone("GMT" + (jsonResponse.getTimezone() / 3600)));
            }
            String java_date = jdf.format(date);
            SunriseLabel.setText(java_date);

            date = new Date(jsonResponse.getSys().getSunset() * 1000L);
            java_date = jdf.format(date);
            SunsetLabel.setText(java_date);

            date = new Date(jsonResponse.getDt() * 1000L);
            jdf = new SimpleDateFormat("dd/MM/yyyy | 'Local time:' HH:mm  z");
            tmp = jsonResponse.getTimezone() / 3600;
            if(tmp > 0) {
                jdf.setTimeZone(TimeZone.getTimeZone("GMT+" + (jsonResponse.getTimezone() / 3600)));
            }
            else {
                jdf.setTimeZone(TimeZone.getTimeZone("GMT" + (jsonResponse.getTimezone() / 3600)));
            }
            java_date = jdf.format(date);
            currentTimeLabel.setText(java_date);



            weatherIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("WeatherIcons/" + jsonResponse.getWeather()[0].getIcon() + ".png"))));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

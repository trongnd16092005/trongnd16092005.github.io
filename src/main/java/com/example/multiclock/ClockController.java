package com.example.multiclock;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class ClockController implements Initializable {

    @FXML
    Label clockLabel;
    @FXML
    Button openBut;
    @FXML
    Text clockText;
    @FXML
    TextField GMTText;
    @FXML
    Label Clock2Label;
    private volatile boolean runningClock1 = true;
    private volatile boolean runningClock2 = true;



    private Thread clockCurrent;
    private Thread clockGMT;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        int timeZoneOffset = defaultTimeZone.getRawOffset() / 3600000;
        clock(timeZoneOffset);
    }
    public void clock(int timeZoneOffset){
         clockCurrent = new Thread(() -> {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            while (runningClock1) {
                ZoneOffset zoneOffset=ZoneOffset.ofHours(timeZoneOffset);
                LocalTime currentTime = LocalTime.now(zoneOffset);
                String formattedTime = currentTime.format(dateTimeFormatter);
                Platform.runLater(() -> {
                    clockLabel.setText(formattedTime);
                });
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        clockCurrent.start();
    }
    @FXML
    public void handleCloseButtonAction() {
        runningClock1=false;
        runningClock2=false;
        Platform.exit();
    }
    public void openNewStage(ActionEvent event) {
        Stage stage2 = new Stage();
        stage2.setTitle("Stage 2");
        Label clock = new Label();
        int timeZoneOffset= Integer.parseInt(GMTText.getText());
        if(timeZoneOffset<-12 || timeZoneOffset>12){
            clockText.setText("Please try again!");
        }else{
        clockGMT = new Thread(() -> {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            while (runningClock2) {
                ZoneOffset zoneOffset=ZoneOffset.ofHours(Integer.parseInt(GMTText.getText()));
                LocalTime currentTime = LocalTime.now(zoneOffset);
                String formattedTime = currentTime.format(dateTimeFormatter);
                Platform.runLater(() -> {
                    clock.setText(formattedTime);
                });
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        clockGMT.start();
        }
        StackPane root = new StackPane(clock);
        stage2.setScene(new Scene(root,200,200));
        stage2.show();
    }
}
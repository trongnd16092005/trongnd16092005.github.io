package com.example.multiclock;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class ClockController implements Initializable {

    @FXML
    private Label clockLabel;
    @FXML
    Button openBut;

    @FXML
    Text clockText;

    @FXML
    TextField GMTText;
    private volatile boolean runningClock1 = true;
    private volatile boolean runningClock2 = true;
    boolean status1=true;
    boolean status2=false;



    private Thread clockCurrent;
    private Thread clockGMT;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        int timeZoneOffset = defaultTimeZone.getRawOffset() / 3600000;
        clock(timeZoneOffset);
    }
    public void clock(int timeZoneOffset){
         status1=true;
         status2=false;
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
    public boolean clock2(int timeZoneOffset){
        status2=true;
        status1=false;
        clockGMT = new Thread(() -> {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            while (runningClock2) {
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
        clockGMT.start();
        return status2;
    }
    @FXML
    public void handleCloseButtonAction() {
        runningClock1=false;
        runningClock2=false;
        Platform.exit();
    }
    public void setUpClock(){
        int timeZoneOffset= Integer.parseInt(GMTText.getText());
        if(timeZoneOffset<-12 || timeZoneOffset>12){
            clockText.setText("Please try again!");
        }else {
            if (!status1 && status2) {
                runningClock2 = false;
                runningClock1 = true;
                clock(timeZoneOffset);
            } else {
                runningClock1 = false;
                runningClock2 = true;
                clock2(timeZoneOffset);
            }

        }
    }
}
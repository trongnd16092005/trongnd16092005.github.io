package com.example.multiclock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("clock.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 320);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        ClockController clockController=fxmlLoader.getController();
        stage.setOnCloseRequest(event -> clockController.handleCloseButtonAction());
    }

    public static void main(String[] args) {
        launch();
    }
}
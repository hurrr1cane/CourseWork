package com.mhorak.coursework;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PatientsApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PatientsApp.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 660);
        stage.setTitle("Patients");
        stage.setScene(scene);

        // Set minimum width and height
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
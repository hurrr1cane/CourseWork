package com.mhorak.coursework;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class PatientsApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PatientsApp.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 660);
        stage.setTitle("Patients");

        // Load the custom icon image
        //Image icon = new Image("/com/mhorak/coursework/patient.png");

        stage.setScene(scene);
        //stage.getIcons().add(icon);

        // Set minimum width and height
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
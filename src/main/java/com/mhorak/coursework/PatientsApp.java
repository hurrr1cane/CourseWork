package com.mhorak.coursework;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for the application
 */
public class PatientsApp extends Application {
    /**
     * Starts the application
     *
     * @param stage The stage
     * @throws IOException If the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PatientsApp.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 660);
        stage.setTitle("Patients");

        // Load the custom icon image
        Image icon = new Image("patient.png");

        stage.setScene(scene);
        stage.getIcons().add(icon);

        // Set minimum width and height
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.show();
    }

    /**
     * Main method
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}
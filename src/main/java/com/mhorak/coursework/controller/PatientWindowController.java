package com.mhorak.coursework.controller;

import com.mhorak.coursework.model.Patient;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PatientWindowController {
    @FXML
    private VBox rootVBox; // Assuming you have this reference in your controller

    public void initialize() {
        // Set padding programmatically
        rootVBox.setPadding(new Insets(10));
    }

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    // Add more fields for other patient attributes

    private Patient patient;


    public void setPatient(Patient patient) {
        this.patient = patient;
        // Update form fields with patient data
    }

    @FXML
    private void handleSave() {
        // Save the form data to the patient object
        // You may want to validate the data before saving

        // Close the window
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        // Close the window without saving
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }
}

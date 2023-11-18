package com.mhorak.coursework.controller;

import com.mhorak.coursework.model.Patient;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import com.mhorak.coursework.exception.*;

import static com.mhorak.coursework.tool.PatientsTool.*;

/**
 * Controller class for the patient window
 */
public class PatientWindowController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField yearField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField temperatureField;

    @FXML
    private TextField hemoglobinField;

    private Patient patient;

    @FXML
    private VBox rootVBox;

    /**
     * Initializes the controller class
     */
    public void initialize() {
        rootVBox.setPadding(new Insets(10));
    }

    /**
     * Sets the patient object
     *
     * @param patient      The patient object
     * @param isNewPatient True if the patient is new, false if the patient is being edited
     */
    public void setPatient(Patient patient, boolean isNewPatient) {
        this.patient = patient;
        // Update form fields with patient data
        if (!isNewPatient) {
            setPatientFields();
        }
    }

    /**
     * Sets the patient object for editing
     */
    private void setPatientFields() {
        if (patient != null) {
            // Set form fields with patient data
            firstNameField.setText(patient.getFirstName());
            lastNameField.setText(patient.getLastName());
            yearField.setText(String.valueOf(patient.getYearOfBirth()));
            genderComboBox.setValue(patient.getGender());
            temperatureField.setText(String.valueOf(patient.getT()));
            hemoglobinField.setText(String.valueOf(patient.getHemoglobin()));
        }
    }

    /**
     * Saves the patient data
     */
    @FXML
    private void handleSave() {
        // Validate user input before creating a new Patient object
        if (validateInput()) {
            // If input is valid, create a new Patient object
            createPatientObject();

            closeWindow();
        }
    }

    /**
     * Validates the user input
     *
     * @return True if the input is valid, false otherwise
     */
    private boolean validateInput() {
        try {
            validateNameField(firstNameField.getText(), true);
            validateNameField(lastNameField.getText(), false);
            validateYearOfBirthField(yearField.getText());
            validateGenderField(genderComboBox.getValue());
            validateTemperatureField(temperatureField.getText());
            validateHemoglobinField(hemoglobinField.getText());

            // If all validations pass, return true
            return true;
        } catch (InputFieldException e) {
            // Handle the exception (you may choose to display an error message)
            System.err.println("Validation Error: " + e.getMessage());
            showErrorAlert(e.getMessage());
            return false;
        }
    }

    /**
     * Shows an error alert
     *
     * @param message The message to display
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Creates a new Patient object with the entered data
     */
    private void createPatientObject() {
        // Create a new Patient object with the entered data
        if (patient == null) {
            patient = new Patient();
        }

        patient.setFirstName(firstNameField.getText());
        patient.setLastName(lastNameField.getText());
        patient.setYearOfBirth(Integer.parseInt(yearField.getText()));
        patient.setHemoglobin(Double.parseDouble(hemoglobinField.getText()));
        patient.setT(Double.parseDouble(temperatureField.getText()));
        patient.setGender(genderComboBox.getValue());
    }

    /**
     * Closes the window
     */
    @FXML
    private void handleCancel() {
        // Close the window without saving
        closeWindow();
    }

    /**
     * Helper method to close the window
     */
    private void closeWindow() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }
}

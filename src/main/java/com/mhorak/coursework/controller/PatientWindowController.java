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
    public void initialize() {
        rootVBox.setPadding(new Insets(10));
    }

    public void setPatient(Patient patient, boolean isNewPatient) {
        this.patient = patient;
        // Update form fields with patient data
        if (!isNewPatient) {
            setPatientFields();
        }
    }

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

    @FXML
    private void handleSave() {
        // Validate user input before creating a new Patient object
        if (validateInput()) {
            // If input is valid, create a new Patient object
            createPatientObject();

            closeWindow();
        }
        // If input is not valid, you may choose to display an error message to the user
    }

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

    private void validateNameField(String name, boolean isName) throws NameFieldException, SurnameFieldException {
        // Name should contain only letters and hyphens, not starting or ending with hyphen

        if (isName) {
            if (name == null) {
                throw new NameFieldException();
            }
            if (!name.matches("^[a-zA-Z]+(-[a-zA-Z]+)?$")) {
                throw new NameFieldException();
            }
        } else {
            if (name == null) {
                throw new SurnameFieldException();
            }
            if (!name.matches("^[a-zA-Z]+(-[a-zA-Z]+)*$")) {
                throw new SurnameFieldException();
            }
        }
    }

    private void validateYearOfBirthField(String year) throws YearOfBirthFieldException {
        // Year of birth should be a positive integer
        if (!year.matches("^[1-9]\\d*$")) {
            throw new YearOfBirthFieldException();
        }
    }

    // New validation method for the gender field
    private void validateGenderField(String gender) throws GenderFieldException {
        // Gender should not be null or empty
        if (gender == null || gender.trim().isEmpty()) {
            throw new GenderFieldException();
        }
    }

    private void validateTemperatureField(String temperature) throws TemperatureFieldException {
        // Temperature should be a positive decimal number
        if (!temperature.matches("^\\d*\\.?\\d+$")) {
            throw new TemperatureFieldException();
        }
    }

    private void validateHemoglobinField(String hemoglobin) throws HemoglobinFieldException {
        // Hemoglobin should be a positive decimal number
        if (!hemoglobin.matches("^\\d*\\.?\\d+$")) {
            throw new HemoglobinFieldException();
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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

        // Note: It's essential to handle exceptions, such as NumberFormatException when parsing integers
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

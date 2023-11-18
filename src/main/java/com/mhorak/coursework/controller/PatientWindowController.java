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

import java.time.Year;

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
     * Validates the name and surname fields
     *
     * @param name   The name or surname to validate
     * @param isName True if the field is the name field, false if the field is the surname field
     * @throws NameFieldException    Thrown when the name field is incorrect
     * @throws SurnameFieldException Thrown when the surname field is incorrect
     */
    private void validateNameField(String name, boolean isName) throws NameFieldException, SurnameFieldException {
        // Name should contain only letters and hyphens, not starting or ending with hyphen

        //Name should contain only letters and hyphens, not starting or ending with hyphen and not containing two consecutive hyphens
        //And be at least 2 characters long and not longer than 30 characters
        boolean checker = !name.matches("^[a-zA-Z]+(-[a-zA-Z]+)*$") || name.length() < 2 || name.length() > 30;
        if (isName) {
            if (checker) {
                throw new NameFieldException();
            }
        } else {
            if (checker) {
                throw new SurnameFieldException();
            }
        }
    }

    /**
     * Validates the year of birth field
     *
     * @param year The year of birth to validate
     * @throws YearOfBirthFieldException Thrown when the year of birth field is incorrect
     */
    private void validateYearOfBirthField(String year) throws YearOfBirthFieldException {
        // Year of birth should be a positive integer
        if (!year.matches("^[1-9]\\d*$") || Year.now().getValue() - Integer.parseInt(year) < 0) {
            throw new YearOfBirthFieldException();
        }
    }

    /**
     * Validates the gender field
     *
     * @param gender The field to be validated
     * @throws GenderFieldException Thrown when gender field is incorrect
     */
    private void validateGenderField(String gender) throws GenderFieldException {
        // Gender should not be null or empty
        if (gender == null || gender.trim().isEmpty()) {
            throw new GenderFieldException();
        }
    }

    /**
     * Validates the temperature field
     *
     * @param temperature The temperature to validate
     * @throws TemperatureFieldException Thrown when the temperature field is incorrect
     */
    private void validateTemperatureField(String temperature) throws TemperatureFieldException {
        // Temperature should be a positive decimal number
        // Temperature must be not higher than 50 degrees Celsius
        if (!temperature.matches("^\\d*\\.?\\d+$") || Double.parseDouble(temperature) > 50) {
            throw new TemperatureFieldException();
        }
    }

    /**
     * Validates the hemoglobin field
     *
     * @param hemoglobin The hemoglobin to validate
     * @throws HemoglobinFieldException Thrown when the hemoglobin field is incorrect
     */
    private void validateHemoglobinField(String hemoglobin) throws HemoglobinFieldException {
        // Hemoglobin should be a positive decimal number
        // Must be not higher than 250 g/L
        if (!hemoglobin.matches("^\\d*\\.?\\d+$") || Double.parseDouble(hemoglobin) > 250) {
            throw new HemoglobinFieldException();
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

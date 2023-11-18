package com.mhorak.coursework.controller;

import com.mhorak.coursework.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mhorak.coursework.tool.PatientsTool.loadPatientsFromCsv;
import static com.mhorak.coursework.tool.PatientsTool.savePatientsToCsv;

public class MainWindowController {
    @FXML
    private TableView<Patient> tableView; // This connects to your TableView in the FXML
    private File openedFile;
    ObservableList<Patient> patientList = FXCollections.observableArrayList();
    ObservableList<Patient> visiblePatientList = FXCollections.observableArrayList();

    LinkedList<ObservableList<Patient>> patientListHistory = new LinkedList<>();
    int historyIndex = -1;

    public void initialize() {

        // Set the TableView items
        tableView.setItems(visiblePatientList);

        updateHistoryList();

    }

    @FXML
    private void handleSortByTButton(ActionEvent event) {
        // Create a copy of the original patientList
        ObservableList<Patient> sortedList = FXCollections.observableArrayList(patientList);

        // Multiply t by 100 to convert it to an integer for counting sort
        for (Patient patient : sortedList) {
            patient.setT(patient.getT() * 100);
        }

        // Separate the patients by gender in the sorted list
        ObservableList<Patient> males = FXCollections.observableArrayList();
        ObservableList<Patient> females = FXCollections.observableArrayList();

        for (Patient patient : sortedList) {
            if (patient.isMale()) {
                males.add(patient);
            } else {
                females.add(patient);
            }
        }

        // Perform counting sort for males
        countingSortByT(males);

        // Perform counting sort for females
        countingSortByT(females);

        // Clear the original patientList and add the sorted patients
        visiblePatientList.clear();
        visiblePatientList.addAll(males);
        visiblePatientList.addAll(females);

        // Update the TableView to reflect the sorted list
        tableView.setItems(visiblePatientList);

        // Reset the original t values by dividing them by 100
        for (Patient patient : visiblePatientList) {
            patient.setT(patient.getT() / 100);
        }
    }


    // Counting Sort method for sorting patients by t (ascending order)
    private void countingSortByT(ObservableList<Patient> patients) {
        int maxT = getMaxT(patients);

        int[] count = new int[maxT + 1];
        Patient[] output = new Patient[patients.size()];

        for (Patient patient : patients) {
            count[(int) patient.getT()]++;
        }

        for (int i = 1; i <= maxT; i++) {
            count[i] += count[i - 1];
        }

        for (int i = patients.size() - 1; i >= 0; i--) {
            int t = (int) patients.get(i).getT();
            output[count[t] - 1] = patients.get(i);
            count[t]--;
        }

        patients.clear();
        patients.addAll(output);
    }

    // Helper method to find the maximum t value
    private int getMaxT(ObservableList<Patient> patients) {
        int maxT = -1;

        for (Patient patient : patients) {
            int t = (int) patient.getT();
            if (t > maxT) {
                maxT = t;
            }
        }

        return maxT;
    }


    @FXML
    private void handleHighHemoglobinInWomenButton(ActionEvent event) {
        // Filter the patientList to include only females with elevated hemoglobin
        ObservableList<Patient> filteredList = patientList.filtered(
                patient -> patient.isFemale() && patient.isHighHemoglobin());

        // Clear the original patientList and add the filtered patients
        visiblePatientList.clear();
        visiblePatientList.addAll(filteredList);

        // This line will refresh the table to apply the custom cell factory
        tableView.refresh();
    }

    @FXML
    private void handleYoungestMenWithTHemoglobin(ActionEvent event) {
        // Filter the patientList to include only youngest males with normal T and low hemoglobin
        ObservableList<Patient> filteredList = patientList.filtered(
                patient -> patient.isMale() && patient.isNormalT() && patient.isLowHemoglobin() && isYoungestMale(patient));

        // Clear the original patientList and add the filtered patients
        visiblePatientList.clear();
        visiblePatientList.addAll(filteredList);

        // This line will refresh the table to apply the updated data
        tableView.refresh();
    }

    // Helper method to check if a patient is the youngest male with normal T and low hemoglobin
    private boolean isYoungestMale(Patient currentPatient) {
        for (Patient patient : tableView.getItems()) {
            if (patient.isMale() && patient.isNormalT() && patient.isLowHemoglobin()) {
                // Compare ages
                if (currentPatient.getAge() > patient.getAge()) {
                    return false;
                }
            }
        }
        return true;
    }

    @FXML
    private void handleNewPatient(ActionEvent event) {
        // Create a new patient
        Patient newPatient = new Patient();

        // Load and show the patient form for adding a new patient
        loadPatientForm(newPatient, true);


        if (!newPatient.isEmpty()) {
            // Add the new patient to the list
            newPatient.setNumber(patientList.size() + 1);
            patientList.add(newPatient);
            visiblePatientList.clear();
            visiblePatientList.addAll(patientList);
            tableView.refresh();

            updateHistoryList();
        }

    }

    @FXML
    private void handleEditPatient(ActionEvent event) {
        // Get the selected patient
        Patient selectedPatient = tableView.getSelectionModel().getSelectedItem();

        if (selectedPatient != null) {
            // Load and show the patient form for editing the selected patient
            loadPatientForm(selectedPatient, false);

            // Add the new patient to the list
            patientList.set(selectedPatient.getNumber() - 1, selectedPatient);
            visiblePatientList.clear();
            visiblePatientList.addAll(patientList);
            tableView.refresh();

            updateHistoryList();
        }
    }

    private void loadPatientForm(Patient patient, boolean isNewPatient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mhorak/coursework/patient-window.fxml"));
            Parent root = loader.load();

            PatientWindowController controller = loader.getController();
            controller.setPatient(patient, isNewPatient);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Patient Form");

            // Use showAndWait to make the window modal
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Patients CSV File");

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            loadPatientsFromCsv(file, patientList);

            // Set the opened file
            openedFile = file;
        }

        visiblePatientList.clear();
        visiblePatientList.addAll(patientList);
        tableView.refresh();

        updateHistoryList();
    }


    @FXML
    void handleSaveAs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Patients CSV File");

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            savePatientsToCsv(file, patientList);

            // Set the opened file to the saved file
            openedFile = file;
        }
    }

    @FXML
    void handleSave(ActionEvent event) {
        // Check if a file is opened
        if (openedFile != null) {
            savePatientsToCsv(openedFile, patientList);
        } else {
            // If no file is opened, prompt the user to choose a file
            handleSaveAs(event);
        }
    }

    @FXML
    void handleExit(ActionEvent event) {
        // Get the current stage
        Stage stage = (Stage) tableView.getScene().getWindow();

        // Close the stage
        stage.close();
    }

    @FXML
    void handleHelp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Help");
        alert.setContentText("For help, please contact the developer at maksym.horak.pz.2022@lpnu.ua");
        alert.show();
    }

    @FXML
    private void handleOlderHighHemoglobinLowT(ActionEvent event) {
        // Filter the patientList to include only patients older than 40 with high hemoglobin and low T
        ObservableList<Patient> filteredList = patientList.filtered(
                patient -> patient.getAge() > 40 && patient.isHighHemoglobin() && patient.isLowT());

        // Clear the original patientList and add the filtered patients
        visiblePatientList.clear();
        visiblePatientList.addAll(filteredList);

        // This line will refresh the table to apply the updated data
        tableView.refresh();
    }

    @FXML
    private void handleOldestWomenSameNameNormalHemoglobin(ActionEvent event) {
        // Filter the patientList to include only women with normal hemoglobin
        ObservableList<Patient> filteredList = patientList.filtered(
                patient -> patient.isFemale() && patient.isNormalHemoglobin());

        // Create a map to store the oldest patient for each name
        Map<String, Patient> oldestPatientsByName = new HashMap<>();

        // Iterate through the filtered list to find the oldest patient with the same name
        for (Patient patient : filteredList) {
            String firstName = patient.getFirstName();
            if (!oldestPatientsByName.containsKey(firstName) ||
                    oldestPatientsByName.get(firstName).getAge() < patient.getAge()) {
                oldestPatientsByName.put(firstName, patient);
            }
        }

        // Clear the original patientList and add the oldest patients with the same name
        visiblePatientList.clear();
        visiblePatientList.addAll(oldestPatientsByName.values());

        // This line will refresh the table to apply the updated data
        tableView.refresh();
    }


    @FXML
    private void handleEncourageYoungPatients(ActionEvent event) {
        // Explicitly specify the column type for "Повідомлення"
        TableColumn<Patient, String> messageColumn = (TableColumn<Patient, String>) tableView.getColumns().get(7); // Assuming "Повідомлення" is the 8th column

        handleBack(event);

        messageColumn.setCellFactory(new Callback<TableColumn<Patient, String>, TableCell<Patient, String>>() {
            @Override
            public TableCell<Patient, String> call(TableColumn<Patient, String> param) {
                return new TableCell<Patient, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            // Get the patient for this row
                            Patient currentPatient = getTableView().getItems().get(getIndex());

                            // Update the message field for matching patients
                            if (currentPatient.isNormalT() && currentPatient.getAge() < 28) {
                                setText(String.format("%s, you are doing great!", currentPatient.getLastName()));
                            } else {
                                setText("");
                            }
                        }
                    }
                };
            }
        });

        // This line will refresh the table to apply the custom cell factory
        tableView.refresh();
    }

    @FXML
    void handleDeletePatient(ActionEvent event) {
        // Get the selected patient
        Patient selectedPatient = tableView.getSelectionModel().getSelectedItem();

        // Remove the selected patient from the list
        patientList.remove(selectedPatient);

        setPatientsNumbers(patientList);

        // Update the TableView to reflect the new patient
        tableView.setItems(patientList);

        updateHistoryList();
    }

    private void updateHistoryList() {
        // Add the current patientList to the history
        patientListHistory.removeAll(patientListHistory.subList(historyIndex + 1, patientListHistory.size()));
        patientListHistory.add(FXCollections.observableList(List.copyOf(patientList)));
        historyIndex++;
    }

    private void setPatientsNumbers(ObservableList<Patient> patientList) {
        for (int i = 0; i < patientList.size(); i++) {
            patientList.get(i).setNumber(i + 1);
        }
    }

    @FXML
    void handleBack(ActionEvent event) {

        visiblePatientList.clear();
        visiblePatientList.addAll(patientList);
        tableView.refresh();

        TableColumn<Patient, String> messageColumn = (TableColumn<Patient, String>) tableView.getColumns().get(7); // Assuming "Повідомлення" is the 8th column
        messageColumn.setCellFactory(new Callback<TableColumn<Patient, String>, TableCell<Patient, String>>() {
            @Override
            public TableCell<Patient, String> call(TableColumn<Patient, String> param) {
                return new TableCell<Patient, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                            setText(null);

                    }
                };
            }
        });
    }

    @FXML
    void handleUndo(ActionEvent event) {
        if (historyIndex > 0) {
            historyIndex--;
            patientList.clear();
            patientList.addAll(patientListHistory.get(historyIndex));
            visiblePatientList.clear();
            visiblePatientList.addAll(patientList);
            tableView.refresh();
        }
    }

    @FXML
    void handleRedo(ActionEvent event) {
        if (historyIndex < patientListHistory.size() - 1) {
            historyIndex++;
            patientList.clear();
            patientList.addAll(patientListHistory.get(historyIndex));
            visiblePatientList.clear();
            visiblePatientList.addAll(patientList);
            tableView.refresh();
        }
    }

}

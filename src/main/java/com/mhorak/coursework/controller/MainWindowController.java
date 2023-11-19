package com.mhorak.coursework.controller;

import com.mhorak.coursework.exception.FileReadException;
import com.mhorak.coursework.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.mhorak.coursework.tool.PatientsTool.*;

/**
 * This class is the controller for the main window.
 * It handles all the events from the main window.
 */
public class MainWindowController {
    @FXML
    private TableView<Patient> tableView; // This connects to your TableView in the FXML
    private File openedFile;
    ObservableList<Patient> patientList = FXCollections.observableArrayList();
    ObservableList<Patient> visiblePatientList = FXCollections.observableArrayList();
    LinkedList<ObservableList<Patient>> patientListHistory = new LinkedList<>();
    int historyIndex = -1;

    /**
     * This method is called when the controller is initialized.
     * It is used to set the TableView items.
     */
    public void initialize() {

        // Set the TableView items
        tableView.setItems(visiblePatientList);


        // Create a ContextMenu
        ContextMenu contextMenu = new ContextMenu();

        // Create MenuItems for the ContextMenu
        MenuItem menuItemAddPatient = new MenuItem("Add patient");
        MenuItem menuItemUndo = new MenuItem("Undo");
        MenuItem menuItemRedo = new MenuItem("Redo");

        // Add the MenuItems to the ContextMenu
        contextMenu.getItems().addAll(menuItemAddPatient, menuItemUndo, menuItemRedo);

        // Set the context menu for the TableView
        tableView.setContextMenu(contextMenu);

        // Add event handlers to the menu items
        menuItemAddPatient.setOnAction(event -> handleNewPatient());
        menuItemUndo.setOnAction(event -> handleUndo());
        menuItemRedo.setOnAction(event -> handleRedo());

        updateHistoryList();

    }

    /**
     * This method is called when the "Sort by T" button is clicked.
     * It sorts the patients by T in ascending order for each gender.
     */
    @FXML
    private void handleSortByTButton() {
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

    /**
     * This method is called when the "Find women with high hemoglobin" button is clicked.
     */
    @FXML
    private void handleHighHemoglobinInWomenButton() {
        // Filter the patientList to include only females with elevated hemoglobin
        ObservableList<Patient> filteredList = patientList.filtered(
                patient -> patient.isFemale() && patient.isHighHemoglobin());

        // Clear the original visiblePatientList and add the filtered patients
        visiblePatientList.clear();
        visiblePatientList.addAll(filteredList);

        // This line will refresh the table to apply the custom cell factory
        tableView.refresh();
    }

    /**
     * This method finds youngest man with normal T and low hemoglobin
     */
    @FXML
    private void handleYoungestMenWithTHemoglobin() {
        // Filter the patientList to include only youngest males with normal T and low hemoglobin
        ObservableList<Patient> filteredList = patientList.filtered(
                patient -> patient.isMale() && patient.isNormalT() && patient.isLowHemoglobin() && isYoungestMale(patient, patientList));

        // Clear the original patientList and add the filtered patients
        visiblePatientList.clear();
        visiblePatientList.addAll(filteredList);

        // This line will refresh the table to apply the updated data
        tableView.refresh();
    }

    /**
     * This method adds a new patient to the list.
     */
    @FXML
    private void handleNewPatient() {
        // Create a new patient
        Patient newPatient = new Patient();

        // Load and show the patient form for adding a new patient
        loadPatientForm(newPatient, true);

        //If the patient is created, add it to the list
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

    /**
     * This method edits the selected patient.
     */
    @FXML
    private void handleEditPatient() {
        // Get the selected patient
        Patient selectedPatient = tableView.getSelectionModel().getSelectedItem();

        // Check if a patient is selected
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

    /**
     * This method loads and shows the patient form.
     *
     * @param patient      The patient to be edited or added
     * @param isNewPatient A boolean value indicating whether the patient is new
     */
    private void loadPatientForm(Patient patient, boolean isNewPatient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mhorak/coursework/patient-window.fxml"));
            Parent root = loader.load();

            PatientWindowController controller = loader.getController();
            controller.setPatient(patient, isNewPatient);

            Stage stage = new Stage();
            Image icon = new Image("patient.png");

            stage.setScene(new Scene(root));
            stage.getIcons().add(icon);
            stage.setTitle("Patient");

            // Use showAndWait to make the window modal
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method called when the "Open" menu item is clicked.
     * It opens a file chooser dialog and loads the patients from the selected file.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Patients CSV File");

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(new Stage());

        try {
            if (file != null) {
                loadPatientsFromCsv(file, patientList);

                // Set the opened file
                openedFile = file;
            }

            visiblePatientList.clear();
            visiblePatientList.addAll(patientList);
            tableView.refresh();

            updateHistoryList();
        } catch (FileReadException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("The file cannot be read");
            alert.show();
        }
    }

    /**
     * This method called when the "Save As" menu item is clicked.
     * It opens a file chooser dialog and saves the patients to the selected file.
     */
    @FXML
    void handleSaveAs() {
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

    /**
     * This method called when the "Save" menu item is clicked.
     * It saves the patients to the opened file or prompts the user to choose a file if no file is opened.
     */
    @FXML
    void handleSave() {
        // Check if a file is opened
        if (openedFile != null) {
            savePatientsToCsv(openedFile, patientList);
        } else {
            // If no file is opened, prompt the user to choose a file
            handleSaveAs();
        }
    }

    /**
     * This method called when the "Exit" menu item is clicked.
     * It closes the application.
     */
    @FXML
    void handleExit() {
        // Get the current stage
        Stage stage = (Stage) tableView.getScene().getWindow();

        // Close the stage
        stage.close();
    }

    /**
     * This method called when the "Help" menu item is clicked.
     * It displays a help message.
     */
    @FXML
    void handleHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Help");
        alert.setContentText("For help, please contact the developer at maksym.horak.pz.2022@lpnu.ua");
        alert.show();
    }

    /**
     * This method is called when the "Find older patients with high hemoglobin and low T" button is clicked.
     * It filters the patients by age, hemoglobin and T.
     */
    @FXML
    private void handleOlderHighHemoglobinLowT() {
        // Filter the patientList to include only patients older than 40 with high hemoglobin and low T
        ObservableList<Patient> filteredList = patientList.filtered(
                patient -> patient.getAge() > 40 && patient.isHighHemoglobin() && patient.isLowT());

        // Clear the original patientList and add the filtered patients
        visiblePatientList.clear();
        visiblePatientList.addAll(filteredList);

        // This line will refresh the table to apply the updated data
        tableView.refresh();
    }

    /**
     * This method is called when the "Find the oldest women with the same name and normal hemoglobin" button is clicked.
     */
    @FXML
    private void handleOldestWomenSameNameNormalHemoglobin() {

        Map<String, List<Patient>> womenGroupedByFirstName = patientList.stream()
                .filter(Patient::isFemale)
                .collect(Collectors.groupingBy(Patient::getFirstName));

        List<LinkedList<Patient>> womenWithSameName = womenGroupedByFirstName.values().stream()
                .filter(patients -> patients.size() > 1)
                .map(LinkedList::new)
                .collect(Collectors.toList());

        womenWithSameName.forEach(patients -> {
            Optional<Patient> oldestNormalHemoglobinPatient = patients.stream()
                    .filter(Patient::isNormalHemoglobin)
                    .max(Comparator.comparingInt(Patient::getAge));

            patients.clear();

            oldestNormalHemoglobinPatient.ifPresent(patients::add);
        });

        womenWithSameName.removeIf(AbstractCollection::isEmpty);

        //Add the oldest patients to the visiblePatientList
        visiblePatientList.clear();
        visiblePatientList.addAll(womenWithSameName.stream()
                .map(LinkedList::getFirst)
                .toList());

        // This line will refresh the table to apply the updated data
        tableView.refresh();
    }

    /**
     * This method is called when the "Encourage young patients" button is clicked.
     * It updates the message field for matching patients.
     */
    @FXML
    private void handleEncourageYoungPatients() {
        // Explicitly specify the column type for "Message"
        TableColumn<Patient, String> messageColumn = (TableColumn<Patient, String>) tableView.getColumns().get(7);

        handleBack();

        // Update the message field for matching patients
        messageColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Patient, String> call(TableColumn<Patient, String> param) {
                return new TableCell<>() {
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

    /**
     * This method is called when the "Delete" button is clicked.
     * It deletes the selected patient from the list.
     */
    @FXML
    void handleDeletePatient() {
        // Get the selected patient
        Patient selectedPatient = tableView.getSelectionModel().getSelectedItem();

        // Remove the selected patient from the list
        patientList.remove(selectedPatient);

        // Update the patient numbers
        setPatientsNumbers(patientList);

        handleBack();

        updateHistoryList();
    }

    /**
     * This method is used to update the history list.
     */
    private void updateHistoryList() {
        // Add the current patientList to the history
        patientListHistory.removeAll(patientListHistory.subList(historyIndex + 1, patientListHistory.size()));
        patientListHistory.add(FXCollections.observableList(List.copyOf(patientList)));
        historyIndex++;
    }

    /**
     * This method is used to update the patient numbers.
     *
     * @param patientList The list of patients
     */
    private void setPatientsNumbers(ObservableList<Patient> patientList) {
        for (int i = 0; i < patientList.size(); i++) {
            patientList.get(i).setNumber(i + 1);
        }
    }

    /**
     * This method is called when the "Back to original list" button is clicked.
     */
    @FXML
    void handleBack() {

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

    /**
     * This method is called when the "Undo" button is clicked.
     * It restores the previous version of the patient list.
     */
    @FXML
    void handleUndo() {
        if (historyIndex > 0) {
            historyIndex--;
            patientList.clear();
            patientList.addAll(patientListHistory.get(historyIndex));
            visiblePatientList.clear();
            visiblePatientList.addAll(patientList);
            tableView.refresh();
        }
    }

    /**
     * This method is called when the "Redo" button is clicked.
     * It restores the next version of the patient list.
     */
    @FXML
    void handleRedo() {
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
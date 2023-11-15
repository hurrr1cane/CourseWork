package com.mhorak.coursework;

import com.mhorak.coursework.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class MainWindow {
    @FXML
    private TableView<Patient> tableView; // This connects to your TableView in the FXML

    ObservableList<Patient> patientList = FXCollections.observableArrayList();

    public void initialize() {
        // Initialize patientList with data (you can add your Patient objects here)

        Patient patient1 = new Patient(1, "Smith", "John", 1990, "Male", 25.5, 14.2);
        Patient patient2 = new Patient(2, "Johnson", "Jane", 1985, "Female", 28.3, 12.8);
        Patient patient3 = new Patient(3, "Wohnson", "Mane", 1999, "Female", 26.3, 20.0);

        patientList.addAll(patient1, patient2, patient3);

        // Set the TableView items
        tableView.setItems(patientList);
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

        // Clear the original patientList and add sorted patients back
        patientList.clear();
        patientList.addAll(males);
        patientList.addAll(females);

        // Update the TableView to reflect the sorted list
        tableView.setItems(patientList);

        // Reset the original t values by dividing them by 100
        for (Patient patient : patientList) {
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
        // Explicitly specify the column type for "Повідомлення"
        TableColumn<Patient, String> messageColumn = (TableColumn<Patient, String>) tableView.getColumns().get(7); // Assuming "Повідомлення" is the 8th column
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
                            Patient patient = getTableView().getItems().get(getIndex());

                            // Check if the patient is female and has elevated hemoglobin
                            if (patient.isFemale() && patient.isHighHemoglobin()) {
                                setText("Elevated hemoglobin");
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
     * Set message text to "Youngest" for youngest man with low hemoglobin and normal T
     * @param event The event that triggered this method
     */
    @FXML
    private void handleYoungestMenWithTHemoglobin(ActionEvent event) {
        // Explicitly specify the column type for "Повідомлення"
        TableColumn<Patient, String> messageColumn = (TableColumn<Patient, String>) tableView.getColumns().get(7); // Assuming "Повідомлення" is the 8th column
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
                            Patient patient = getTableView().getItems().get(getIndex());

                            // Check if the patient is male with normal T and low hemoglobin
                            if (patient.isMale() && patient.isNormalT() && patient.isLowHemoglobin()) {
                                // Check if the patient is the youngest
                                if (isYoungestMale(patient)) {
                                    setText("Youngest");
                                } else {
                                    setText("");
                                }
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
    private void handleResetButton(ActionEvent event) {
        // Reset the TableView items
        tableView.setItems(patientList);

        // Reset the custom cell factory for "Повідомлення"
        TableColumn<Patient, String> messageColumn = (TableColumn<Patient, String>) tableView.getColumns().get(7); // Assuming "Повідомлення" is the 8th column
        messageColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // This line will refresh the table to apply the custom cell factory
        tableView.refresh();
    }

    @FXML
    private void handleNewPatient(ActionEvent event) {
        // Create a new patient
        Patient newPatient = new Patient();

        //Create new window to add a patient
        //AddPatientWindow addPatientWindow = new AddPatientWindow(newPatient);

        // Add the new patient to the list
        patientList.add(newPatient);

        // Update the TableView to reflect the new patient
        tableView.setItems(patientList);
    }

    @FXML
    private void handleOpen(ActionEvent event) {

    }

    @FXML void handleSave(ActionEvent event) {

    }

    @FXML void handleExit(ActionEvent event) {

    }

    @FXML
    private void handleOlderHighHemoglobinLowT(ActionEvent event) {
        // Explicitly specify the column type for "Повідомлення"
        TableColumn<Patient, String> messageColumn = (TableColumn<Patient, String>) tableView.getColumns().get(7); // Assuming "Повідомлення" is the 8th column
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
                            Patient patient = getTableView().getItems().get(getIndex());

                            // Check if the patient is male with normal T and low hemoglobin
                            // Check if the patient is older than 40, has high hemoglobin, and low T
                            if (patient.getAge() > 40 && patient.isHighHemoglobin() && patient.isLowT()) {
                                // Update the message field for matching patients
                                setText("Bad indexes");
                            }
                            else {
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
    private void handleOldestWomenSameNameNormalHemoglobin(ActionEvent event) {
        // Explicitly specify the column type for "Повідомлення"
        TableColumn<Patient, String> messageColumn = (TableColumn<Patient, String>) tableView.getColumns().get(7); // Assuming "Повідомлення" is the 8th column
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

                            // Check if the patient is female with normal hemoglobin and has the same name
                            // Determine the ones with normal hemoglobin
                            // Determine the oldest one among those with the same name
                            boolean hasSameName = false;
                            Patient oldestWithSameName = null;

                            for (Patient patient : patientList) {
                                if (!patient.equals(currentPatient) &&
                                        patient.isFemale() &&
                                        patient.isNormalHemoglobin() &&
                                        patient.getFirstName().equals(currentPatient.getFirstName())) {
                                    if (!hasSameName || oldestWithSameName.getAge() < patient.getAge()) {
                                        hasSameName = true;
                                        oldestWithSameName = patient;
                                    }
                                }
                            }

                            // Update the message field for matching patients
                            if (hasSameName) {
                                setText("Oldest with same name");
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
    private void handleEncourageYoungPatients(ActionEvent event) {
        // Explicitly specify the column type for "Повідомлення"
        TableColumn<Patient, String> messageColumn = (TableColumn<Patient, String>) tableView.getColumns().get(7); // Assuming "Повідомлення" is the 8th column
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
                                setText("Oldest with same name");
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


}

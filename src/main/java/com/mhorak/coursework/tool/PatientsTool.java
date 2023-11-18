package com.mhorak.coursework.tool;

import com.mhorak.coursework.exception.FileReadException;
import com.mhorak.coursework.model.Patient;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.List;

/**
 * Class for handling patient data
 */
public class PatientsTool {
    /**
     * Saves the patient list to a CSV file
     * @param file The file to save to
     * @param patientList The list of patients to save
     */
    public static void savePatientsToCsv(File file, List<Patient> patientList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write CSV header
            writer.write("Number,Last Name,First Name,Year of Birth,Gender,T,Hemoglobin\n");

            // Write each patient in CSV format
            for (Patient patient : patientList) {
                writer.write(patient.toCsv() + "\n");
            }

            System.out.println("Patients saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads patients from a CSV file
     * @param file The file to load from
     * @param patientList The list to load patients into
     * @throws FileReadException Thrown when the file cannot be read
     */
    public static void loadPatientsFromCsv(File file, List<Patient> patientList) throws FileReadException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Assuming the first line is the header, read and ignore it
            String headerLine = reader.readLine();

            patientList.clear();

            // Read patient data from the CSV file
            String line;
            while ((line = reader.readLine()) != null) {
                Patient patient = parsePatientFromCsv(line);
                // Add the parsed patient to your patientList
                patientList.add(patient);
            }

            System.out.println("Patients loaded from: " + file.getAbsolutePath());
        } catch (Throwable e) {
            throw new FileReadException();
        }
    }

    /**
     * Parses a patient from a CSV line
     * @param csvLine The CSV line to parse
     * @return The parsed patient
     * @throws FileReadException Thrown when the CSV line cannot be parsed
     */
    private static Patient parsePatientFromCsv(String csvLine) throws FileReadException {
        // Split the CSV line into fields
        String[] fields = csvLine.split(",");

        // Trim each field to remove leading and trailing whitespaces
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }

        if (fields.length != 7) {
            throw new FileReadException();
        }

        // Create a new Patient object using the fields
        Patient patient = new Patient();
        patient.setNumber(Integer.parseInt(fields[0]));
        patient.setLastName(fields[1]);
        patient.setFirstName(fields[2]);
        patient.setYearOfBirth(Integer.parseInt(fields[3]));
        patient.setGender(fields[4]);
        patient.setT(Double.parseDouble(fields[5]));
        patient.setHemoglobin(Double.parseDouble(fields[6]));

        return patient;
    }

    /**
     * Sorts the patient list by t using counting sort
     * @param patients The list of patients to sort
     */
    public static void countingSortByT(ObservableList<Patient> patients) {
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
    private static int getMaxT(ObservableList<Patient> patients) {
        int maxT = -1;

        for (Patient patient : patients) {
            int t = (int) patient.getT();
            if (t > maxT) {
                maxT = t;
            }
        }

        return maxT;
    }

    // Helper method to check if a patient is the youngest male with normal T and low hemoglobin
    public static boolean isYoungestMale(Patient currentPatient, ObservableList<Patient> patientList) {
        for (Patient patient : patientList) {
            if (patient.isMale() && patient.isNormalT() && patient.isLowHemoglobin()) {
                // Compare ages
                if (currentPatient.getAge() > patient.getAge()) {
                    return false;
                }
            }
        }
        return true;
    }
}

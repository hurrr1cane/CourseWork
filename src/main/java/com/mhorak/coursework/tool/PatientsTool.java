package com.mhorak.coursework.tool;

import com.mhorak.coursework.model.Patient;

import java.io.*;
import java.util.List;

public class PatientsTool {
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

    public static void loadPatientsFromCsv(File file, List<Patient> patientList) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Assuming the first line is the header, read and ignore it
            String headerLine = reader.readLine();

            // Read patient data from the CSV file
            String line;
            while ((line = reader.readLine()) != null) {
                Patient patient = parsePatientFromCsv(line);
                // Add the parsed patient to your patientList
                patientList.add(patient);
            }

            System.out.println("Patients loaded from: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Patient parsePatientFromCsv(String csvLine) {
        // Split the CSV line into fields
        String[] fields = csvLine.split(",");

        // Trim each field to remove leading and trailing whitespaces
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }

        // Create a new Patient object using the fields
        // Note: You may need to adjust the order of fields based on your CSV format
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
}

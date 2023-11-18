package com.mhorak.coursework.model;

import lombok.Data;

import java.io.Serializable;
import java.time.Year;

/**
 * Class for storing patient information
 */
@Data
public class Patient implements Serializable {
    private int number;
    private String lastName;
    private String firstName;
    private int yearOfBirth;
    private String gender;
    private double t;
    private double hemoglobin;

    /**
     * Default constructor
     */
    public Patient() {
        // Default constructor nullifies all fields
        this.number = 0;
        this.lastName = null;
        this.firstName = null;
        this.gender = null;
        this.yearOfBirth = 0;
        this.t = 0;
        this.hemoglobin = 0;
    }

    /**
     * Constructor with all fields
     *
     * @param number      Patient number
     * @param lastName    Patient last name
     * @param firstName   Patient first name
     * @param yearOfBirth Patient year of birth
     * @param gender      Patient's gender
     * @param t           Patient's temperature
     * @param hemoglobin  Patient's hemoglobin
     */
    public Patient(int number, String lastName, String firstName, int yearOfBirth, String gender, double t, double hemoglobin) {
        this.number = number;
        this.lastName = lastName;
        this.firstName = firstName;
        this.yearOfBirth = yearOfBirth;
        this.gender = gender;
        this.t = t;
        this.hemoglobin = hemoglobin;
    }

    /**
     * Copy constructor
     *
     * @param other Patient object
     */
    public Patient(Patient other) {
        this.number = other.number;
        this.lastName = other.lastName;
        this.firstName = other.firstName;
        this.yearOfBirth = other.yearOfBirth;
        this.gender = other.gender;
        this.t = other.t;
        this.hemoglobin = other.hemoglobin;
    }

    /**
     * Returns a string representation of the patient
     *
     * @return String representation of the patient
     */
    @Override
    public String toString() {
        return "Patient #" + number + ": " + firstName + " " + lastName + ", born in " + yearOfBirth +
                ", Gender: " + gender + ", t: " + t + ", Hemoglobin: " + hemoglobin;
    }

    /**
     * Returns a CSV representation of the patient
     *
     * @return CSV representation of the patient
     */
    public String toCsv() {
        return number + ", " +
                lastName + ", " +
                firstName + ", " +
                yearOfBirth + ", " +
                gender + ", " +
                t + ", " +
                hemoglobin;
    }

    /**
     * Returns the patient's age
     *
     * @return Patient's age
     */
    public int getAge() {
        return Year.now().getValue() - yearOfBirth;
    }

    /**
     * Returns true if the patient's temperature is normal
     *
     * @return True if the patient's temperature is normal
     */
    public boolean isNormalT() {
        return t >= 36.5 && t <= 37.5;
    }

    /**
     * Returns true if the patient's temperature is low
     *
     * @return True if the patient's temperature is low
     */
    public boolean isLowT() {
        return t < 36.5;
    }

    /**
     * Returns true if the patient's temperature is high
     *
     * @return True if the patient's temperature is high
     */
    public boolean isHighT() {
        return t > 37.5;
    }

    /**
     * Returns true if the patient's hemoglobin is high
     *
     * @return True if the patient's hemoglobin is high
     */
    public boolean isHighHemoglobin() {
        if (gender.equals("Female")) {
            return hemoglobin > 150;
        } else {
            return hemoglobin > 160;
        }
    }

    /**
     * Returns true if the patient's hemoglobin is normal
     *
     * @return True if the patient's hemoglobin is normal
     */
    public boolean isNormalHemoglobin() {
        if (gender.equals("Female")) {
            return hemoglobin >= 120 && hemoglobin <= 150;
        } else {
            return hemoglobin >= 130 && hemoglobin <= 160;
        }
    }

    /**
     * Returns true if the patient's hemoglobin is low
     *
     * @return True if the patient's hemoglobin is low
     */
    public boolean isLowHemoglobin() {
        if (gender.equals("Female")) {
            return hemoglobin < 120;
        } else {
            return hemoglobin < 130;
        }
    }

    /**
     * Method to check if the patient is male
     *
     * @return True if patient is a male
     */
    public boolean isMale() {
        return gender.equals("Male");
    }

    /**
     * Method to check if the patient is female
     *
     * @return True if patient is a female
     */
    public boolean isFemale() {
        return gender.equals("Female");
    }

    /**
     * Method to check if the patients are equal
     *
     * @param other Patient object
     * @return True if patients equal
     */
    public boolean equals(Patient other) {
        return (this.number == other.number &&
                this.firstName.equals(other.firstName) &&
                this.lastName.equals(other.lastName) &&
                this.yearOfBirth == other.yearOfBirth &&
                this.t == other.t &&
                this.hemoglobin == other.hemoglobin);
    }

    /**
     * Method to check if the patient is empty
     *
     * @return True if patient is empty
     */
    public boolean isEmpty() {
        return (this.number == 0 &&
                this.firstName == null &&
                this.lastName == null &&
                this.yearOfBirth == 0 &&
                this.t == 0 &&
                this.hemoglobin == 0);
    }

}
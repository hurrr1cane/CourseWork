package com.mhorak.coursework.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Year;

@Data
public class Patient implements Serializable {
    private int number;
    private String lastName;
    private String firstName;
    private int yearOfBirth;
    private String gender;
    private double t;
    private double hemoglobin;

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

    public Patient(int number, String lastName, String firstName, int yearOfBirth, String gender, double t, double hemoglobin) {
        this.number = number;
        this.lastName = lastName;
        this.firstName = firstName;
        this.yearOfBirth = yearOfBirth;
        this.gender = gender;
        this.t = t;
        this.hemoglobin = hemoglobin;
    }

    public Patient(Patient other) {
        // Copy constructor
        this.number = other.number;
        this.lastName = other.lastName;
        this.firstName = other.firstName;
        this.yearOfBirth = other.yearOfBirth;
        this.gender = other.gender;
        this.t = other.t;
        this.hemoglobin = other.hemoglobin;
    }

    // Override the toString method to display patient information
    @Override
    public String toString() {
        return "Patient #" + number + ": " + firstName + " " + lastName + ", born in " + yearOfBirth +
                ", Gender: " + gender + ", t: " + t + ", Hemoglobin: " + hemoglobin;
    }

    public String toCsv() {
        StringBuilder csvBuilder = new StringBuilder();

        csvBuilder.append(number).append(", ");
        csvBuilder.append(lastName).append(", ");
        csvBuilder.append(firstName).append(", ");
        csvBuilder.append(yearOfBirth).append(", ");
        csvBuilder.append(gender).append(", ");
        csvBuilder.append(t).append(", ");
        csvBuilder.append(hemoglobin);

        return csvBuilder.toString();
    }

    public int getAge() {
        return Year.now().getValue() - yearOfBirth;
    }

    public boolean isNormalT() {
        return t >= 36.5 && t <= 37.5;
    }

    public boolean isLowT() {
        return t < 36.5;
    }

    public boolean isHightT() {
        return t > 37.5;
    }

    public boolean isHighHemoglobin() {
        if (gender.equals("Female")) {
            return hemoglobin > 150;
        } else {
            return hemoglobin > 160;
        }
    }

    public boolean isNormalHemoglobin() {
        if (gender.equals("Female")) {
            return hemoglobin >= 120 && hemoglobin <= 150;
        } else {
            return hemoglobin >= 130 && hemoglobin <= 160;
        }
    }

    public boolean isLowHemoglobin() {
        if (gender.equals("Female")) {
            return hemoglobin < 120;
        } else {
            return hemoglobin < 130;
        }
    }

    public boolean isMale() {
        return gender.equals("Male");
    }

    public boolean isFemale() {
        return gender.equals("Female");
    }

    public boolean equals(Patient other) {
        return (this.number == other.number &&
                this.firstName.equals(other.firstName) &&
                this.lastName.equals(other.lastName) &&
                this.yearOfBirth == other.yearOfBirth &&
                this.t == other.t &&
                this.hemoglobin == other.hemoglobin);
    }

    public boolean isEmpty() {
        return (this.number == 0 &&
                this.firstName == null &&
                this.lastName == null &&
                this.yearOfBirth == 0 &&
                this.t == 0 &&
                this.hemoglobin == 0);
    }

}
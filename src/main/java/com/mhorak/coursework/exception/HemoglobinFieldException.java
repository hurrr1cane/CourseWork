package com.mhorak.coursework.exception;

/**
 * Class for exceptions thrown when the hemoglobin field is incorrect
 */
public class HemoglobinFieldException extends InputFieldException {
    public HemoglobinFieldException(String message) {
        super(message);
    }

    public HemoglobinFieldException() {
        super("Hemoglobin field is incorrect");
    }
}

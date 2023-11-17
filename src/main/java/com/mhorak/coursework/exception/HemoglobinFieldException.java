package com.mhorak.coursework.exception;

public class HemoglobinFieldException extends InputFieldException{
    public HemoglobinFieldException(String message) {
        super(message);
    }

    public HemoglobinFieldException() {
        super("Hemoglobin field is incorrect");
    }
}

package com.mhorak.coursework.exception;

public class SurnameFieldException extends InputFieldException{
    public SurnameFieldException(String message) {
        super(message);
    }

    public SurnameFieldException() {
        super("Surname field is incorrect");
    }
}

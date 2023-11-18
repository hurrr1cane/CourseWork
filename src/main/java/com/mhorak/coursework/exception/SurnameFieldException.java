package com.mhorak.coursework.exception;

/**
 * Class for exceptions thrown when the surname field is incorrect
 */
public class SurnameFieldException extends InputFieldException{
    public SurnameFieldException(String message) {
        super(message);
    }

    public SurnameFieldException() {
        super("Surname field is incorrect");
    }
}

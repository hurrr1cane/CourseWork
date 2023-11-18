package com.mhorak.coursework.exception;

/**
 * Class for exceptions thrown when the name field is incorrect
 */
public class NameFieldException extends InputFieldException {
    public NameFieldException(String message) {
        super(message);
    }

    public NameFieldException() {
        super("Name field is incorrect");
    }

}

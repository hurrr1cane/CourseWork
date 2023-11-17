package com.mhorak.coursework.exception;

public class NameFieldException extends InputFieldException {
    public NameFieldException(String message) {
        super(message);
    }

    public NameFieldException() {
        super("Name field is incorrect");
    }

}

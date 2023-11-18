package com.mhorak.coursework.exception;

public class GenderFieldException extends InputFieldException{
    public GenderFieldException(String message) {
        super(message);
    }

    public GenderFieldException() {
        super("Gender field must be filled");
    }
}

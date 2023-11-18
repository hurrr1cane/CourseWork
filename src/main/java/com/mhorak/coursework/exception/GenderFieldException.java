package com.mhorak.coursework.exception;

/**
 * Class for exceptions, when gender field isn't filled
 */
public class GenderFieldException extends InputFieldException {
    public GenderFieldException(String message) {
        super(message);
    }

    public GenderFieldException() {
        super("Gender field must be filled");
    }
}

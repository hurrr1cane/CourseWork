package com.mhorak.coursework.exception;

/**
 * Class for exceptions, when number field filled incorrectly
 */
public class NumberFieldException extends InputFieldException {
    public NumberFieldException(String message) {
        super(message);
    }

    public NumberFieldException() {
        super("Number field filled incorrectly");
    }
}

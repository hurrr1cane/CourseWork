package com.mhorak.coursework.exception;

/**
 * Abstract class for exceptions thrown when the input field is incorrect
 */
public abstract class InputFieldException extends Throwable {
    public InputFieldException(String message) {
        super(message);
    }
}

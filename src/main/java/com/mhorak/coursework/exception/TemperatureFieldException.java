package com.mhorak.coursework.exception;

/**
 * Class for exceptions thrown when the temperature field is incorrect
 */
public class TemperatureFieldException extends InputFieldException{
    public TemperatureFieldException(String message) {
        super(message);
    }

    public TemperatureFieldException() {
        super("Temperature field is incorrect");
    }
}

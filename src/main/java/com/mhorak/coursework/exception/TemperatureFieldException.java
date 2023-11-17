package com.mhorak.coursework.exception;

public class TemperatureFieldException extends InputFieldException{
    public TemperatureFieldException(String message) {
        super(message);
    }

    public TemperatureFieldException() {
        super("Temperature field is incorrect");
    }
}

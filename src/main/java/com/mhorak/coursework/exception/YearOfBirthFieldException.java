package com.mhorak.coursework.exception;

public class YearOfBirthFieldException extends InputFieldException{
    public YearOfBirthFieldException(String message) {
        super(message);
    }

    public YearOfBirthFieldException() {
        super("Year of birth field is incorrect");
    }
}

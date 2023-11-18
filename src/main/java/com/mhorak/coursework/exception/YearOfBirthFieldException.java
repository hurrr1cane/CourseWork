package com.mhorak.coursework.exception;

/**
 * Class for exceptions thrown when the year of birth field is incorrect
 */
public class YearOfBirthFieldException extends InputFieldException{
    public YearOfBirthFieldException(String message) {
        super(message);
    }

    public YearOfBirthFieldException() {
        super("Year of birth field is incorrect");
    }
}

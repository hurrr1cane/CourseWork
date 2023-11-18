package com.mhorak.coursework.exception;

public class FileReadException extends Throwable {
    public FileReadException(String message) {
        super(message);
    }

    public FileReadException() {
        super("File read error");
    }
}

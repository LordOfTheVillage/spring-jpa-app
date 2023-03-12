package com.example.japapp.exeptions;

public class NotFoundBookException extends Exception {
    public NotFoundBookException(String message) {
        super(message);
    }
}

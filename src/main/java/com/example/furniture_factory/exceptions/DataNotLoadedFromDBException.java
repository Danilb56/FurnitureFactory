package com.example.furniture_factory.exceptions;

public class DataNotLoadedFromDBException extends RuntimeException {

    public DataNotLoadedFromDBException(String message) {
        super(message);
    }
}

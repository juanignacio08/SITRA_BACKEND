package com.sitra.sitra.exceptions;

public class DuplicateKeyError extends RuntimeException {
    public DuplicateKeyError(String message) {
        super(message);
    }
}
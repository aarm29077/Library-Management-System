package com.example.Library.util.customExceptions;

public class AuthorNotCreatedException extends RuntimeException {
    public AuthorNotCreatedException(String message) {
        super(message);
    }
}
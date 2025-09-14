package com.springbootBackend.backend.exceptions.customExceptions;

public class PhoneNumberAlreadyExistsException extends RuntimeException {

    public PhoneNumberAlreadyExistsException(String message) {
        super(message);
    }

}


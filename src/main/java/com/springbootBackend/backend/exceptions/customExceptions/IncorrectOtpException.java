package com.springbootBackend.backend.exceptions.customExceptions;

public class IncorrectOtpException extends RuntimeException{

    public IncorrectOtpException(String message) {
        super(message);
    }
}

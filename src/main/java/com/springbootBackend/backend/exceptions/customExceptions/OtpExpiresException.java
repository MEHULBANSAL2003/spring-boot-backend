package com.springbootBackend.backend.exceptions.customExceptions;

public class OtpExpiresException extends RuntimeException{

    public OtpExpiresException(String message) {
        super(message);
    }
}

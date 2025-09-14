package com.springbootBackend.backend.exceptions.customExceptions;

public class IncorrectOtpLimitReachException extends RuntimeException{

    public IncorrectOtpLimitReachException(String message){
        super(message);
    }

}

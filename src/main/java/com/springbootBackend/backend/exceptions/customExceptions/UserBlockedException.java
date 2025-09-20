package com.springbootBackend.backend.exceptions.customExceptions;

public class UserBlockedException extends RuntimeException{

    public UserBlockedException(String message){
        super(message);
    }

}

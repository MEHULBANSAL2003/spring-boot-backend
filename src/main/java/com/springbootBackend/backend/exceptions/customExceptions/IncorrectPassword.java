package com.springbootBackend.backend.exceptions.customExceptions;

public class IncorrectPassword extends RuntimeException{

    public IncorrectPassword(String message){
        super(message);
    }
}

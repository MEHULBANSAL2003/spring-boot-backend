package com.springbootBackend.backend.exceptions.customExceptions;

public class UserNameExistsException extends RuntimeException{
    public UserNameExistsException(String message){
        super(message);
    }
}

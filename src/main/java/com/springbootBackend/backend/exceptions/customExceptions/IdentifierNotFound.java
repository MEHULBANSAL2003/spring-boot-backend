package com.springbootBackend.backend.exceptions.customExceptions;

public class IdentifierNotFound extends RuntimeException{

    public IdentifierNotFound(String message){
        super(message);
    }

}




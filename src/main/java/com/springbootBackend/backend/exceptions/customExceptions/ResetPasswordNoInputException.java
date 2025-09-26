package com.springbootBackend.backend.exceptions.customExceptions;

public class ResetPasswordNoInputException extends RuntimeException{

public ResetPasswordNoInputException(String message){
  super(message);
}

}

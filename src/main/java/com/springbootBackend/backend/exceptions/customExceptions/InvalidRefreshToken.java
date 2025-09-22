package com.springbootBackend.backend.exceptions.customExceptions;

public class InvalidRefreshToken extends RuntimeException{

  public InvalidRefreshToken(String message){
    super(message);
  }

}

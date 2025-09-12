package com.springbootBackend.backend.exceptions;


import com.springbootBackend.backend.ErrorResponse.InputValidationError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InputValidationError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request){
    FieldError fieldError = (FieldError) ex.getBindingResult().getAllErrors().get(0);
    String reason = fieldError.getDefaultMessage();
    InputValidationError error = new InputValidationError("fail", reason, request.getRequestURI(), HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
}
}

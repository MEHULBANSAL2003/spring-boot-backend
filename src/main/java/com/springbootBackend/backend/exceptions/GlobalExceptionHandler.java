package com.springbootBackend.backend.exceptions;


import com.springbootBackend.backend.ErrorResponse.InputValidationError;
import com.springbootBackend.backend.ErrorResponse.MethodNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // validation error : 400 (bad request)
@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InputValidationError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request){
    FieldError fieldError = (FieldError) ex.getBindingResult().getAllErrors().get(0);
    String reason = fieldError.getDefaultMessage();
    InputValidationError error = new InputValidationError("fail", reason, request.getRequestURI(), HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
};

//not founf error: 404

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<MethodNotFound> handleNotFoundExeption(NoHandlerFoundException ex, HttpServletRequest request){
        MethodNotFound error = new MethodNotFound("fail", "The requested URL was not found", request.getRequestURI(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<MethodNotFound>(error,HttpStatus.NOT_FOUND);
    }

}

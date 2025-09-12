package com.springbootBackend.backend.exceptions;


import com.springbootBackend.backend.ErrorResponse.InputValidationError;
import com.springbootBackend.backend.ErrorResponse.MethodNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    String message = fieldError.getDefaultMessage();
    InputValidationError error = new InputValidationError(HttpStatus.BAD_REQUEST.getReasonPhrase(), message, request.getRequestURI(), HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
};

//validation error : 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<InputValidationError> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        InputValidationError error = new InputValidationError(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Request body is missing or malformed",
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

//not found error: 404

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<MethodNotFound> handleNotFoundExeption(NoHandlerFoundException ex, HttpServletRequest request){
        MethodNotFound error = new MethodNotFound(HttpStatus.NOT_FOUND.getReasonPhrase(), "The requested URL was not found", request.getRequestURI(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<MethodNotFound>(error,HttpStatus.NOT_FOUND);
    }

}

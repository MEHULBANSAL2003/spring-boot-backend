package com.springbootBackend.backend.exceptions;


import com.springbootBackend.backend.ErrorResponse.CustomErrorMsgFormat;
import com.springbootBackend.backend.exceptions.customExceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // validation error : 400 (bad request)
@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorMsgFormat> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request){
    FieldError fieldError = (FieldError) ex.getBindingResult().getAllErrors().get(0);
    String message = fieldError.getDefaultMessage();
    CustomErrorMsgFormat error = new CustomErrorMsgFormat(HttpStatus.BAD_REQUEST.getReasonPhrase(), message, request.getRequestURI(), HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
};

//validation error : 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorMsgFormat> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        CustomErrorMsgFormat error = new CustomErrorMsgFormat(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Request body is missing or malformed",
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

//not found error: 404

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CustomErrorMsgFormat> handleNotFoundException(NoHandlerFoundException ex, HttpServletRequest request){
        CustomErrorMsgFormat error = new CustomErrorMsgFormat(HttpStatus.NOT_FOUND.getReasonPhrase(), "The requested URL was not found", request.getRequestURI(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    //method not allowed: 405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CustomErrorMsgFormat> handleHttpRequestMethodNotAllowedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request){
        CustomErrorMsgFormat error = new CustomErrorMsgFormat(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), "The HTTP method used is not supported for this endpoint.", request.getRequestURI(), HttpStatus.METHOD_NOT_ALLOWED.value());
        return new ResponseEntity<>(error,HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({OtpExpiresException.class, IncorrectOtpException.class})
    public ResponseEntity<CustomErrorMsgFormat> invalidOtpCredentialsException(Exception ex, HttpServletRequest request){
        CustomErrorMsgFormat error = new CustomErrorMsgFormat(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    // custom exception (409 -> phone number already exists)
    @ExceptionHandler({PhoneNumberAlreadyExistsException.class, UserNameExistsException.class,IllegalArgumentException.class})
    public ResponseEntity<CustomErrorMsgFormat> handlePhoneNumberAlreadyRegisteredException(Exception ex, HttpServletRequest request){
        CustomErrorMsgFormat error = new CustomErrorMsgFormat(HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

    @ExceptionHandler({IdentifierNotFound.class,IncorrectPassword.class})
    public ResponseEntity<CustomErrorMsgFormat> handleIdentifierNotFoundException(Exception ex, HttpServletRequest request){
        CustomErrorMsgFormat error = new CustomErrorMsgFormat(HttpStatus.UNAUTHORIZED.getReasonPhrase(),ex.getMessage(), request.getRequestURI(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<CustomErrorMsgFormat> handleDatabaseError(DataAccessException ex, HttpServletRequest request) {
        CustomErrorMsgFormat error = new CustomErrorMsgFormat(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "error: "+ex.getMostSpecificCause().getMessage(),
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({IncorrectOtpLimitReachException.class})
    public ResponseEntity<CustomErrorMsgFormat> handlIncorrectOtpLimitException(IncorrectOtpLimitReachException ex, HttpServletRequest request) {
        CustomErrorMsgFormat error = new CustomErrorMsgFormat(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(), ex.getMessage(), request.getRequestURI(), HttpStatus.TOO_MANY_REQUESTS.value());
        return new ResponseEntity<>(error,HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(UserBlockedException.class)
    public ResponseEntity<CustomErrorMsgFormat> handlBlockedUserException(UserBlockedException ex, HttpServletRequest request) {
        CustomErrorMsgFormat error = new CustomErrorMsgFormat(HttpStatus.FORBIDDEN.getReasonPhrase(), ex.getMessage(), request.getRequestURI(), HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(error,HttpStatus.FORBIDDEN);
    }


    //internal server error exception - 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorMsgFormat> handleInternalServerException(Exception ex, HttpServletRequest request){
        CustomErrorMsgFormat error = new CustomErrorMsgFormat(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "An unexpected error occurred. Please try again later.", request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

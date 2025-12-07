package com.springbootBackend.backend.ErrorResponse;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomErrorMsgFormat {

    private final String error;
    private final String message;
    private final LocalDateTime timestamp;
    private final int status;
    private final String path;

    public CustomErrorMsgFormat(String error, String message, String path, int status){
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.path = path;
    }


}

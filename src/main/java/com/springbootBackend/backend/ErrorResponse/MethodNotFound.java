package com.springbootBackend.backend.ErrorResponse;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MethodNotFound {

    private String error;
    private String message;
    private LocalDateTime timestamp;
    private int status;
    private String path;

    public MethodNotFound(String error, String message, String path, int status) {
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.path = path;
    }

    public String getError() { return error; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp(){ return timestamp; }
    public int getStatus() { return status; }
    public String getPath() { return path; }
}

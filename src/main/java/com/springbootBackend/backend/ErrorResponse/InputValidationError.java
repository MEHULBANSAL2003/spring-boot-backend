package com.springbootBackend.backend.ErrorResponse;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class InputValidationError {

    private String status;
    private String reason;
    private LocalDateTime timestamp;
    private int httpCode;
    private String path;

    public InputValidationError(String status, String reason, String path, int httpCode) {
        this.status = status;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
       this.httpCode = httpCode;
       this.path = path;
    }

    public String getStatus() { return status; }
    public String getReason() { return reason; }
    public LocalDateTime getTimestamp(){ return timestamp; }
    public int getHttpCode() { return httpCode; }
    public String getPath() { return path; }
}

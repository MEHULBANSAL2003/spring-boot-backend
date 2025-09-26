package com.springbootBackend.backend.dto.ResetPassword;

public class ResetPasswordFinalResponseDto {

  String status;
  String message;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  // Getter and Setter for message
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

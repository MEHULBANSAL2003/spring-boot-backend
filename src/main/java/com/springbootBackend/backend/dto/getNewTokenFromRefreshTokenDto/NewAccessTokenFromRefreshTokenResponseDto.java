package com.springbootBackend.backend.dto.getNewTokenFromRefreshTokenDto;

public class NewAccessTokenFromRefreshTokenResponseDto {
  private String status;
  private String message;
  private String reason;
  private String authToken;
  private String refreshToken;

  public NewAccessTokenFromRefreshTokenResponseDto() {}

  public NewAccessTokenFromRefreshTokenResponseDto(String status, String reason) {
    this.status = status;
    this.reason = reason;
  }

  public NewAccessTokenFromRefreshTokenResponseDto(
    String status,
    String message,
    String authToken,
    String refreshToken
  ) {
    this.status = status;
    this.message = message;
    this.authToken = authToken;
    this.refreshToken = refreshToken;
  }

  // ✅ Getters & setters
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public String getMessage() { return message; }
  public void setMessage(String message) { this.message = message; }

  public String getReason() { return reason; }
  public void setReason(String reason) { this.reason = reason; }

  public String getAuthToken() { return authToken; }
  public void setAuthToken(String authToken) { this.authToken = authToken; }

  public String getRefreshToken() { return refreshToken; }
  public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}

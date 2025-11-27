package com.springbootBackend.backend.dto.getNewTokenFromRefreshTokenDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewAccessTokenFromRefreshTokenResponseDto {
  // ✅ Getters & setters
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

}

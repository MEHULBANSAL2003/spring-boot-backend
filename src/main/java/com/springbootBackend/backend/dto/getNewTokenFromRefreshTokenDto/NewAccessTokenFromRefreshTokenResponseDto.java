package com.springbootBackend.backend.dto.getNewTokenFromRefreshTokenDto;

public class NewAccessTokenFromRefreshTokenResponseDto {
  String status;
  String message;
  String reason;
  String authToken;
  String refreshToken;


  public NewAccessTokenFromRefreshTokenResponseDto(String status, String reason){
    this.status = status;
    this.reason = reason;
  }

  public  NewAccessTokenFromRefreshTokenResponseDto(String status, String message, String authToken, String refreshToken){
    this.status = status;
    this.message = message;
    this.authToken = authToken;
    this.refreshToken = refreshToken;
  }


}

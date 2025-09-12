package com.springbootBackend.backend.dto.userMobileSignUpDto;

import jakarta.validation.constraints.NotBlank;



public class MobileSignUpResponseDto {

    String status;
    int otpLength;
    boolean isOtpRequired;
    String message;
    int expiresIn;
    int retryAfter;

    public MobileSignUpResponseDto(String status, int otpLength, boolean isOtpRequired,String message,int expiresIn, int retryAfter){
        this.status = status;
        this.isOtpRequired = isOtpRequired;
        this.otpLength = otpLength;
        this.message = message;
        this.expiresIn = expiresIn;
        this.retryAfter = retryAfter;
    }

    public String getStatus(){ return this.status; }
    public int getOtpLength(){return this.otpLength; }
    public boolean getIsOtpRequired(){return this.isOtpRequired; }
    public String getMessage(){return this.message; }
    public int getExpiresIn(){return this.expiresIn; }
    public int getRetryAfter(){return this.retryAfter;}

}

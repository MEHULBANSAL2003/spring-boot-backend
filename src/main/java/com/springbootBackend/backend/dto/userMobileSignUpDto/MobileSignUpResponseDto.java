package com.springbootBackend.backend.dto.userMobileSignUpDto;

import jakarta.validation.constraints.NotBlank;



public class MobileSignUpResponseDto {

    String status;
    int otpLength;
    boolean isOtpRequired;
    String message;
    int expiresIn;
    int retryAfter;
    boolean isTwilioActive;

    public MobileSignUpResponseDto(String status, int otpLength, boolean isOtpRequired,String message,int expiresIn, int retryAfter, boolean isTwilioActive){
        this.status = status;
        this.isOtpRequired = isOtpRequired;
        this.otpLength = otpLength;
        this.message = message;
        this.expiresIn = expiresIn;
        this.retryAfter = retryAfter;
        this.isTwilioActive = isTwilioActive;
    }

    public String getStatus(){ return this.status; }
    public int getOtpLength(){return this.otpLength; }
    public boolean getIsOtpRequired(){return this.isOtpRequired; }
    public String getMessage(){return this.message; }
    public int getExpiresIn(){return this.expiresIn; }
    public int getRetryAfter(){return this.retryAfter;}
    public boolean getIsTwilioActive(){return this.isTwilioActive; }

}

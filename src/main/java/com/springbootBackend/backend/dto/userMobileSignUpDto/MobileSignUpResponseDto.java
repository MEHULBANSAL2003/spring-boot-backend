package com.springbootBackend.backend.dto.userMobileSignUpDto;

import jakarta.validation.constraints.NotBlank;



public class MobileSignUpResponseDto {

    String status;
    int otpLength;
    boolean isOtpRequired;

    public MobileSignUpResponseDto(String status, int otpLength, boolean isOtpRequired){
        this.status = status;
        this.isOtpRequired = isOtpRequired;
        this.otpLength = otpLength;
    }

    public String getStatus(){ return this.status; }
    public int getOtpLength(){return this.otpLength; }
    public boolean getIsOtpRequired(){return this.isOtpRequired; }

}

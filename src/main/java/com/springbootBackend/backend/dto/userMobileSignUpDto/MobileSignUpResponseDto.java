package com.springbootBackend.backend.dto.userMobileSignUpDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MobileSignUpResponseDto {
    String status;
    int otpLength;
    boolean isOtpRequired;
    String message;
    int expiresIn;
    boolean isTwilioActive;

    public MobileSignUpResponseDto(String status, int otpLength, boolean isOtpRequired,String message,int expiresIn,boolean isTwilioActive){
        this.status = status;
        this.isOtpRequired = isOtpRequired;
        this.otpLength = otpLength;
        this.message = message;
        this.expiresIn = expiresIn;
        this.isTwilioActive = isTwilioActive;
    }

}

package com.springbootBackend.backend.dto.userEmailSignUpDto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailSignUpResponseDto {

    String status;
    int otpLength;
    boolean isOtpRequired;
    String message;
    int expiresIn;
    boolean isTwilioActive;

    public EmailSignUpResponseDto(String status, int otpLength, boolean isOtpRequired,String message,int expiresIn,boolean isTwilioActive){
        this.status = status;
        this.isOtpRequired = isOtpRequired;
        this.otpLength = otpLength;
        this.message = message;
        this.expiresIn = expiresIn;
        this.isTwilioActive = isTwilioActive;
    }

}

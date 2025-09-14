package com.springbootBackend.backend.dto.userEmailSignUpDto;

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

    public String getStatus(){ return this.status; }
    public int getOtpLength(){return this.otpLength; }
    public boolean getIsOtpRequired(){return this.isOtpRequired; }
    public String getMessage(){return this.message; }
    public int getExpiresIn(){return this.expiresIn; }
    public boolean getIsTwilioActive(){return this.isTwilioActive; }
}

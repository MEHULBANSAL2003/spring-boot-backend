package com.springbootBackend.backend.dto.userEmailMobileSignUpVerificationDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserEmailSignupVerificationRequestDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    String userName;

    @NotBlank(message = "Please provide otp")
    @Size(min = 6, max = 6, message = "Otp must be of 6 characters")
    String otp;

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}

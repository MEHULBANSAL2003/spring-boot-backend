package com.springbootBackend.backend.dto.ResetPassword;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordOtpVerifyRequestDto {

  @NotBlank(message = "identifier is required")
  String identifier;
  @NotBlank(message = "otp is required")
  String otp;

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  // Getter and Setter for otp
  public String getOtp() {
    return otp;
  }

  public void setOtp(String otp) {
    this.otp = otp;
  }
}

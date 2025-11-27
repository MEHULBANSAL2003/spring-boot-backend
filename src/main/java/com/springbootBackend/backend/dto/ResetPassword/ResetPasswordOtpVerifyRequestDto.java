package com.springbootBackend.backend.dto.ResetPassword;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResetPasswordOtpVerifyRequestDto {

  @NotBlank(message = "identifier is required")
  String identifier;
  // Getter and Setter for otp
  @NotBlank(message = "otp is required")
  String otp;

}

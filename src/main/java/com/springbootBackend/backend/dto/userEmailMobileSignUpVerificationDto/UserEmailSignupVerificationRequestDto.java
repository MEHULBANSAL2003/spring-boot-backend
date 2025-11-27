package com.springbootBackend.backend.dto.userEmailMobileSignUpVerificationDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEmailSignupVerificationRequestDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    String userName;

    @NotBlank(message = "Please provide otp")
    @Size(min = 6, max = 6, message = "Otp must be of 6 characters")
    String otp;
}

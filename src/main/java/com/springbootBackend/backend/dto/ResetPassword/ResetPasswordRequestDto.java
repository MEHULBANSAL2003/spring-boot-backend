package com.springbootBackend.backend.dto.ResetPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResetPasswordRequestDto {

  @Pattern(regexp = "^[0-9]{10}$", message = "Invalid Phone Number")
  String phoneNumber;


  @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
  @Pattern(regexp = "^[a-zA-Z0-9._@]{3,20}$", message = "Username can only contain letters, numbers, dot (.), underscore (_), and at (@), and must be 3–20 characters long")
  String userName;


  @Email(message = "Invalid email format")
  private String email;

}

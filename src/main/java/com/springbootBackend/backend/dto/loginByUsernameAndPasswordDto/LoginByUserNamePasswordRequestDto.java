package com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class LoginByUserNamePasswordRequestDto {

    @NotBlank(message = "Username, email, or phone number is required")
    String identifier;

    @NotBlank(message = "Password is required")
    String password;

}

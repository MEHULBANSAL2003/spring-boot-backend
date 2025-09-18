package com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class LoginByUserNamePasswordRequestDto {

    @NotBlank(message = "Username, email, or phone number is required")
    String identifier;

    @NotBlank(message = "Password is required")
    String password;

    public void setIdentifier(String identifier){this.identifier = identifier; }
    public void setPassword(String password){this.password = password; }
    public String getIdentifier(){return this.identifier; }
    public String getPassword(){return this.password; }

}

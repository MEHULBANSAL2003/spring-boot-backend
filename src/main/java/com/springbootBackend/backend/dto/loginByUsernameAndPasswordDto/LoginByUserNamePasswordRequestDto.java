package com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class LoginByUserNamePasswordRequestDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    String userName;

    @NotBlank(message = "Password is required")
    String password;

    public void setUserName(String userName){this.userName = userName; }
    public void setPassword(String password){this.password = password; }
    public String getUserName(){return this.userName; }
    public String getPassword(){return this.password; }

}

package com.springbootBackend.backend.dto.userMobileSignUpVerificationDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class UserMobileSignupVerificationResponseDto {


    private String status;
    private Long userId;
    private String email;
    private String userName;
    private String countryCode;
    private String phoneNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String authToken;
    private String refreshToken;



    public UserMobileSignupVerificationResponseDto() {}

    public UserMobileSignupVerificationResponseDto(String status,Long userId, String email, String userName, String countryCode, String phoneNumber,LocalDateTime createdAt, LocalDateTime updatedAt, String authToken, String refreshToken) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.authToken = authToken;
        this.refreshToken = refreshToken;
    }


}

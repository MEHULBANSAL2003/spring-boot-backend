package com.springbootBackend.backend.dto.userEmailMobileSignUpVerificationDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserEmailSignupVerificationResponseDto {

    private String status;
    private Long userId;
    private String email;
    private String userName;
    private String countryCode;
    private String phoneNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public UserEmailSignupVerificationResponseDto() {}

    public UserEmailSignupVerificationResponseDto(String status,Long userId, String email, String userName, String countryCode, String phoneNumber,LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

}

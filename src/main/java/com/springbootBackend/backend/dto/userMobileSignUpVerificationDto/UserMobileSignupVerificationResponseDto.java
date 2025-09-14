package com.springbootBackend.backend.dto.userMobileSignUpVerificationDto;

import java.time.LocalDateTime;

public class UserMobileSignupVerificationResponseDto {


    private String status;
    private Long userId;
    private String email;
    private String userName;
    private String countryCode;
    private String phoneNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public UserMobileSignupVerificationResponseDto() {}

    public UserMobileSignupVerificationResponseDto(String status,Long userId, String email, String userName, String countryCode, String phoneNumber,LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public void setStatus(String status){this.status = status; }
    public String getStatus(){return this.status; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }


    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }


    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

}

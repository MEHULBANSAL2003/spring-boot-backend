package com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto;

import com.springbootBackend.backend.entity.UserDataEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class LoginByUserNamePasswordResponseDto {

    private String status;
    private Long userId;
    private String email;
    private String userName;
    private String countryCode;
    private String phoneNumber;
    private Integer age;
    private LocalDate dob;
    private UserDataEntity.Gender gender;
    private String profile_pic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String authToken;
    private String refreshToken;

    public LoginByUserNamePasswordResponseDto() {
    }

    // All-args constructor
    public LoginByUserNamePasswordResponseDto(String status, Long userId, String email, String userName,
                                              String countryCode, String phoneNumber, Integer age, LocalDate dob,
                                              UserDataEntity.Gender gender, String profile_pic, LocalDateTime createdAt,
                                              LocalDateTime updatedAt,String authToken, String refreshToken) {
        this.status = status;
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.dob = dob;
        this.gender = gender;
        this.profile_pic = profile_pic;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authToken= authToken;
        this.refreshToken = refreshToken;

    }
}

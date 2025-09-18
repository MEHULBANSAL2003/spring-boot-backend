package com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto;

import java.time.LocalDateTime;
import java.util.Date;

public class LoginByUserNamePasswordResponseDto {

    private String status;
    private Long userId;
    private String email;
    private String userName;
    private String countryCode;
    private String phoneNumber;
    private int age;
    private Date dob;
    private String gender;
    private String profile_pic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LoginByUserNamePasswordResponseDto() {
    }

    // All-args constructor
    public LoginByUserNamePasswordResponseDto(String status, Long userId, String email, String userName,
                                              String countryCode, String phoneNumber, int age, Date dob,
                                              String gender, String profile_pic, LocalDateTime createdAt,
                                              LocalDateTime updatedAt) {
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
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }



}

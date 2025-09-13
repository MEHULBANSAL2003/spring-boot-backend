package com.springbootBackend.backend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "pending_verification",indexes = {
        @Index(name = "idx_email",columnList = "email"),
        @Index(name="idx_userName", columnList = "userName"),
        @Index(name = "idx_phoneNumber", columnList = "phoneNumber")
})

@org.hibernate.annotations.Check(constraints = "email IS NOT NULL OR phone_number IS NOT NULL")
public class UserPendingVerification {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String countryCode;
@Column(unique = true)
private String phoneNumber;
@Column(unique = true)
private String email;
@Column(unique = true, nullable = false)
private String userName;
private String password;
@Column(length = 6)
private String otp;
private LocalDateTime otpExpiryTime;
private Integer incorrectAttempts;


@CreationTimestamp
private LocalDateTime createdAt;

@UpdateTimestamp
private LocalDateTime updatedAt;

@NotNull
private boolean isTwilioActive;


public enum Status{
    PENDING,ACTIVE
}
@Enumerated(EnumType.STRING)  // store as "PENDING", "ACTIVE"
@Column(nullable = false)
private Status verificationStatus = Status.PENDING; // default value




    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public LocalDateTime getOtpExpiryTime() { return otpExpiryTime; }
    public void setOtpExpiryTime(LocalDateTime otpExpiryTime) { this.otpExpiryTime = otpExpiryTime; }

    public Integer getIncorrectAttempts() { return incorrectAttempts; }
    public void setIncorrectAttempts(Integer incorrectAttempts) { this.incorrectAttempts = incorrectAttempts; }


    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Status getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(Status verificationStatus) { this.verificationStatus = verificationStatus; }

    public boolean getIsTwilioActive() { return isTwilioActive; }
    public void setIsTwilioActive(boolean twilioActive) { isTwilioActive = twilioActive; }

}

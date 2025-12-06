package com.springbootBackend.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class ResetPassword {

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

  private String otp;

  private LocalDateTime otpExpiryTime;

  private boolean otpVerified = false;

  public enum OtpVerifiedBy{
    EMAIL, PHONE
  }
  private OtpVerifiedBy otpVerifiedBy;
}

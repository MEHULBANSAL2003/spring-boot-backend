package com.springbootBackend.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

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

  // --- Getters and Setters ---

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getOtp() {
    return otp;
  }

  public void setOtp(String otp) {
    this.otp = otp;
  }

  public LocalDateTime getOtpExpiryTime() {
    return otpExpiryTime;
  }

  public void setOtpExpiryTime(LocalDateTime otpExpiryTime) {
    this.otpExpiryTime = otpExpiryTime;
  }

  public void setOtpVerified(boolean otpVerified){
    this.otpVerified = otpVerified;
  }

  public boolean getOtpVerified(){
    return this.otpVerified;
  }

  public void setOtpVerifiedBy(OtpVerifiedBy otpVerifiedBy){
    this.otpVerifiedBy = otpVerifiedBy;
  }
  public OtpVerifiedBy getOtpVerifiedBy(){
    return this.otpVerifiedBy;
  }
}

package com.springbootBackend.backend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter

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
private Status verificationStatus = Status.PENDING;

private int otpRequestCount;

private LocalDateTime otpRequestWindowStart;

}

package com.springbootBackend.backend.entity;


import jakarta.persistence.*;
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
public class UserPendingVerification {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
Long id;
String countryCode;
@Column(unique = true)
String phoneNumber;
@Column(unique = true)
String email;
@Column(unique = true, nullable = false)
String userName;
String otp;
LocalTime timeLeft;
String incorrectAttempts;

@CreationTimestamp
private LocalDateTime createdAt;

@UpdateTimestamp
private LocalDateTime updatedAt;


public enum Status{
    PENDING,ACTIVE
}
@Enumerated(EnumType.STRING)  // store as "PENDING", "ACTIVE"
@Column(nullable = false)
private Status verificationStatus = Status.PENDING; // default value

}

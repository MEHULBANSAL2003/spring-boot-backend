package com.springbootBackend.backend.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "user_data",indexes = {
        @Index(name = "idx_email",columnList = "email"),
        @Index(name="idx_userName", columnList = "userName"),
        @Index(name = "idx_phoneNumber", columnList = "phone_number")
})
public class UserDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true)
    private String email;
    private String hashedEmail;
    @Column(unique = true, nullable = false)
    private String userName;
    @Column(nullable = false)
    private String hashedPassword;
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    private Gender gender;
    private Integer age;
    private LocalDate dateOfBirth;
    private String profilePicUrl;

    @Column(length = 10)
    private String countryCode;

    @Column(length = 20, unique = true)
    private String phoneNumber;
    public enum userStatus{
        ACTIVE,INACTIVE,BLOCKED,TEMP_BLOCKED
    };
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private userStatus currStatus=userStatus.ACTIVE;

    private LocalDateTime lastLogin;
  private String ipAddress;
  private String userAgent;

  private int noOfTimePasswordChanged;

  @Enumerated(EnumType.STRING)
  private ResetPassword.OtpVerifiedBy lastPasswordChangeMethod;


    // blocked use case
    private int incorrectAttempts;
    private LocalDateTime incorrectAttemptTimeWindowStart;
    private int blockedCount;
    private LocalDateTime blockedStartTime;
    private LocalDateTime blockedEndTime;
    private long blockForMin;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}

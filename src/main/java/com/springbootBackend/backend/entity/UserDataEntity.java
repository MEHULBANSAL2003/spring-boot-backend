package com.springbootBackend.backend.entity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_data", schema = "UserAuth",indexes = {
        @Index(name = "idx_email",columnList = "email"),
        @Index(name="idx_userName", columnList = "userName")
})
public class UserDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;
    private String hashed_email;
    @Column(unique = true, nullable = false)
    private String userName;
    private String hashed__password;

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    private Gender gender;


    private Integer age;
    private LocalDate date_of_birth;
    private String profile_pic_url;

    @Column(length = 10)
    private String countryCode;

    @Column(length = 20)
    private String phone_number;
    private String curr_status;
    private LocalDateTime last_login;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer failed_attempts;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;

}

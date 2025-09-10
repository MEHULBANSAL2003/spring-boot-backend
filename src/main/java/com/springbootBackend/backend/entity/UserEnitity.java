package com.springbootBackend.backend.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "user_data", schema = "UserAuth",indexes = {
        @Index(name = "idx_email",columnList = "email"),
        @Index(name="idx_userName", columnList = "userName")
})
public class UserEnitity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    @Column(unique = true, nullable = false)
    String email;
    @Column(unique = true, nullable = false)
    String userName;
    String password;
}

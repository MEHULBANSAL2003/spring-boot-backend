package com.springbootBackend.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserDataEntity user;

    @Column(name="refresh_token",nullable = false, unique = true, length = 500)
    private String refreshToken;

    @Column(nullable = false)
    private Instant issuedAt;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked = false;


    public RefreshToken() {}

    public RefreshToken(UserDataEntity user, String refreshToken, Instant issuedAt, Instant expiresAt) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.revoked = false;
    }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserDataEntity getUser() {
    return user;
  }

  public void setUser(UserDataEntity user) {
    this.user = user;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Instant getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(Instant issuedAt) {
    this.issuedAt = issuedAt;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Instant expiresAt) {
    this.expiresAt = expiresAt;
  }

  public boolean isRevoked() {
    return revoked;
  }

  public void setRevoked(boolean revoked) {
    this.revoked = revoked;
  }


}

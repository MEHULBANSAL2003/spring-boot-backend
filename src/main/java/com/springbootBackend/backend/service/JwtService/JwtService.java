package com.springbootBackend.backend.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

  @Value("${jwt.secret-key}")
  private String secretKey;

  @Value("${jwt.access-token-exp-ms}")
  private long accessTokenExpiration;

  @Value("${jwt.refresh-token-exp-ms}")
  private long refreshTokenExpiration;

  private Key getSigningKey() {
    return new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
  }

  public String generateAccessToken(Long userId) {
    return Jwts.builder()
      .claim("userId", userId)
      .claim("token_type", "ACCESS")
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
      .signWith(getSigningKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  public String generateRefreshToken(Long userId) {
    return Jwts.builder()
      .claim("userId", userId)
      .claim("token_type", "REFRESH")
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
      .signWith(getSigningKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  public Long getUserIdFromToken(String token) {
    Claims claims = Jwts.parserBuilder()
      .setSigningKey(getSigningKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
    return claims.get("userId", Long.class);
  }

  public String getTokenType(String token) {
    Claims claims = Jwts.parserBuilder()
      .setSigningKey(getSigningKey())
      .build()
      .parseClaimsJws(token)
      .getBody();

    return claims.get("token_type", String.class);
  }
}

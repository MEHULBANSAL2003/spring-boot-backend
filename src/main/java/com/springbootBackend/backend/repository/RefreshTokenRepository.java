package com.springbootBackend.backend.repository;

import com.springbootBackend.backend.entity.RefreshToken;
import com.springbootBackend.backend.entity.UserDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByJti(String jti);
    List<RefreshToken> findByUserAndRevokedFalse(UserDataEntity user);
    void deleteByUser(UserDataEntity user);
}

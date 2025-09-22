package com.springbootBackend.backend.repository;

import com.springbootBackend.backend.entity.RefreshToken;
import com.springbootBackend.backend.entity.UserDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByRefreshToken(String token);
    List<RefreshToken> findByUserAndRevokedFalse(UserDataEntity user);
    void deleteByUser(UserDataEntity user);
}

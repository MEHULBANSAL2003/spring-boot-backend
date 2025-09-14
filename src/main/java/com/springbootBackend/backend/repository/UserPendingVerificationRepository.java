package com.springbootBackend.backend.repository;

import com.springbootBackend.backend.entity.UserPendingVerification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserPendingVerificationRepository extends JpaRepository<UserPendingVerification,Long> {
    Optional<UserPendingVerification> findByPhoneNumber(String phoneNumber);
    Optional<UserPendingVerification> findByUserName(String userName);

    @Transactional
    int deleteAllByOtpExpiryTimeBefore(LocalDateTime now);

}

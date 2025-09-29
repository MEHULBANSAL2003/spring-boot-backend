package com.springbootBackend.backend.repository;

import com.springbootBackend.backend.entity.UserDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserDataRepository extends JpaRepository<UserDataEntity,Long> {
Optional<UserDataEntity> findByPhoneNumber(String phoneNumber);
    Optional<UserDataEntity> findByUserNameOrPhoneNumber(String userName,String phoneNumber);
    Optional<UserDataEntity> findByUserName(String userName);
    Optional<UserDataEntity> findByUserNameOrEmail(String userName, String email);

    @Query("SELECT u FROM UserDataEntity u WHERE u.userName = :identifier OR u.email = :identifier OR u.phoneNumber = :identifier")
    Optional<UserDataEntity> findByIdentifier(@Param("identifier") String identifier);

    List<UserDataEntity> findByCurrStatusAndLastLoginBefore(UserDataEntity.userStatus status, LocalDateTime lastLogin);
}

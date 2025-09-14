package com.springbootBackend.backend.repository;

import com.springbootBackend.backend.entity.UserDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDataRepository extends JpaRepository<UserDataEntity,Long> {
Optional<UserDataEntity> findByPhoneNumber(String phoneNumber);
    Optional<UserDataEntity> findByUserName(String userName);
}

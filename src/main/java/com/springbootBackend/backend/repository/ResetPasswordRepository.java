package com.springbootBackend.backend.repository;

import com.springbootBackend.backend.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword,Long> {

  Optional<ResetPassword> findByUserName(String userName);

  @Query("SELECT u FROM UserDataEntity u WHERE u.userName = :identifier OR u.email = :identifier OR u.phoneNumber = :identifier")
  Optional<ResetPassword> findByIdentifier(@Param("identifier") String identifier);
}

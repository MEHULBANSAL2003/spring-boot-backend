package com.springbootBackend.backend.repository;

import com.springbootBackend.backend.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword,Long> {

  Optional<ResetPassword> findByUserName(String userName);
}

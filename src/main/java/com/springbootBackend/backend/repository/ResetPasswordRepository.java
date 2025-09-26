package com.springbootBackend.backend.repository;

import com.springbootBackend.backend.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword,Long> {
}

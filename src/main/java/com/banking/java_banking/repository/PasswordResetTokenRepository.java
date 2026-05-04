package com.banking.java_banking.repository;

import com.banking.java_banking.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByEmailAndOtpCodeAndUsedFalse(String email, String otpCode);

    void deleteByEmail(String email);
}
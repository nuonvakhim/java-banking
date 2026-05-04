package com.banking.java_banking.service.impl;

import com.banking.java_banking.dto.EmailDetails;
import com.banking.java_banking.dto.ForgotPasswordRequest;
import com.banking.java_banking.dto.ResetPasswordRequest;
import com.banking.java_banking.entity.PasswordResetToken;
import com.banking.java_banking.entity.User;
import com.banking.java_banking.exception.BadRequestException;
import com.banking.java_banking.repository.PasswordResetTokenRepository;
import com.banking.java_banking.repository.UserRepository;
import com.banking.java_banking.service.EmailService;
import com.banking.java_banking.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final int OTP_EXPIRY_MINUTES = 10;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void initiatePasswordReset(ForgotPasswordRequest request) {
        String email = request.getEmail();

        // Validate that the account exists
        userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("No account found with email: " + email));

        // Remove any previous OTPs for this email before creating a new one
        passwordResetTokenRepository.deleteByEmail(email);

        String otpCode = generateOtp();

        PasswordResetToken token = PasswordResetToken.builder()
                .email(email)
                .otpCode(otpCode)
                .expiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES))
                .used(false)
                .build();

        passwordResetTokenRepository.save(token);

        emailService.sendEmailAlert(EmailDetails.builder()
                .recipient(email)
                .subject("Your Password Reset OTP")
                .messageBody(buildEmailBody(otpCode))
                .build());
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        PasswordResetToken token = passwordResetTokenRepository
                .findByEmailAndOtpCodeAndUsedFalse(request.getEmail(), request.getOtpCode())
                .orElseThrow(() -> new BadRequestException("Invalid OTP code"));

        if (LocalDateTime.now().isAfter(token.getExpiresAt())) {
            throw new BadRequestException("OTP has expired. Please request a new one");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("No account found with this email"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        token.setUsed(true);
        passwordResetTokenRepository.save(token);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private String generateOtp() {
        int otp = 100_000 + SECURE_RANDOM.nextInt(900_000);
        return String.valueOf(otp);
    }

    private String buildEmailBody(String otpCode) {
        return String.format(
                "Hello,%n%n" +
                "You requested a password reset for your banking account.%n%n" +
                "Your OTP code is: %s%n%n" +
                "This code expires in %d minutes. Do not share it with anyone.%n%n" +
                "If you did not request this, please ignore this email.",
                otpCode, OTP_EXPIRY_MINUTES
        );
    }
}
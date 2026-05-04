package com.banking.java_banking.service;

import com.banking.java_banking.dto.ForgotPasswordRequest;
import com.banking.java_banking.dto.ResetPasswordRequest;

public interface PasswordResetService {

    void initiatePasswordReset(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}
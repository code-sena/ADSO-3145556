package com.eduaircontrol.mssecurity.infrastructure.inbound.web;

import com.eduaircontrol.mssecurity.application.AuthService;
import com.eduaircontrol.mssecurity.application.PasswordResetService;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.ChangePasswordRequest;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.DeleteAccountRequest;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.ForgotPasswordRequest;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.ResetPasswordRequest;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.VerifyCodeRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Flujos de contraseña: recuperación por código y cambio/eliminación de cuenta.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordResetService passwordResetService;
    private final AuthService authService;

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.requestCode(request.email());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resend-code")
    public ResponseEntity<Void> resendCode(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.requestCode(request.email());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Void> verifyCode(@Valid @RequestBody VerifyCodeRequest request) {
        passwordResetService.verifyCode(request.email(), request.code());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.email(), request.code(), request.newPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        authService.changePassword(UUID.fromString(authentication.getName()),
                request.currentPassword(), request.newPassword());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount(@Valid @RequestBody DeleteAccountRequest request,
            Authentication authentication) {
        authService.deleteAccount(UUID.fromString(authentication.getName()), request.password());
        return ResponseEntity.noContent().build();
    }
}

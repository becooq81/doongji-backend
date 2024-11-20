package com.find.doongji.auth.controller;

import java.util.Collections;

import com.find.doongji.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.find.doongji.auth.payload.request.LoginRequest;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class BasicAuthController implements AuthController {

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authService.authenticateUser(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(Collections.singletonMap("message", "Login successful"));
    }

    @Override
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Collections.singletonMap("message", "Logout successful"));
    }
    
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}

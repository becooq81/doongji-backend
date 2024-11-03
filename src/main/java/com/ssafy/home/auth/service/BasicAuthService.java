package com.ssafy.home.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.home.auth.payload.request.LoginRequest;
import com.ssafy.home.auth.repository.AuthRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class BasicAuthService implements AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Override
    public boolean login(LoginRequest loginRequest, HttpSession session) {

        String storedPassword = authRepository.getPasswordByUsername(loginRequest.getUsername());

        if (storedPassword != null && storedPassword.equals(loginRequest.getPassword())) {
            session.setAttribute("username", loginRequest.getUsername());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void logout(HttpSession session) {

        if (session.getAttribute("username") == null) {
            throw new SecurityException("User is not logged in");
        }
        session.invalidate();
    }

}

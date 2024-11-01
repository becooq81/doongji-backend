package com.ssafy.home.auth.service;

import com.ssafy.home.auth.payload.request.LoginRequest;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    boolean login(LoginRequest loginRequest, HttpSession session);
    void logout(HttpSession session);
}

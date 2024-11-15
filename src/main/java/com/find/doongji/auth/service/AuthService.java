package com.find.doongji.auth.service;

import com.find.doongji.auth.payload.request.LoginRequest;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    boolean login(LoginRequest loginRequest, HttpSession session);
    void logout(HttpSession session);
}

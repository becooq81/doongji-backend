package com.find.doongji.auth.service;

import com.find.doongji.auth.payload.request.LoginRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {

    Authentication authenticateUser(LoginRequest loginRequest);

    boolean isAuthenticated();
}

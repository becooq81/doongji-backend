package com.ssafy.home.auth.service;

import com.ssafy.home.auth.payload.request.LoginRequest;
import com.ssafy.home.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicAuthService implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean login(LoginRequest loginRequest, HttpSession session) {
        // 입력한 username으로 DB에서 비밀번호를 가져옴
        String storedPassword = userRepository.getPasswordByUsername(loginRequest.getUsername());

        // 비밀번호를 평문으로 비교
        if (storedPassword != null && storedPassword.equals(loginRequest.getPassword())) {
            // 세션에 사용자 이름 저장
            session.setAttribute("username", loginRequest.getUsername());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }
}

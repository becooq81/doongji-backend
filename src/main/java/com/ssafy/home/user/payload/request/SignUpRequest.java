package com.ssafy.home.user.payload.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String name; // 이름 필드 추가
}

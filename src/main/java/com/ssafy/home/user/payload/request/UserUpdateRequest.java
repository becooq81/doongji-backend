package com.ssafy.home.user.payload.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String email;
    private String name;
}

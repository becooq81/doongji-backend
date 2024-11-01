package com.ssafy.home.user.payload.response;

import lombok.Data;

@Data
public class UserSearchResponse {
    private String username;
    private String email;
    private String name;
}

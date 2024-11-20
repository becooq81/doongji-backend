package com.find.doongji.member.payload.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String email;
    private String name;
}

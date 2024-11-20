package com.find.doongji.member.payload.response;

import com.find.doongji.auth.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
    private String username;
    private String email;
    private String password;
    private String name;
    private Role role;
}

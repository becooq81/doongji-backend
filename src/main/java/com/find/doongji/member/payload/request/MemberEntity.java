package com.find.doongji.member.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberEntity {
    private String username;
    private String email;
    private String password;
    private String name;
    private String role;
}

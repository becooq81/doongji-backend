package com.find.doongji.member.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchResponse {
    private String username;
    private String email;
    private String name;
}

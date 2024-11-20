package com.find.doongji.auth.payload.response;

import com.find.doongji.member.payload.request.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AuthUser implements UserDetails {

    private final MemberEntity memberEntity;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
        this.authorities = List.of(new SimpleGrantedAuthority(memberEntity.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return memberEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return memberEntity.getUsername();
    }
}

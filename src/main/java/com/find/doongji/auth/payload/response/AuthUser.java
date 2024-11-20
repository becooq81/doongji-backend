package com.find.doongji.auth.payload.response;

import com.find.doongji.member.payload.response.Member;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AuthUser implements UserDetails {

    private final Member member;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Member member) {
        this.member = member;
        this.authorities = List.of(new SimpleGrantedAuthority(member.getRole().name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }
}

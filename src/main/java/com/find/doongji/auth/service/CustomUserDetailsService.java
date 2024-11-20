package com.find.doongji.auth.service;

import com.find.doongji.auth.payload.response.AuthUser;
import com.find.doongji.member.payload.request.MemberEntity;
import com.find.doongji.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findByUsername(username);
        if (memberEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new AuthUser(memberEntity);
    }
}

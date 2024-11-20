package com.find.doongji.member.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.find.doongji.auth.enums.Role;
import com.find.doongji.member.payload.request.SignUpRequest;
import com.find.doongji.member.payload.request.MemberUpdateRequest;
import com.find.doongji.member.payload.request.MemberEntity;
import com.find.doongji.member.payload.response.MemberResponse;
import com.find.doongji.member.payload.response.MemberSearchResponse;
import com.find.doongji.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BasicMemberService implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerMember(SignUpRequest signUpRequest) {
        validateSignUpRequest(signUpRequest);

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        MemberEntity memberEntity = MemberEntity.builder()
                .username(signUpRequest.getUsername())
                .password(encodedPassword)
                .email(signUpRequest.getEmail())
                .name(signUpRequest.getName())
                .role(signUpRequest.getRole() != null ? signUpRequest.getRole().getKey() : Role.ROLE_USER.getKey())
                .build();

        System.out.println(memberEntity.getRole());

        memberRepository.insertMember(memberEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMemberProfile() {
        String currentUsername = getAuthenticatedUsername();
        MemberEntity memberEntity =  memberRepository.findByUsername(currentUsername);
        return MemberResponse.builder()
                .username(memberEntity.getUsername())
                .email(memberEntity.getEmail())
                .name(memberEntity.getName())
                .build();
    }

    @Override
    @Transactional
    public void updateMemberProfile(MemberUpdateRequest memberUpdateRequest) {
        String currentUsername = getAuthenticatedUsername();

        memberRepository.updateMember(currentUsername, memberUpdateRequest);
    }

    @Override
    @Transactional
    public void deleteMemberProfile() {
        String currentUsername = getAuthenticatedUsername();
        memberRepository.deleteMember(currentUsername);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberSearchResponse> searchMembers(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            throw new IllegalArgumentException("Invalid query parameter");
        }

        List<MemberSearchResponse> users = memberRepository.searchMembers(keyword);
        if (users.isEmpty()) {
            throw new NoSuchElementException("No users found with the given keyword");
        }

        return users;
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User not authenticated");
        }
        return authentication.getName();
    }

    private void validateSignUpRequest(SignUpRequest signUpRequest) {
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (memberRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

}

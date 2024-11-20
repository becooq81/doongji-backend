package com.find.doongji.member.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.find.doongji.auth.enums.Role;
import com.find.doongji.member.payload.request.SignUpRequest;
import com.find.doongji.member.payload.request.MemberUpdateRequest;
import com.find.doongji.member.payload.response.Member;
import com.find.doongji.member.payload.response.MemberResponse;
import com.find.doongji.member.payload.response.MemberSearchResponse;
import com.find.doongji.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BasicMemberService implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(SignUpRequest signUpRequest) {
        validateSignUpRequest(signUpRequest);

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        Member member = Member.builder()
                .username(signUpRequest.getUsername())
                .password(encodedPassword)
                .email(signUpRequest.getEmail())
                .name(signUpRequest.getName())
                .role(Role.ROLE_USER)
                .build();

        memberRepository.insertUser(member);
    }

    @Override
    public MemberResponse getUserProfile() {
        String currentUsername = getAuthenticatedUsername();
        Member member =  memberRepository.findByUsername(currentUsername);
        return MemberResponse.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }

    @Override
    public void updateUserProfile(MemberUpdateRequest memberUpdateRequest) {
        String currentUsername = getAuthenticatedUsername();

        memberRepository.updateUser(currentUsername, memberUpdateRequest);
    }

    @Override
    public void deleteUserProfile() {
        String currentUsername = getAuthenticatedUsername();
        memberRepository.deleteUser(currentUsername);
    }

    @Override
    public List<MemberSearchResponse> searchUsers(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            throw new IllegalArgumentException("Invalid query parameter");
        }

        List<MemberSearchResponse> users = memberRepository.searchUsers(keyword);
        if (users.isEmpty()) {
            throw new NoSuchElementException("No users found with the given keyword");
        }

        return users;
    }

    // Helper methods

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

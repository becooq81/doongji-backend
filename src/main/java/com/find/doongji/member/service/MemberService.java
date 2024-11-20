package com.find.doongji.member.service;

import java.util.List;

import com.find.doongji.member.payload.request.SignUpRequest;
import com.find.doongji.member.payload.request.MemberUpdateRequest;
import com.find.doongji.member.payload.response.Member;
import com.find.doongji.member.payload.response.MemberResponse;
import com.find.doongji.member.payload.response.MemberSearchResponse;

import jakarta.servlet.http.HttpSession;

public interface MemberService {
	
    void registerUser(SignUpRequest signUpRequest);
    
    MemberResponse getUserProfile();
    
    void updateUserProfile(MemberUpdateRequest memberUpdateRequest);
    
    void deleteUserProfile();
    
    List<MemberSearchResponse> searchUsers(String keyword);
}

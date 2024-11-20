package com.find.doongji.member.controller;

import java.util.List;

import com.find.doongji.member.payload.response.MemberResponse;
import org.springframework.http.ResponseEntity;

import com.find.doongji.member.payload.request.SignUpRequest;
import com.find.doongji.member.payload.request.MemberUpdateRequest;
import com.find.doongji.member.payload.response.MemberSearchResponse;

public interface MemberController {
	
    ResponseEntity<?> registerMember(SignUpRequest signUpRequest);
    
    ResponseEntity<MemberResponse> getMemberProfile();
    
    ResponseEntity<?> updateMemberProfile(MemberUpdateRequest memberUpdateRequest);
    
    ResponseEntity<?> deleteMemberProfile();

    ResponseEntity<List<MemberSearchResponse>> searchMembers(String keyword);
}

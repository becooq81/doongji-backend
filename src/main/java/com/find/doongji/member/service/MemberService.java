package com.find.doongji.member.service;

import java.util.List;

import com.find.doongji.member.payload.request.SignUpRequest;
import com.find.doongji.member.payload.request.MemberUpdateRequest;
import com.find.doongji.member.payload.response.MemberResponse;
import com.find.doongji.member.payload.response.MemberSearchResponse;

public interface MemberService {
	
    void registerMember(SignUpRequest signUpRequest);
    
    MemberResponse getMemberProfile();
    
    void updateMemberProfile(MemberUpdateRequest memberUpdateRequest);
    
    void deleteMemberProfile();
    
    List<MemberSearchResponse> searchMembers(String keyword);
}

package com.find.doongji.member.service;

import com.find.doongji.member.payload.request.MemberUpdateRequest;
import com.find.doongji.member.payload.request.SignUpRequest;
import com.find.doongji.member.payload.response.MemberResponse;
import com.find.doongji.member.payload.response.MemberSearchResponse;

import java.util.List;

public interface MemberService {

    void registerMember(SignUpRequest signUpRequest);

    MemberResponse getMemberProfile();

    void updateMemberProfile(MemberUpdateRequest memberUpdateRequest);

    void deleteMemberProfile();

    List<MemberSearchResponse> searchMembers(String keyword);
}

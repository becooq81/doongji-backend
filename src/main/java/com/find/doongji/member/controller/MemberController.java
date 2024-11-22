package com.find.doongji.member.controller;

import com.find.doongji.member.payload.request.MemberUpdateRequest;
import com.find.doongji.member.payload.request.SignUpRequest;
import com.find.doongji.member.payload.response.MemberResponse;
import com.find.doongji.member.payload.response.MemberSearchResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MemberController {

    ResponseEntity<?> registerMember(SignUpRequest signUpRequest);

    ResponseEntity<MemberResponse> getMemberProfile();

    ResponseEntity<?> updateMemberProfile(MemberUpdateRequest memberUpdateRequest);

    ResponseEntity<?> deleteMemberProfile();

    ResponseEntity<List<MemberSearchResponse>> searchMembers(String keyword);
}

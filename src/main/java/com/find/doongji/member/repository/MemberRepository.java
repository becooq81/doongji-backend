package com.find.doongji.member.repository;

import com.find.doongji.member.payload.request.MemberEntity;
import com.find.doongji.member.payload.request.MemberUpdateRequest;
import com.find.doongji.member.payload.response.MemberSearchResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberRepository {

    // 사용자 이름으로 사용자 찾기
    MemberEntity findByUsername(@Param("username") String username);


    // 새로운 사용자 추가
    int insertMember(MemberEntity memberEntity);

    // 사용자 정보 업데이트
    int updateMember(@Param("username") String username, @Param("request") MemberUpdateRequest memberUpdateRequest);

    // 사용자 삭제
    int deleteMember(@Param("username") String username);

    // 사용자 검색 (사용자 이름 또는 이름으로)
    List<MemberSearchResponse> searchMembers(@Param("keyword") String keyword);

    // 사용자 이름으로 사용자 존재 여부 확인
    boolean existsByUsername(@Param("username") String username);

    // 사용자 이메일로 사용자 존재 여부 확인
    boolean existsByEmail(@Param("email") String email);
}


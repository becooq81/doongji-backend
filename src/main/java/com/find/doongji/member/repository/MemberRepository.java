package com.find.doongji.member.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.find.doongji.member.payload.request.SignUpRequest;
import com.find.doongji.member.payload.request.MemberUpdateRequest;
import com.find.doongji.member.payload.response.Member;
import com.find.doongji.member.payload.response.MemberSearchResponse;

@Mapper
public interface MemberRepository {

    // 사용자 이름으로 사용자 찾기
    Member findByUsername(@Param("username") String username);

    // 사용자 이름 중복 확인
    int countByUsername(@Param("username") String username);

    // 사용자 이메일 중복 확인
    int countByEmail(@Param("email") String email);

    // 새로운 사용자 추가
    int insertUser(Member member);

    // 사용자 정보 업데이트
    int updateUser(@Param("username") String username, @Param("request") MemberUpdateRequest memberUpdateRequest);

    // 사용자 삭제
    int deleteUser(@Param("username") String username);

    // 사용자 검색 (사용자 이름 또는 이름으로)
    List<MemberSearchResponse> searchUsers(@Param("keyword") String keyword);

    // 사용자 이름으로 사용자 존재 여부 확인
    boolean existsByUsername(@Param("username") String username);

    // 사용자 이메일로 사용자 존재 여부 확인
    boolean existsByEmail(@Param("email") String email);
}


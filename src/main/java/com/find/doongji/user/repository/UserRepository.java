package com.find.doongji.user.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.find.doongji.user.payload.request.SignUpRequest;
import com.find.doongji.user.payload.request.UserUpdateRequest;
import com.find.doongji.user.payload.response.UserResponse;
import com.find.doongji.user.payload.response.UserSearchResponse;

@Mapper
public interface UserRepository {

    // 사용자 이름으로 사용자 찾기
    UserResponse findByUsername(@Param("username") String username);

    // 사용자 이름 중복 확인
    int countByUsername(@Param("username") String username);

    // 사용자 이메일 중복 확인
    int countByEmail(@Param("email") String email);

    // 새로운 사용자 추가
    int insertUser(SignUpRequest signUpRequest);

    // 사용자 정보 업데이트
    int updateUser(@Param("username") String username, @Param("request") UserUpdateRequest userUpdateRequest);

    // 사용자 삭제
    int deleteUser(@Param("username") String username);

    // 사용자 검색 (사용자 이름 또는 이름으로)
    List<UserSearchResponse> searchUsers(@Param("keyword") String keyword);
}


package com.find.doongji.member.repository;

import com.find.doongji.auth.enums.Role;
import com.find.doongji.member.payload.response.MemberResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.find.doongji.member.payload.request.MemberEntity;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@Transactional
public class MemberEntityTest {
    
    @Autowired
    MemberRepository repo;
    
    @Test
    public void registTest() {

        MemberEntity memberEntity = MemberEntity.builder()
                        .username("kim")
                        .email("kim@naver.com")
                        .name("김싸피")
                        .role(Role.ROLE_USER.getKey())
                        .password("1234")
                        .build();
        repo.insertMember(memberEntity);

        MemberEntity registeredUser = repo.findEntityByUsername("kim");

        Assertions.assertNotNull(registeredUser, "User should be registered successfully");
        Assertions.assertEquals("kim", registeredUser.getUsername());
        Assertions.assertEquals("kim@naver.com", registeredUser.getEmail());
        Assertions.assertEquals("김싸피", registeredUser.getName());
        
        log.info("User registration test passed for user: {}", registeredUser.getUsername());
    }
}

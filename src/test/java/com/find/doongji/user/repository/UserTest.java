package com.find.doongji.user.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.find.doongji.user.payload.request.SignUpRequest;
import com.find.doongji.user.payload.response.UserResponse;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class UserTest {
    
    @Autowired
    UserRepository repo;
    
    @Test
    @Transactional
    public void registTest() {

        SignUpRequest newUser = new SignUpRequest(
                "kim",
                "kim@naver.com",
                "0000",
                "0000",
                "김싸피"
        );

        repo.insertUser(newUser);
        
        UserResponse registeredUser = repo.findByUsername("kim");
        
        Assertions.assertNotNull(registeredUser, "User should be registered successfully");
        Assertions.assertEquals("kim", registeredUser.getUsername());
        Assertions.assertEquals("kim@naver.com", registeredUser.getEmail());
        Assertions.assertEquals("김싸피", registeredUser.getName());
        
        log.info("User registration test passed for user: {}", registeredUser.getUsername());
    }
}

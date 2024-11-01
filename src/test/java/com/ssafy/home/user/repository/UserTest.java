package com.ssafy.home.user.repository;

import com.ssafy.home.user.payload.request.SignUpRequest;
import com.ssafy.home.user.payload.response.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class UserTest {
    
    @Autowired
    UserRepository repo;
    
    @Test
    @Transactional
    public void registTest() {

        SignUpRequest newUser = new SignUpRequest();
        newUser.setUsername("kim");
        newUser.setEmail("kim@naver.com");
        newUser.setPassword("0000");
        newUser.setConfirmPassword("0000");
        newUser.setName("김싸피");
        
        repo.insertUser(newUser);
        
        UserResponse registeredUser = repo.findByUsername("kim");
        
        Assertions.assertNotNull(registeredUser, "User should be registered successfully");
        Assertions.assertEquals("kim", registeredUser.getUsername());
        Assertions.assertEquals("kim@naver.com", registeredUser.getEmail());
        Assertions.assertEquals("김싸피", registeredUser.getName());
        
        log.info("User registration test passed for user: {}", registeredUser.getUsername());
    }
}

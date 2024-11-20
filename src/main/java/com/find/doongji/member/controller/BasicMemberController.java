package com.find.doongji.member.controller;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import com.find.doongji.member.payload.response.MemberResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.find.doongji.member.payload.request.SignUpRequest;
import com.find.doongji.member.payload.request.MemberUpdateRequest;
import com.find.doongji.member.payload.response.Member;
import com.find.doongji.member.payload.response.MemberSearchResponse;
import com.find.doongji.member.service.MemberService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/member")
public class BasicMemberController implements MemberController {

    @Autowired
    private MemberService userService;

    @Override
    @PostMapping
    public ResponseEntity<?> registerMember(@Valid @RequestBody SignUpRequest signUpRequest) {
        userService.registerUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", "User registered successfully"));
    }

    @Override
    @GetMapping("/my")
    public ResponseEntity<MemberResponse> getMemberProfile() {
        MemberResponse member = userService.getUserProfile();
        return ResponseEntity.ok(member);
    }

    @Override
    @PutMapping("/my")
    public ResponseEntity<?> updateMemberProfile(@RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {
        userService.updateUserProfile(memberUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap("message", "User profile updated successfully"));    
    }

    @Override
    @DeleteMapping("/my")
    public ResponseEntity<?> deleteMemberProfile() {
        userService.deleteUserProfile();
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<List<MemberSearchResponse>> searchMembers(@RequestParam String keyword) {
        List<MemberSearchResponse> users = userService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }
    
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex) {
    	return ResponseEntity.status(HttpStatus.NOT_FOUND)
    			.body(Collections.singletonMap("message", ex.getMessage()));
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    			.body(Collections.singletonMap("message", ex.getMessage()));
    }
    
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> handleSecurityException(SecurityException ex) {
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    			.body(Collections.singletonMap("message", ex.getMessage()));
    }
}

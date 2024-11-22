package com.find.doongji.member.controller;

import com.find.doongji.member.payload.request.MemberUpdateRequest;
import com.find.doongji.member.payload.request.SignUpRequest;
import com.find.doongji.member.payload.response.MemberResponse;
import com.find.doongji.member.payload.response.MemberSearchResponse;
import com.find.doongji.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class BasicMemberController implements MemberController {

    private final MemberService memberService;

    @Override
    @PostMapping
    public ResponseEntity<?> registerMember(@Valid @RequestBody SignUpRequest signUpRequest) {
        memberService.registerMember(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", "User registered successfully"));
    }

    @Override
    @GetMapping("/my")
    public ResponseEntity<MemberResponse> getMemberProfile() {
        MemberResponse member = memberService.getMemberProfile();
        return ResponseEntity.ok(member);
    }

    @Override
    @PutMapping("/my")
    public ResponseEntity<?> updateMemberProfile(@RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {
        memberService.updateMemberProfile(memberUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap("message", "User profile updated successfully"));
    }

    @Override
    @DeleteMapping("/my")
    public ResponseEntity<?> deleteMemberProfile() {
        memberService.deleteMemberProfile();
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<List<MemberSearchResponse>> searchMembers(@RequestParam String keyword) {
        List<MemberSearchResponse> users = memberService.searchMembers(keyword);
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

package com.find.doongji.history.controller;

import com.find.doongji.history.payload.response.HistoryResponse;
import com.find.doongji.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/history")
@RequiredArgsConstructor
public class BasicHistoryController implements HistoryController {

    private final HistoryService service;

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<?> getHistoryByUsername() {
        try {
            List<HistoryResponse> searchHistories = service.getAllHistory();
            return ResponseEntity.ok(searchHistories);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("error", "Access Denied: " + ex.getMessage()));

        }
    }

}

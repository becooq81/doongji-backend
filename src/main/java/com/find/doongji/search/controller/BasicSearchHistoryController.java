package com.find.doongji.search.controller;

import com.find.doongji.search.payload.request.SearchHistoryRequest;
import com.find.doongji.search.payload.response.SearchHistoryResponse;
import com.find.doongji.search.service.SearchHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/search")
@RequiredArgsConstructor
public class BasicSearchHistoryController implements SearchHistoryController{

    private final SearchHistoryService service;

    @Override
    @PostMapping("/{username}")
    public ResponseEntity<?> addSearchHistory(@PathVariable String username,
                                              @RequestBody @Valid SearchHistoryRequest searchHistoryRequest) {
        service.addSearchHistory(searchHistoryRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{username}")
    public ResponseEntity<?> getSearchHistoryByUsername(@PathVariable String username) {
        List<SearchHistoryResponse> searchHistories = service.getSearchHistory(username);
        return new ResponseEntity<>(searchHistories, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteSearchHistoryById(Long id) {
        service.removeSearchHistory(id);
        return ResponseEntity.ok().build();
    }
}

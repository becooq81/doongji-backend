package com.find.doongji.search.controller;

import com.find.doongji.search.client.RecommendClient;
import com.find.doongji.search.payload.request.SearchHistoryRequest;
import com.find.doongji.search.payload.request.SearchRequest;
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
public class BasicSearchController implements SearchController {

    private final SearchHistoryService service;
    private final RecommendClient client;


    @Override
    public ResponseEntity<?> search(@RequestBody @Valid SearchRequest request) {
        return null;
    }

    @Override
    @GetMapping("/{username}")
    public ResponseEntity<List<SearchHistoryResponse>> getSearchHistoryByUsername(@PathVariable String username) {
        List<SearchHistoryResponse> searchHistories = service.getSearchHistory(username);
        return new ResponseEntity<>(searchHistories, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{username}/{id}")
    public ResponseEntity<Void> deleteSearchHistoryById(@PathVariable String username, @PathVariable Long id) {
        service.removeSearchHistory(username, id);
        return ResponseEntity.ok().build();
    }
}

package com.find.doongji.search.controller;

import com.find.doongji.search.payload.request.SearchRequest;
import com.find.doongji.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/search")
@RequiredArgsConstructor
public class BasicSearchController {

    private final SearchService searchService;

    @PostMapping
    public ResponseEntity<?> search(@RequestBody SearchRequest searchRequest) {
        try {
            return ResponseEntity.ok(searchService.search(searchRequest));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("An error occurred while processing the search request.");
        }

    }

}

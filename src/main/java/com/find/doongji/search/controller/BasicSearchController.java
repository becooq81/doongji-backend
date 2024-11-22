package com.find.doongji.search.controller;

import com.find.doongji.search.payload.request.SearchRequest;
import com.find.doongji.search.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class BasicSearchController {

    private final SearchService searchService;

    @PostMapping
    public ResponseEntity<?> search(@Valid @RequestBody SearchRequest searchRequest) {
        try {
            return ResponseEntity.ok(searchService.search(searchRequest));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("An error occurred while processing the search request.");
        }

    }

    @GetMapping("/result")
    public ResponseEntity<?> getAptDetail(@RequestParam("aptSeq") String aptSeq) {
        try {
            return ResponseEntity.ok(searchService.viewSearched(aptSeq));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("An error occurred while processing the search request.");
        }
    }

}

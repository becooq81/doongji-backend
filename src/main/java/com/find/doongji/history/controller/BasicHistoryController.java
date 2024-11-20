package com.find.doongji.history.controller;

import com.find.doongji.history.payload.response.HistoryResponse;
import com.find.doongji.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/search")
@RequiredArgsConstructor
public class BasicHistoryController implements HistoryController {

    private final HistoryService service;

    @Override
    @GetMapping("/{username}")
    public ResponseEntity<List<HistoryResponse>> getHistoryByUsername(@PathVariable String username) {
        List<HistoryResponse> searchHistories = service.getAllHistory(username);
        return new ResponseEntity<>(searchHistories, HttpStatus.OK);
    }

}

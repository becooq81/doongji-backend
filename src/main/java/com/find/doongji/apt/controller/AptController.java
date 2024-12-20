package com.find.doongji.apt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


public interface AptController {

    @GetMapping("/search/dong")
    ResponseEntity<?> getAptDealByDong(@RequestParam("sido") String sido, @RequestParam("gugun") String gugun, @RequestParam("dong") String dong);

    @GetMapping("/search/name")
    ResponseEntity<?> getAptDealByName(@RequestParam("keyword") String keyword);
}

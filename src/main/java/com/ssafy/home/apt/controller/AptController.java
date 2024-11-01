package com.ssafy.home.apt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssafy.home.apt.payload.response.AptDeal;

import java.util.List;


public interface AptController {

	@GetMapping("/search/dong")
	public ResponseEntity<?> getAptDealByDong(@RequestParam("sido") String sido, @RequestParam("gugun") String gugun, @RequestParam("dong") String dong);
	
	@GetMapping("/search/name")
	public ResponseEntity<?> getAptDealByName(@RequestParam("keyword") String keyword);
}

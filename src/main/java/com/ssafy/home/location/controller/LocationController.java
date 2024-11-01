package com.ssafy.home.location.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssafy.home.location.payload.response.DongCode;
import com.ssafy.home.location.payload.response.DongList;
import com.ssafy.home.location.payload.response.GugunList;
import com.ssafy.home.location.payload.response.SidoList;

public interface LocationController {

	@GetMapping("/gugun")
	ResponseEntity<GugunList> getGugunList(@RequestParam("sidoName") String sidoName);

	@GetMapping("/dong")
	ResponseEntity<DongList> getDongList(@RequestParam("sidoName") String sidoName, @RequestParam("gugunName") String gugunName);

	
	@GetMapping("/dongcode")
	ResponseEntity<DongCode> getDongCode(@RequestParam("sidoName") String sidoName, @RequestParam("gugunName") String gugunName, @RequestParam("dongName") String dongName);
	
	@GetMapping("/sido")
	ResponseEntity<SidoList> getSidoList();
	
}

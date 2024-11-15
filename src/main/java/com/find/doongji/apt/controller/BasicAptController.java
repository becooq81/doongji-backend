package com.find.doongji.apt.controller;

import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.find.doongji.apt.payload.response.AptDeal;
import com.find.doongji.apt.service.AptService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/apt")
@RestController
@RequiredArgsConstructor
public class BasicAptController implements AptController {
	
	private final AptService service;

	@Override
	public ResponseEntity<?> getAptDealByDong(String sido, String gugun, String dong) {
		try {
			List<AptDeal> aptDeals = service.findAptDealByDong(sido, gugun, dong);
			return new ResponseEntity<>(aptDeals, HttpStatus.OK);
		} catch (NotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No apartment deals found: " + e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
	    }
	}

	@Override
	public ResponseEntity<?> getAptDealByName(String keyword) {
		try {
			List<AptDeal> aptDeals = service.findAptDealByAptNm(keyword);
			return ResponseEntity.ok(aptDeals);
		} catch (NotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No apartment deals found for the keyword: " + e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
	    }
	}

}

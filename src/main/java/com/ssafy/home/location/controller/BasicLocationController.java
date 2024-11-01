package com.ssafy.home.location.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.location.payload.response.DongCode;
import com.ssafy.home.location.payload.response.DongList;
import com.ssafy.home.location.payload.response.GugunList;
import com.ssafy.home.location.service.LocationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/location")
@RestController
public class BasicLocationController implements LocationController {

	private final LocationService service;

	@Override
	public ResponseEntity<GugunList> getGugunList(String sidoName) {
		GugunList gugunList = service.findGugunBySi(sidoName);
		return new ResponseEntity<>(gugunList, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<DongList> getDongList(String sidoName, String gugunName) {
		DongList dongList = service.findDongBySiGugun(sidoName, gugunName);
		return new ResponseEntity<>(dongList, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<DongCode> getDongCode(String sidoName, String gugunName, String dongName) {
		DongCode dongcode = service.findDongCode(sidoName, gugunName, dongName);
		return new ResponseEntity<>(dongcode, HttpStatus.OK);
	}

	
	

}

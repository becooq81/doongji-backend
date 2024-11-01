package com.ssafy.home.location.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/location")
@RestController
public class BasicLocationController implements LocationController {

	@Override
	public List<String> getGugunList(String sidoName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDongList(String sidoName, String gugunName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDongCode(String sidoName, String gugunName, String dongName) {
		// TODO Auto-generated method stub
		return null;
	}

}

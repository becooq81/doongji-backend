package com.ssafy.home.location.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.home.location.payload.response.DongCode;
import com.ssafy.home.location.payload.response.DongList;
import com.ssafy.home.location.payload.response.GugunList;
import com.ssafy.home.location.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicLocationService implements LocationService {

	private final LocationRepository repo;
	
	/**
	 * 시, 군구에 부합하는 모든 동을 찾는다
	 */
	@Override
	public DongList findDongBySiGugun(String si, String gugun) {
		List<String> dongNames = repo.selectDong(si, gugun);
		DongList dongList = DongList.builder()
				.si(si)
				.gugun(gugun)
				.dongList(dongNames)
				.build();
		return dongList;
	}

	/**
	 * 시에 부합하는 모든 구군을 찾는다
	 */
	@Override
	public GugunList findGugunBySi(String si) {
		List<String> gugunNames = repo.selectGugun(si);
		GugunList gugunList = GugunList.builder()
				.si(si)
				.gugunList(gugunNames)
				.build();
		return gugunList;
	}

	/**
	 * 시, 구군, 동에 부합하는 동코드를 찾는다
	 */
	@Override
	public DongCode findDongCode(String si, String gugun, String dong) {
		String dongcodeVal = repo.selectDongCode(si, gugun, dong);
		DongCode dongcode = DongCode.builder()
				.si(si)
				.gugun(gugun)
				.dong(dong)
				.dongcode(dongcodeVal)
				.build();
		return dongcode;
	}

}

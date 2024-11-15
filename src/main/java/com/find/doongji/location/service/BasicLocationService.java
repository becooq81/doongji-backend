package com.find.doongji.location.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.home.location.payload.response.DongCode;
import com.find.doongji.location.payload.response.DongList;
import com.find.doongji.location.payload.response.GugunList;
import com.find.doongji.location.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicLocationService implements LocationService {

	private final LocationRepository repo;
	
	/**
	 * 시, 군구에 부합하는 모든 동을 찾는다
	 */
	@Override
	public DongList findDongBySiGugun(String sido, String gugun) {
		List<String> dongNames = repo.selectDong(sido, gugun);
		DongList dongList = DongList.builder()
				.sido(sido)
				.gugun(gugun)
				.dongList(dongNames)
				.build();
		return dongList;
	}

	/**
	 * 시에 부합하는 모든 구군을 찾는다
	 */
	@Override
	public GugunList findGugunBySi(String sido) {
		List<String> gugunNames = repo.selectGugun(sido);
		GugunList gugunList = GugunList.builder()
				.sido(si)
				.gugunList(gugunNames)
				.build();
		return gugunList;
	}

	/**
	 * 시, 구군, 동에 부합하는 동코드를 찾는다
	 */
	@Override
	public DongCode findDongCode(String sido, String gugun, String dong) {
		String dongcodeVal = repo.selectDongCode(sido, gugun, dong);
		DongCode dongcode = DongCode.builder()
				.sido(sido)
				.gugun(gugun)
				.dong(dong)
				.dongcode(dongcodeVal)
				.build();
		return dongcode;
	}

}

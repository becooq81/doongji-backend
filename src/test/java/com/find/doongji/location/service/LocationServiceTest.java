package com.find.doongji.location.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.home.location.payload.response.DongCode;
import com.find.doongji.location.payload.response.DongList;
import com.find.doongji.location.payload.response.GugunList;
import com.find.doongji.location.repository.LocationRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class LocationServiceTest {
	
	@Autowired
	LocationService service;
	
	@Autowired
	LocationRepository repo;
	
	/**
	 * 시, 군구에 부합하는 모든 동을 찾는다
	 */
	@Test
	public void findDongBySiGugunTest() {
		String si = "서울특별시";
		String gugun = "종로구";
		int expectedCount = 87;

		DongList result = service.findDongBySiGugun(si, gugun);
		Assertions.assertEquals(result.getSi(), si);
		Assertions.assertEquals(result.getGugun(), gugun);
		Assertions.assertEquals(result.getDongList().size(), expectedCount);

	}
	
	/**
	 * 시에 부합하는 모든 구군을 찾는다
	 */
	@Test
	public void findGugunBySiTest() {
		String si = "서울특별시";
		int expectedCount = 25;
		
		GugunList result = service.findGugunBySi(si);
		Assertions.assertEquals(result.getSi(), si);
		Assertions.assertEquals(result.getGugunList().size(), expectedCount);
	}
	
	/**
	 * 시, 구군, 동에 부합하는 동코드를 찾는다
	 */
	@Test
	public void findDongCodeTest() {
		String si = "서울특별시";
		String gugun = "종로구";
		String dong = "궁정동";
		String expected = "1111010300";
		
		DongCode result = service.findDongCode(si, gugun, dong);
		Assertions.assertEquals(result.getSi(), si);
		Assertions.assertEquals(result.getGugun(), gugun);
		Assertions.assertEquals(result.getDong(), dong);
		Assertions.assertEquals(result.getDongcode(), expected);

	}
}

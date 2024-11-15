package com.find.doongji.apt.service;

import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;


import com.find.doongji.apt.payload.response.AptDeal;
import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.repository.AptRepository;
import com.find.doongji.location.service.LocationService;

@SpringBootTest
public class AptServiceTest {

	@Autowired
	AptService aptService;
	
	@Autowired
	AptRepository repo;
	
	@Autowired
	LocationService locationService;
	
	@Test
	@DisplayName("아파트 명으로 아파트 거래내역 찾기 테스트")
	public void findAptDealByAptNmTest() throws NotFoundException {
		String aptNm = "일우";
		int expectedCount = 3015;

		List<AptDeal> result = aptService.findAptDealByAptNm(aptNm);
		Assertions.assertEquals(result.size(), expectedCount);
	}
	
	@Test
	@DisplayName("아파트 명으로 아파트 거래내역 찾기 실패 테스트")
	public void findAptDealByAptNmEmptyTest() throws NotFoundException {
		String aptNm = "dummy";
		int expectedResult = 0;
		
		NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
	        aptService.findAptDealByAptNm(aptNm);
	    });		
	    Assertions.assertEquals("Apt deal list is empty for apt name: " + aptNm, thrown.getMessage());
	}
	
	@Test
	@DisplayName("아파트 번호로 아파트 정보 찾기 테스트")
	public void findAptInfoByAptSeqTest() throws NotFoundException {
		String aptSeq = "11110-100";
		String expectedAptNm = "MID그린(7동)";
		
		AptInfo result = aptService.findAptInfoByAptSeq(aptSeq);
		Assertions.assertEquals(expectedAptNm, result.getAptNm());

	}
	
	@Test
	@DisplayName("아파트 번호로 아파트 정보 찾기 실패 테스트")
	public void findAptInfoByAptSeqFailTest() throws NotFoundException {
		String aptSeq = "dummy";
		
		NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
	        aptService.findAptInfoByAptSeq(aptSeq);
	    });		
	}
	@Test
	@DisplayName("동으로 아파트 거래내역 찾기 테스트")
	public void findAptDealByDongTest() throws NotFoundException {
		String sido = "서울특별시";
		String gugun = "종로구";
		String dong = "무악동";
		int expectedCount = 1271;
		
		List<AptDeal> result = aptService.findAptDealByDong(sido, gugun, dong);
		Assertions.assertEquals(expectedCount, result.size());
	}
	
	@Test
	@DisplayName("동으로 아파트 거래내역 찾기 실패 테스트")
	public void findAptDealByDongFailTest() throws NotFoundException {
		String sido = "dummy";
		String gugun = "dummy";
		String dong = "dummy";
		int expectedCount = 0;
		NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
	        aptService.findAptDealByDong(sido, gugun, dong);
	    });	
	    Assertions.assertEquals("DongCode not found for sido: " + sido + ", gugun: " + gugun + ", dong: " + dong, thrown.getMessage());

	}
	
}

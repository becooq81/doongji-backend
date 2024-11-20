package com.find.doongji.apt.repository;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.find.doongji.apt.payload.response.AptDeal;
import com.find.doongji.apt.payload.response.AptInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
public class AptRepositoryTest {

	@Autowired
	AptRepository repo;
	
	/*
	 * 아파트 번호로 아파트 거래 내역 조회 
	 */
	@Test
	public void selectAptDealByAptSeqTest() {
		String aptSeq = "11110-100";
		int expectedCount = 7;
		
		List<AptDeal> result = repo.selectAptDealByAptSeq(aptSeq);
		Assertions.assertEquals(result.size(), expectedCount);
	}
	
	/*
	 * 동으로 아파트 거래 내역 조회
	 */
	@Test
	public void selectAptDealByDongTest() {
		String dongcode = "1111012000";
		String sggCd = dongcode.substring(0, 5);
		String umdCd = dongcode.substring(5);
		int expectedCount = 15;
		
		List<AptDeal> result = repo.selectAptDealByDong(sggCd, umdCd);
		Assertions.assertEquals(result.size(), expectedCount);
	}
	
	/*
	 * 아파트 명으로 아파트 번호 조회
	 */
	@Test
	public void selectAptSeqByAptNmTest() {
		String aptNm = "일우";
		int expectedCount = 5;
		
		List<String> result = repo.selectAptSeqByAptNm(aptNm);
		Assertions.assertEquals(result.size(), expectedCount);
	}
	
	/*
	 * 아파트 번호로 아파트 거래 내역 조회 
	 */
	@Test
	public void selectAptInfoByAptSeqTest() {
		String aptSeq = "11110-100";
		String expectedAptNm = "MID그린(7동)";
		
		AptInfo result = repo.selectAptInfoByAptSeq(aptSeq);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(expectedAptNm, result.getAptNm());

	}
	
	/*
	 * 아파트 번호로 아파트 정보 조회
	 */
	@Test
	public void selectAptDealByAptNmTest() {
		String aptNm = "경희궁";
		int expectedCount = 672;
		
		List<AptDeal> result = repo.selectAptDealByAptNm(aptNm);
		Assertions.assertEquals(result.size(), expectedCount);
	}
}

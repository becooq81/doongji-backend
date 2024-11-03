package com.ssafy.home.location.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class LocationRepositoryTest {
	
	
	@Autowired
	LocationRepository repo;
	
	@Test
	public void selectGugunTest() {
		String si = "서울특별시";
		int expectedCount = 25;
		Assertions.assertEquals(repo.selectGugun(si).size(), expectedCount);
	}
	
	@Test
	public void selectDongTest() {
		String si = "서울특별시";
		String gugun = "종로구";
		int expectedCount = 87;
		Assertions.assertEquals(repo.selectDong(si, gugun).size(), expectedCount);
	}
	
	@Test
	public void selectDongCodeTest() {
		String si = "서울특별시";
		String gugun = "종로구";
		String dong = "궁정동";
		String expected = "1111010300";
		Assertions.assertEquals(repo.selectDongCode(si, gugun, dong), expected);	
	}
}

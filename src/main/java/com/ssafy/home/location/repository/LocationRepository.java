package com.ssafy.home.location.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LocationRepository {
	
	// 시, 군구 값으로 모든 동을 찾는다
	List<String> selectDong(String si, String gugun);
	
	// 시 값으로 모든 구군을 찾는다
	List<String> selectGugun(String si);
	
	// 시, 군구, 동 값으로 일치하는 동코드를 찾는다
	String selectDongCode(String si, String gugun, String dong);
	
	// 모든 시도 값을 찾는다
	List<String> selectSido();
	
}

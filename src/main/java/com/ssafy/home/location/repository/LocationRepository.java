package com.ssafy.home.location.repository;

import java.util.List;

public interface LocationRepository {
	
	List<String> selectDong(String si, String gugun);
	List<String> selectGugun(String si);
	
}

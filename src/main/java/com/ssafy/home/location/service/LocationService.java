package com.ssafy.home.location.service;

import com.ssafy.home.location.payload.response.DongList;
import com.ssafy.home.location.payload.response.GugunList;

public interface LocationService {
	DongList findDongBySiGugun(String si, String gugun);
	GugunList findGugunBySi(String si);
}

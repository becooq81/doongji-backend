package com.ssafy.home.location.service;

import com.ssafy.home.location.payload.response.DongCode;
import com.ssafy.home.location.payload.response.DongList;
import com.ssafy.home.location.payload.response.GugunList;
import com.ssafy.home.location.payload.response.SidoList;


public interface LocationService {
	DongList findDongBySiGugun(String sido, String gugun);
	GugunList findGugunBySi(String sido);
	DongCode findDongCode(String sido, String gugun, String dong);
	SidoList findSido();
}

package com.find.doongji.location.service;

import com.find.doongji.location.payload.response.DongCode;
import com.find.doongji.location.payload.response.DongList;
import com.find.doongji.location.payload.response.GugunList;
import com.find.doongji.location.payload.response.SidoList;


public interface LocationService {
	DongList findDongBySiGugun(String sido, String gugun);
	GugunList findGugunBySi(String sido);
	DongCode findDongCode(String sido, String gugun, String dong);
	SidoList findSido();
}

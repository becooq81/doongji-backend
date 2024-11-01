package com.ssafy.home.apt.service;

import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;

import com.ssafy.home.apt.payload.response.AptDeal;
import com.ssafy.home.apt.payload.response.AptInfo;

public interface AptService {
	
	/**
	 * @param aptName
	 * @return 아파트 명에 해당하는 아파트 거래 내역 반환
	 * @throws NotFoundException 
	 */
	List<AptDeal> findAptDealByAptNm(String aptName) throws NotFoundException;

	/**
	 * 
	 * @param aptSeq
	 * @return 아파트 번호에 해당하는 아파트 정보 반환
	 * @throws NotFoundException 
	 */
	AptInfo findAptInfoByAptSeq(String aptSeq) throws NotFoundException;
	
	/**
	 * 
	 * @param sido
	 * @param gugun
	 * @param dong
	 * @return 시, 구군, 동 값에 해당하는 아파트 거래 내역 반환
	 * @exception NotFoundException
	 */
	List<AptDeal> findAptDealByDong(String sido, String gugun, String dong) throws NotFoundException;
	
	
}

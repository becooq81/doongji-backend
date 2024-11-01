package com.ssafy.home.apt.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.home.apt.payload.response.AptDeal;
import com.ssafy.home.apt.payload.response.AptInfo;

@Mapper
public interface AptRepository {

	/**
	 * @param aptSeq
	 * @return aptSeq에 해당하는 아파트 거래 내역 반환
	 */
	List<AptDeal> selectAptDealByAptSeq(String aptSeq);
	
	
	/**
	 * @param sggCd (시군구 코드), umdCd (읍면동 코드)
	 * @return 시군구, 읍면동 코드에 해당하는 아파트 거래 내역 반환
	 */
	List<AptDeal> selectAptDealByDong(@Param("sggCd") String sggCd, @Param("umdCd") String umdCd);

	/**
	 *  @param aptNm (아파트 명): 부분 검색 가능
	 *  @return 아파트 명에 해당하는 아파트 번호 반환
	 */
	List<String> selectAptSeqByAptNm(String aptNm);
	
	/**
	 * @param aptSeq
	 * @return 아파트 번호에 해당하는 아파트 정보 반환
	 */
	AptInfo selectAptInfoByAptSeq(String aptSeq);
	
	/**
	 * @param aptNm
	 * @return 아파트명에 해당하는 아파트 거래 내역 반환

	 */
	List<AptDeal> selectAptDealByAptNm(String aptNm);
}
